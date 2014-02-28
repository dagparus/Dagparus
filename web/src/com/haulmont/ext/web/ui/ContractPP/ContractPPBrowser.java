package com.haulmont.ext.web.ui.ContractPP;


import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.core.global.MessageProvider;
import com.haulmont.cuba.core.global.MetadataProvider;
import com.haulmont.cuba.core.global.UserSessionProvider;
import com.haulmont.cuba.gui.ServiceLocator;
import com.haulmont.cuba.gui.UserSessionClient;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.actions.CreateAction;
import com.haulmont.cuba.gui.components.actions.EditAction;
import com.haulmont.cuba.gui.components.actions.RefreshAction;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.GroupDatasource;
import com.haulmont.cuba.gui.data.ValueListener;
import com.haulmont.cuba.gui.data.impl.CollectionDsListenerAdapter;
import com.haulmont.cuba.gui.data.impl.DsListenerAdapter;
import com.haulmont.cuba.gui.report.ReportHelper;
import com.haulmont.cuba.gui.settings.Settings;
import com.haulmont.cuba.report.Report;
import com.haulmont.cuba.security.entity.EntityOp;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.web.App;
import com.haulmont.cuba.web.filestorage.WebExportDisplay;
import com.haulmont.cuba.web.gui.components.WebComponentsHelper;
import com.haulmont.cuba.web.gui.components.WebFilter;
import com.haulmont.cuba.web.gui.components.WebPopupButton;
import com.haulmont.docflow.core.app.CardService;
import com.haulmont.docflow.core.entity.Doc;
import com.haulmont.docflow.web.DocflowAppWindow;
import com.haulmont.docflow.web.actions.ThesisExcelAction;
import com.haulmont.docflow.web.ui.common.DocCreator;
import com.haulmont.docflow.web.ui.common.DocflowHelper;
import com.haulmont.ext.core.entity.ContractPP;
import com.haulmont.taskman.core.app.TaskmanService;
import com.haulmont.taskman.web.ui.common.RemoveCardNullChildAction;
import com.haulmont.taskman.web.ui.task.ProcessMenuBuilderAction;
import com.haulmont.workflow.core.app.WfService;
import com.haulmont.workflow.core.app.WfUtils;
import com.haulmont.workflow.core.entity.Card;
import com.haulmont.workflow.core.entity.CardInfo;
import com.haulmont.workflow.web.ui.base.ResolutionsFrame;
import com.haulmont.workflow.web.ui.base.action.CardContext;
import com.vaadin.terminal.ThemeResource;
import org.apache.commons.lang.BooleanUtils;

import javax.inject.Inject;
import java.util.*;

public class ContractPPBrowser extends AbstractLookup {

    @Inject
    protected GroupTable contractPPTable;
    @Inject
    protected GroupDatasource<ContractPP, UUID> contractPPDs;
    @Inject
    protected WebFilter genericFilter;
    @Inject
    private java.util.List<CardInfo> infoList;

    @Inject
    protected ResolutionsFrame contractPPFrame;

    @Inject
    protected CheckBox hideResolutions;

    @Inject
    protected PopupButton createButton;

    @Inject
    protected TaskmanService taskman_TaskmanService;

    @Inject
    protected CardService docflow_CardService;

    protected List<Card> cards;

    private boolean inTemplates;

    protected PopupButton printButton;

    protected String entityName;

    public ContractPPBrowser(IFrame frame) {
        super(frame);
        entityName = MetadataProvider.getSession().getClass(MetadataProvider.getReplacedClass(ContractPP.class)).getName();
    }


    @Override
    public void init(Map<String, Object> params) {
        super.init(params);
        //установка возможности выбрать несколько карточек:
        contractPPTable.setMultiSelect(true);
        inTemplates = false;
        if (createButton != null) {
            //проверка прав текущего пользователя на создание карточек типа
            //Счет на оплату. В зависимости от прав пользователя установка
            //возможности создавать карточку
            createButton.setEnabled(UserSessionProvider.getUserSession().isEntityOpPermitted(contractPPDs.getMetaClass(), EntityOp.CREATE));
            //добавление действия "Создать новую карточку"
            createButton.addAction(new CreateAction());
            //добавление действия "Копировать существующую карточку"
            createButton.addAction(new CopyAction());
        }

        //добавление обработчика событий, в котором будет происходить проверка на связь удаляемой карточки со связанными с нею карточками
        //при подтверждении удаления карточка будет удалена, а в связанной карточке будет удалена связь.
        contractPPTable.addAction(new RemoveCardNullChildAction("remove", this, contractPPTable));

        contractPPTable.addAction(new EditAction(contractPPTable) {

            @Override
            protected void afterWindowClosed(Window window) {
                super.afterWindowClosed(window);
                refresh();
            }
        });

        contractPPTable.addAction(new RefreshAction(contractPPTable));
        contractPPTable.addAction(new ThesisExcelAction(contractPPTable,
                new WebExportDisplay(), genericFilter));
        //добавление обработчика событий по удалению просмотренных оповещений
        contractPPTable.addAction(new DeleteNotification());

        cards = docflow_CardService.getCardsForCurrentUserActor();

        //выделение строчек жирным если на них имеется уведомления
        //для текущего пользователя
        initTableColoring();
        contractPPDs.addListener(new DsListenerAdapter() {
            @Override
            public void stateChanged(Datasource ds, Datasource.State prevState,
                                     Datasource.State state) {
                super.stateChanged(ds, prevState, state);
                if (Datasource.State.VALID.equals(state)) {
                    loadCardInfoList();
                }
            }
        });

        final Card exclItem = (Card) params.get("exclItem");
        if (exclItem != null) {
            this.setLookupValidator(new Validator() {
                public boolean validate() {
                    Card selectedCard = contractPPTable.getSingleSelected();
                    CardService cardService = ServiceLocator.lookup(CardService.NAME);
                    if (selectedCard != null && cardService.isDescendant(exclItem, selectedCard,
                            MetadataProvider.getViewRepository().getView(Card.class, "_minimal"), "parentCard")) {
                        showNotification(MessageProvider.getMessage(ContractPPBrowser.class, "cardIsChild"),
                                NotificationType.WARNING);
                        return false;
                    }

                    return true;
                }
            });
        }

        addCommentColumn();
        addHasAttachmentColumn();

        // Кнопка печати
        printButton = getComponent("printButton");
        if (printButton != null) {
            printButton.addAction(new ActionPrint("Contract_LO"));
            printButton.addAction(new ActionPrint("ContractOU"));
            printButton.addAction(new ActionPrint("ContractPI"));
            printButton.addAction(new ActionPrint("ContractppAV"));
            printButton.addAction(new ActionPrint("Contractpp_LO_AV"));
        }
    }

    class ActionPrint extends AbstractActionPrint {
        ActionPrint(String id) {
            super(id);
        }

        @Override
        public void actionPerform(Component component) {
            ContractPP entity = contractPPDs.getItem();
            Map<String, Object> entites = new HashMap<String, Object>();
            entites.put("entity", entity);
            Report report = loadReport(MessageProvider.getMessage(ContractPPEditor.class, reportName));
            report = getDsContext().getDataService().reload(report, "report.edit");
            //openWindow("report$inputParameters", WindowManager.OpenType.DIALOG, Collections.<String, Object>singletonMap("report", report));
            ReportHelper.printReport(report, entites);
        }
    }

    public void putParamsByCreateNew(Map<String, Object> params) {
        params.put("justCreated", true);
    }

    protected Doc initCurrentDoc(DocCreator creator) {
        return creator.getDocument();
    }

    protected class CreateAction extends AbstractAction {

        protected CreateAction() {
            super("create");
        }

        @Override
        public String getCaption() {
            return MessageProvider.getMessage(getClass(), "actions.CreateNew");
        }

        @Override
        public boolean isEnabled() {
            return super.isEnabled()
                    && UserSessionClient.getUserSession().isEntityOpPermitted(contractPPDs.getMetaClass(), EntityOp.CREATE);
        }

        public void actionPerform(Component component) {
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("docType", entityName);
            params.put("inTemplates", inTemplates);
            Object object = DocflowHelper.createDocEditorOrDocCreator(params);
            if (object instanceof DocCreator) {
                final DocCreator creator = (DocCreator) object;
                creator.addListener(
                        new CloseListener() {
                            public void windowClosed(String actionId) {
                                if (Editor.WINDOW_COMMIT.equals(actionId)) {

                                    Map<String, Object> params = new HashMap<String, Object>();
                                    params.put("initialTemplate", creator.getInitialTemplate());
                                    putParamsByCreateNew(params);
                                    Window editor = openEditor(entityName + ".edit", initCurrentDoc(creator),
                                            WindowManager.OpenType.THIS_TAB, params);
                                    editor.addListener(
                                            new CloseListener() {
                                                public void windowClosed(String actionId) {
                                                    // refresh browser regardless of actionId as the new document
                                                    // could be saved by DocCreator
                                                    refresh();
                                                }
                                            }
                                    );
                                }
                            }
                        }
                );
            }
            if (object instanceof Window) {
                final Window editor = (Window) object;
                editor.addListener(
                        new CloseListener() {
                            public void windowClosed(String actionId) {
                                // refresh browser regardless of actionId as the new document
                                // could be saved by DocCreator
                                refresh();
                            }
                        }
                );
            }
        }
    }


    protected void addHasAttachmentColumn() {
        MetaPropertyPath pp = contractPPDs.getMetaClass().getPropertyPath("hasAttachments");
        com.vaadin.ui.Table vTable = (com.vaadin.ui.Table) WebComponentsHelper.unwrap(contractPPTable);
        vTable.removeGeneratedColumn(pp);
        vTable.addGeneratedColumn(
                pp,
                new com.vaadin.ui.Table.ColumnGenerator() {
                    public com.vaadin.ui.Component generateCell(com.vaadin.ui.Table source, Object itemId, Object columnId) {
                        Doc doc = contractPPDs.getItem((UUID) itemId);
                        if (doc.getHasAttachments() != null && doc.getHasAttachments()) {
                            return new com.vaadin.ui.Embedded("", new ThemeResource("icons/attachment-small.png"));
                        }
                        return null;
                    }
                });
    }

    protected void refresh() {
        //обновление источник данных
        contractPPDs.refresh();
        loadCardInfoList();
    }

    //загрузка уведомлений
    protected void loadCardInfoList() {
        LoadContext lc = new LoadContext(CardInfo.class);
        LoadContext.Query q = lc
                .setQueryString("select ci from wf$CardInfo ci where ci.user.id" +
                        " = :userId");
        q.addParameter("userId", UserSessionProvider.getUserSession()
                .getCurrentOrSubstitutedUser());
        lc.setView("card-browse");
        infoList = ServiceLocator.getDataService().loadList(lc);
    }

    //добавление генератора стилей, который делает строчку жирной
    //если на нее имеется уведомление
    protected void initTableColoring() {
        com.vaadin.ui.Table vTable = (com.vaadin.ui.Table)
                WebComponentsHelper.unwrap(contractPPTable);
        vTable.setCellStyleGenerator(new com.vaadin.ui.Table.CellStyleGenerator() {

            public String getStyle(Object itemId, Object propertyId) {
                if (propertyId == null) {
                    if (!(itemId instanceof UUID))
                        return "";
                    ContractPP contractPP =
                            contractPPDs.getItem((UUID)itemId);
                    return getTableCellStyle(contractPP);
                }
                return "";
            }
        });
    }

    protected String getTableCellStyle(Card card) {
        if (infoList != null) {
            for (CardInfo cardInfo : infoList) {
                if (cardInfo.getCard().getId().equals(card.getId())
                        && cardInfo.getDeleteTs() == null) {
                    return "taskremind";
                }
            }
        }
        return "";
    }

    /** Apply user settings to all components of this window */
    @Override
    public void applySettings(Settings settings) {
        boolean isLookup = (getFrame() instanceof Window.Lookup);
        if (!isLookup) {
            super.applySettings(settings);
        }
        //initAfterSetLookup(isLookup);

        contractPPTable.removeAction(contractPPTable.getAction("refresh"));
        contractPPTable.removeAction(contractPPTable.getAction("excel"));
    }

    private void initAfterSetLookup(boolean isLookup) {
        //если экран открыт как экран с возможностью выбора экземпляров из списка
        //то панель с границей раздела (SplitPanel) растягивается на 100% и
        //отменяется возможность перемещения границы разделов
        if (isLookup) {
            SplitPanel split = getComponent("split");
            split.setSplitPosition(100);
            split.setLocked(true);
            contractPPTable.setMultiSelect(false);
        }
        //иначе добавляется генерируемая колонка
        else {
            ((com.vaadin.ui.Table) WebComponentsHelper.unwrap(contractPPTable)).addGeneratedColumn(contractPPDs.getMetaClass().getPropertyPath("locState"),
                    new com.vaadin.ui.Table.ColumnGenerator() {
                        public com.vaadin.ui.Component generateCell(com.vaadin.ui.Table source, Object itemId, Object columnId) {
                            UUID uuid = (UUID) itemId;
                            com.vaadin.ui.Component component;
                            Doc doc = contractPPDs.getItem(uuid);

                            boolean containsCard = cards != null ? cards.contains(doc) : false;
                            if (doc != null && !WfUtils.isCardInStateList(doc, "Finished", "Canceled") &&
                                    (containsCard || (!containsCard && WfUtils.isCardInState(doc, "New")))) {
                                //формируется кнопка, при нажатии на которую появляется выпадающий список с доступными
                                //для данной карточки действиями
                                final PopupButton popupButton = new WebPopupButton();
                                //получение списка состояний
                                popupButton.setCaption(contractPPDs.getItem((UUID) itemId).getLocState());



                                component = WebComponentsHelper.unwrap(popupButton);
                                ((org.vaadin.hene.popupbutton.PopupButton) component).addListener(new ProcessMenuBuilderAction(doc, contractPPTable, popupButton));
                                component.addStyleName("link");
                                component.addStyleName("dashed");
                            } else {
                                component = new com.vaadin.ui.Label(doc == null ? "" : doc.getLocState(), com.vaadin.ui.Label.CONTENT_XHTML);
                            }
                            component.setWidth("-1px");
                            return component;
                        }
                    });

            //инициализация журнала действий


            final SplitPanel split = getComponent("split");
            //добавление слушателя, который срабатывает при установлении или снятии галочки с опции "Скрыть журнал действий"
            /*hideResolutions.addListener(new ValueListener() {
                public void valueChanged(Object source, String property, Object prevValue, Object value) {
                    boolean hideResolutionsValue = BooleanUtils.isTrue((Boolean) value);
                    int pos = (hideResolutionsValue ? 100 : 60);
                    split.setSplitPosition(pos);
                    split.setLocked(hideResolutionsValue);

                    Card card = (Card) contractPPTable.getSingleSelected();



                }
            }); */

            String value = getSettings().get("hideResolutions").attributeValue("value");
            hideResolutions.setValue(value == null ? true : BooleanUtils.toBoolean(value));
        }
    }

    protected void addCommentColumn() {
        MetaPropertyPath pp = contractPPDs.getMetaClass().getPropertyPath("comment");
        com.vaadin.ui.Table vTable = (com.vaadin.ui.Table) WebComponentsHelper.unwrap(contractPPTable);
        vTable.removeGeneratedColumn(pp);
        vTable.addGeneratedColumn(
                pp,
                new com.vaadin.ui.Table.ColumnGenerator() {
                    public com.vaadin.ui.Component generateCell(com.vaadin.ui.Table source, Object itemId, Object columnId) {
                        UUID uuid = (UUID) itemId;
                        com.vaadin.ui.Component component;
                        Doc doc = contractPPDs.getItem(uuid);
                        String descr = doc.getComment();
                        int enterIdx = descr != null ? (descr.length() > 40 ? 40 : descr.indexOf('\n')) : -1;
                        //установка свойств отображаемого компонента
                        if (enterIdx != -1) {
                            com.vaadin.ui.TextField content = new com.vaadin.ui.TextField(null, descr);
                            content.setReadOnly(true);
                            content.setWidth("300px");
                            content.setHeight("300px");
                            component = new com.vaadin.ui.PopupView("<span>"
                                    + descr.substring(0, enterIdx) + "...</span>", content);
                            component.addStyleName("longtext");
                        } else {
                            component = new com.vaadin.ui.Label(descr == null ? "" : descr);
                        }

                        component.setWidth("-1px");
                        return component;
                    }
                });
    }

    protected class DeleteNotification extends AbstractAction {
        private static final long serialVersionUID = -4530817747247184231L;

        protected DeleteNotification() {
            super("deleteNotification");
        }

        public void actionPerform(Component component) {
            Set selected = contractPPTable.getSelected();
            if (selected.size() != 1) return;

            WfService wfService = ServiceLocator.lookup(WfService.NAME);
            User user = UserSessionProvider.getUserSession().getCurrentOrSubstitutedUser();
            wfService.deleteNotifications(contractPPDs.getItem(), user);

            refresh();
            //обновление папок приложение
            ((DocflowAppWindow) App.getInstance().getAppWindow()).reloadAppFolders(new ArrayList(selected));
        }
    }

    protected class CopyAction extends AbstractAction {

        protected CopyAction() {
            super("copy");
        }

        @Override
        public String getCaption() {
            return MessageProvider.getMessage(getClass(), "actions.Copy");
        }

        //делаем кнопку доступной, если у пользователя есть
        //права на создание данной карточки
        @Override
        public boolean isEnabled() {
            return super.isEnabled()
                    && UserSessionProvider.getUserSession().isEntityOpPermitted(contractPPDs.getMetaClass(), EntityOp.CREATE);
        }

        public void actionPerform(Component component) {
            //получение счета на оплату, который был выделен пользователем
            //в момент нажатия кнопки "Копировать"
            ContractPP contractPP = contractPPDs.getItem();
            if (contractPP != null) {
                LoadContext loadContext = new LoadContext(contractPP.getClass());
                loadContext.setId(contractPP.getId());
                loadContext.setView("edit");
                loadContext.setUseSecurityConstraints(false);
                contractPP = ServiceLocator.getDataService().load(loadContext);

                ContractPP doc;
                try {
                    //создаем новый документ Счет на оплату
                    doc = contractPP.getClass().newInstance();
                    doc.setTemplate(true);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                //копирование значений полей в новую карточку
                doc.copyFrom(contractPP);
                doc.setTemplate(contractPP.getTemplate());
                doc.setAmount(contractPP.getAmount());
                doc.setExtCompany(contractPP.getExtCompany());
                doc.setClient(contractPP.getClient());
                doc.setDateContr(contractPP.getDateContr());
                doc.setNumber(contractPP.getNumber());
                doc.setAmount(contractPP.getAmount());
                doc.setCreator(UserSessionProvider.getUserSession().getUser());
                doc.setSubstitutedCreator(UserSessionProvider.getUserSession().getCurrentOrSubstitutedUser());

                Map<String, Object> params = new java.util.HashMap<String, Object>();
                params.put("justCreated", true);
                Window window = openEditor(entityName + ".edit", doc, WindowManager.OpenType.THIS_TAB, params);
                window.addListener(
                        new CloseListener() {
                            public void windowClosed(String actionId) {
                                if (Window.COMMIT_ACTION_ID.equals(actionId)) {
                                    contractPPDs.refresh();
                                }
                            }
                        }
                );
            }
        }
    }

}