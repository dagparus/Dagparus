
package com.haulmont.ext.web.ui.ContractPP;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.client.ClientConfig;
import com.haulmont.cuba.core.app.DataService;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.gui.ServiceLocator;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.actions.RemoveAction;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.DsContext;
import com.haulmont.cuba.gui.data.impl.*;
import com.haulmont.cuba.gui.report.ReportHelper;
import com.haulmont.cuba.report.Report;
import com.haulmont.cuba.security.entity.EntityOp;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.security.entity.UserRole;
import com.haulmont.docflow.core.app.ThesisConstants;
import com.haulmont.cuba.gui.data.HierarchicalDatasource;
import com.haulmont.cuba.web.App;
import com.haulmont.cuba.web.gui.components.WebButton;
import com.haulmont.cuba.web.gui.components.WebVBoxLayout;
import com.haulmont.docflow.core.app.CardService;
import com.haulmont.docflow.core.app.NumerationService;
import com.haulmont.docflow.core.entity.Doc;
import com.haulmont.docflow.core.entity.DocKind;
import com.haulmont.docflow.core.entity.NumeratorType;
import com.haulmont.docflow.core.entity.Organization;
import com.haulmont.docflow.web.DocflowAppWindow;
import com.haulmont.docflow.web.ui.common.DocTypeSelector;
import com.haulmont.ext.core.entity.ContractPP;
import com.haulmont.ext.core.entity.ContractppModull;
import com.haulmont.ext.core.entity.Enum.ContractDocType;
import com.haulmont.ext.core.entity.ExtModull;
import com.haulmont.ext.core.entity.Villa;
import com.haulmont.ext.web.ui.Modull.ModullFrame;
import com.haulmont.taskman.web.ui.common.*;
import com.haulmont.workflow.core.entity.*;
import com.haulmont.workflow.web.ui.base.AbstractCardEditor;
import com.haulmont.workflow.web.ui.base.CardProcFrame;
import com.haulmont.workflow.web.ui.base.CardRolesFrame;
import com.haulmont.workflow.web.ui.base.action.ActionsFrame;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;

import javax.inject.Inject;
import java.util.*;

public class ContractPPEditor extends AbstractCardEditor {

    protected ContractPPAccessData accessData;

    protected boolean removedComment = false;

    protected boolean hierarchyTabInited = false;

    protected Boolean isTabComment;

    protected boolean historyTabInited = false;

    protected boolean versionsTabInitialized = false;

    private boolean key = false;

    protected DateField dateContr;

    protected DateField dateClose;

    @Inject
    protected ActionsField villa;

    @Inject
    protected Table modullTable;

    @Inject
    protected DsContext dsContext;

    @Inject
    protected CollectionDatasource<CardProc, UUID> cardProcDs;

    @Inject
    protected CollectionDatasource<ContractppModull, UUID> modullDs;

    @Inject
    protected PickerField parentCard;

    @Inject
    protected WebVBoxLayout startProc;

    @Inject
    protected com.haulmont.cuba.gui.data.DataService dataService;

    @Inject
    protected CardProcFrame cardProcFrame;

    @Inject
    protected WebVBoxLayout buttonStart;

    @Inject
    protected CollectionPropertyDatasourceImpl cardProjectsDs;

    @Inject
    Datasource cardDs;

    @Inject
    protected DataService cuba_DataService;

    @Inject
    protected NumerationService docflow_NumerationService;

    public ContractPPEditor(IFrame frame) {
        super(frame);
    }

    @Override
    protected boolean isCommentVisible() {
        return false;
    }

    @Override
    public void init(Map<String, Object> params) {
        super.init(params);
        //после закрытия окна редактирования папки приложения должны обновиться
        addListener(new CloseListener() {
            public void windowClosed(String actionId) {
                App.getInstance().getAppWindow().getFoldersPane().reloadAppFolders();
            }
        });

       /* modullTable = getComponent("modullTable");
        modullTable.addAction(new AddAction());
        modullTable.addAction(new RemoveAction());   */
             /*
        //получение источника данных, содержащего сущности комментариев
        //к карточкам
        final HierarchicalDatasource commentDs = ((IFrame) getComponent("cardCommentFrame")).getDsContext().get("commentDs");
        if (commentDs != null) {
            /*добавление слушателя, который следит за изменением
            количества комментариев к карточке
            если комментарии есть, то в название закладки
            добавляется число комментариев
            к данной карточке*/
           /* commentDs.addListener(new CollectionDsListenerAdapter() {
                public void collectionChanged(CollectionDatasource ds, Operation operation) {
                    for (Tabsheet.Tab tab : ((Tabsheet) getComponent("tabsheet")).getTabs()) {
                        if ("cardCommentTab".equals(tab.getName()) && !removedComment) {
                            int count = commentDs.size();
                            if (count > 0) {
                                tab.setCaption(getMessage("cardCommentTab") + " (" + count + ")");
                            } else {
                                tab.setCaption(getMessage("cardCommentTab"));
                            }
                        }
                    }
                }
            });
            //обновление источника данных
            commentDs.refresh();
        }
            */
        //добавление слушателя, который следит за изменением
        //количества вложений в карточке
        //если вложения есть, то в название закладки
        //добавляется число вложений
        //в данной карточке

        cardDs.addListener(new DsListenerAdapter() {
            public void valueChanged(Entity source, String property, Object prevValue, Object value) {
                Datasource cardDs = getDsContext().get("cardDs");
                dateContr = getComponent("dateContr");
                dateClose = getComponent("dateClose");

                Date dateValue = dateContr.getValue();
                if (dateValue != null){
                    GregorianCalendar calendar = new GregorianCalendar();
                    calendar.setTime(dateValue);
                    calendar.add(GregorianCalendar.YEAR, 1);
                    dateClose.setValue(calendar.getTime());
                }  }
        });

        attachmentsDs.addListener(new CollectionDsListenerAdapter() {
            @Override
            public void collectionChanged(CollectionDatasource ds, Operation operation) {
                for (Tabsheet.Tab tab : ((Tabsheet) getComponent("tabsheet")).getTabs()) {
                    if (tab.getName() != null && tab.getName().equals("attachmentsTab")) {
                        if (ds.getItemIds().size() > 0)
                            tab.setCaption(getMessage("attachments") + " (" + ds.getItemIds().size() + ")");
                        else
                            tab.setCaption(getMessage("attachments"));
                    }
                }
            }

            @Override
            public void itemChanged(Datasource ds, Entity prevItem, Entity item) {
                for (Action action : attachmentsTable.getActions()) {
                    if (((Card) getItem()).getJbpmProcessId() != null && !"actions.Create".equals(action.getId()) &&
                            !"actions.Copy".equals(action.getId()))
                        action.setEnabled(item instanceof CardAttachment);
                }
            }
        });

        //добавление слушателя, который следит за изменением
        //количества проектов в карточке
        //если проекты выбраны, то в название закладки
        //добавляется число проектов
        //в данной карточке
        cardProjectsDs.addListener(new CollectionDsListenerAdapter() {
            public void collectionChanged(CollectionDatasource ds, Operation operation) {
                Tabsheet tabsheet = getComponent("tabsheet");
                for (Tabsheet.Tab tab : tabsheet.getTabs()) {
                    if ("cardProjectsTab".equals(tab.getName())) {
                        if (cardProjectsDs.size() > 0)
                            tab.setCaption(getMessage("cardProjectsTab") + " (" + cardProjectsDs.size() + ")");
                        else
                            tab.setCaption(getMessage("cardProjectsTab"));
                    }
                }
            }
        });
        modullDs.addListener(new CollectionDsListenerAdapter() {
            public void collectionChanged(CollectionDatasource ds, Operation operation) {
                Tabsheet tabsheet = getComponent("tabsheet");
                for (Tabsheet.Tab tab : tabsheet.getTabs()) {
                    if ("modullTab".equals(tab.getName())) {
                        if (modullDs.size() > 0)
                            tab.setCaption(getMessage("modullTab") + " (" + modullDs.size() + ")");
                        else
                            tab.setCaption(getMessage("modullTab"));
                    }
                }

                ContractPP contractPP = (ContractPP) cardDs.getItem();
                int summ = 0;
                if (contractPP.getModull() != null) {
                    for (ContractppModull contractppModull : contractPP.getModull()) {
                        if (contractppModull.getModull() != null) {
                            summ += Integer.parseInt(contractppModull.getTotalCost());
                        }
                    }
                }
                contractPP.setAmount(String.valueOf(summ));
                contractPP.setTextAmount((TextNumber.start(summ)).trim());
            }
        });

        modullDs.addListener(new DsListenerAdapter() {
            public void valueChanged(Object source, String property, Object prevValue, Object value) {
                ContractppModull contractppModull = modullDs.getItem();
                if ("count".equals(property)) {
                        contractppModull.setTotalCost(String.valueOf(Integer.parseInt(contractppModull.getCount()) * Integer.parseInt(contractppModull.getModull().getPrice())));
                }
                ContractPP contractPP = (ContractPP) cardDs.getItem();
                int summ = 0;
                if (contractPP.getModull() != null) {
                    for (ContractppModull iContractppModull : contractPP.getModull()) {
                        if (iContractppModull.getModull() != null) {
                            summ += Integer.parseInt(iContractppModull.getTotalCost());
                        }
                    }
                }
                contractPP.setAmount(String.valueOf(summ));
                contractPP.setTextAmount((TextNumber.start(summ)).trim());
            }
        });

        //переопределение действия,которое будет происходить
        //при нажатии на кнопку поиска "родительской" карточки

        //удаление существующего обработчика событий
        /*parentCard.removeAction(parentCard.getAction(PickerField.LookupAction.NAME));
        //добавление нового обработчика событий
        parentCard.addAction(new PickerField.LookupAction(parentCard) {

            public void actionPerform(Component component) {
                if (DocTypeSelector.isNeeded("read")) {
                    //создание окна выбора типа карточки
                    //(Задача, Документ, Договор, Счет на оплату)
                    final DocTypeSelector docTypeSelector =
                            new DocTypeSelector("select", "read");
                    docTypeSelector.addListener(new com.vaadin.ui.Window.CloseListener() {
                        public void windowClose(com.vaadin.ui.Window.CloseEvent e) {
                            openCardLookup(docTypeSelector.getEntityName());
                        }
                    });
                    docTypeSelector.show();
                } else {
                    openCardLookup((getItem()).getMetaClass().getName());
                }
            }
        });   */
        //добавление обработчика событий
        //по открытию выбранной в качестве
        //основания карточки
      //  parentCard.addAction(new PickerField.OpenAction(parentCard));


        getDsContext().addListener(new DsContext.CommitListener() {
            //добавление слушателя, срабатывающего при
            //сохранении сущности в базу данных
            public void beforeCommit(CommitContext<Entity> context) {
                //получение сущности
                Doc doc = (Doc) getItem();
                //если сущность только что созданная
                if ((PersistenceHelper.isNew(doc))
                        //если пользователь
                        //не вводил номер счета
                        && StringUtils.isBlank(doc.getNumber())
                        //если нумератор для данной сущности
                        //существует и тип нумератора -
                        //при сохранении
                        && NumeratorType.ON_COMMIT.equals(doc.getDocKind().getNumeratorType())) {
                    //то получаем данный нумератор по имени
                    NumerationService ns = ServiceLocator.lookup(NumerationService.NAME);

                    //получение номер из последовательности
                    String num = ns.getNextNumber(doc);
                    if (num != null)
                        //с помощью метода доступа
                        //проставляем номер в документ
                        doc.setNumber(num);
                }

                CardSecurityFrame securityFrame = getComponent("securityFrame");
                if (securityFrame != null) {
                    context.getCommitInstances().addAll(securityFrame.getToCommit());
                }
            }

            @Override
            public void afterCommit(CommitContext<Entity> context, Set<Entity> result) {


                Set<Entity> rolesToCommit = new HashSet<Entity>();
                for (Entity entry : result) {
                    if (entry instanceof CardRole) {

                        rolesToCommit.add(entry);
                    }
                }
                CardRolesFrame frame = (CardRolesFrame) getComponent("cardProcFrame.cardRolesFrame");
                if (frame != null) {
                    frame.getActorActionsFieldsMap().clear();
                    CollectionDatasourceImpl tmpCardRolesDs = frame.getDsContext().get("tmpCardRolesDs");
                    if (tmpCardRolesDs != null)
                        tmpCardRolesDs.committed(rolesToCommit);
                }
                String template = TemplateHelper.processTemplate(getMessage("docEditor.caption"), Collections.<String, Object>singletonMap("item", getItem()));
                App.getInstance().getWindowManager().setCurrentWindowCaption((Window) getFrame(), template, null);
            }
        });

        //инициализация lazy-закладок
        initLazyTabs();
        isTabComment = BooleanUtils.isTrue((Boolean) params.get("isTabComment"));
        adjustForVersion(params);
    }

    @Override
    public void setItem(Entity item) {
        super.setItem(item);

        //получение сущности
        ContractPP contractPP = (ContractPP) getItem();

        Datasource cardDs = getDsContext().get("cardDs");
        villa = getComponent("villa");
        LoadContext ltx = new LoadContext(Villa.class);
        ltx.setQueryString("select v from ext$Villa v where v.villaname = :nameVilla")
                .addParameter("nameVilla", "г.Махачкала");
        ltx.setView("edit");
        Villa villa = cuba_DataService.load(ltx);
        this.villa.setValue(villa);

        initAttachments(contractPP);
        Organization org = new Organization();
        org.getAccounts().get(0);

        //блок для первоначальной инициализации новой карточки
        if (PersistenceHelper.isNew(contractPP)) {
            //установка создателя в карточку заявки
            contractPP.setCreator
                    (UserSessionProvider.getUserSession().getUser());
            //установка замещаемого создателя в карточку заявки
            contractPP.setSubstitutedCreator
                    (UserSessionProvider.getUserSession()
                            .getCurrentOrSubstitutedUser());

            //установка вида документа:
        /*    LoadContext ctx = new LoadContext(DocKind.class);
            ctx.setQueryString("select dk from df$DocKind dk where dk.docType = " +
                    "(select dt from df$DocType dt where dt.name = :typeName)")
                    .addParameter("typeName", "ext$ContractPP");
            ctx.setView("edit");
            DocKind docKind = cuba_DataService.load(ctx);
            contractPP.setDocKind(docKind);    */

            //установка времени
            contractPP.setDateTime(TimeProvider.currentTimestamp());
            //установка вида документа
            if (contractPP.getDocKind().getName().equals("Лицензионное обслуживание")) contractPP.setNameContract(ContractDocType.fromId("LS"));
            else if (contractPP.getDocKind().getName().equals("Оказание услуг")) contractPP.setNameContract(ContractDocType.fromId("PS"));
            else if (contractPP.getDocKind().getName().equals("Поставка экземпляра")) contractPP.setNameContract(ContractDocType.fromId("PD"));

            //если нумератор, подключенный к данной карточке
            //имеет тип "При создании"
          /*  if (NumeratorType.ON_CREATE.equals(contractPP.getDocKind().getNumeratorType())) {
                //то получаем данный нумератор
                //получение номера из последовательности
                String num = docflow_NumerationService.getNextNumber(contractPP);
                if (num != null)
                    //с помощью метода доступа
                    //проставляем номер в документ
                    contractPP.setNumber(num);
            }                            */

            //проставление "родительской" карточки в текущую
            Card parentCard = ((Doc) item).getParentCard();
            if (parentCard != null) {
                contractPP.setParentCard(parentCard);
            }
        }

        //удаление нотификаций для данной карточки
        deleteNotifications(contractPP);
        //обновление папок приложений
        ((DocflowAppWindow) App.getInstance().getAppWindow()).reloadAppFolders(Collections.singletonList(contractPP));

        //получение процесса, доступного для данной карточки
        Proc process = contractPP.getProc();
        boolean isActiveProc = false;
        /*   AbstractWfAccessData */
        accessData = getContext().getParamValue("accessData");
        if (!(accessData == null || accessData.getStartCardProcessEnabled()))
            isActiveProc = true;
        else if (process != null)
            for (UUID uuid : cardProcDs.getItemIds()) {
                CardProc cpt = cardProcDs.getItem(uuid);
                if (cpt.getProc().getId().equals(process.getId())
                        && cpt.getActive()) {
                    isActiveProc = true;
                }
            }

        //если процесс активен для данной карточки
        if (!isActiveProc) {
            //делаем видимой панель с кнопками,
            //отражающими действия для данного процесса
            startProc.setVisible(true);
            CollectionDatasource<Proc, UUID> procDs = cardProcFrame
                    .getDsContext().get("procDs");
            if (procDs.getState() == Datasource.State.VALID) {
                for (UUID uuid : procDs.getItemIds()) {
                    final Proc proc = procDs.getItem(uuid);

                    //создание кнопки запуска процесса
                    Button button = new WebButton();
                    button.setWidth("200px");
                    button.setAction(new AbstractAction("start" +
                            proc.getJbpmProcessKey()) {
                        public void actionPerform(Component component) {
                            startProcess(proc);
                        }

                        @Override
                        public String getCaption() {
                            return proc.getName();
                        }
                    });
                    buttonStart.add(button);
                }
            }
        }

        if (isTabComment)
            ((Tabsheet) getComponent("tabsheet")).setTab("cardCommentTab");
    }

    //создание диалога подтверждения запуска
    //процесса
    private void startProcess(final Proc proc) {
        showOptionDialog(
                MessageProvider
                        .getMessage(cardProcFrame.getClass(), "runProc.title"),
                String.format(MessageProvider
                        .getMessage(cardProcFrame.getClass(), "runProc.msg"),
                        proc.getName()),
                IFrame.MessageType.CONFIRMATION,
                new Action[]{
                        new DialogAction(DialogAction.Type.YES) {
                            @Override
                            public void actionPerform(Component component) {
                                CardProc cp = null;
                                for (UUID uuid : cardProcDs.getItemIds()) {
                                    CardProc cpt = cardProcDs.getItem(uuid);
                                    if (cpt.getProc().getId().equals(proc.getId())) {
                                        cp = cpt;
                                    }
                                }
                                if (cp == null) {
                                    cp = new CardProc();
                                    cp.setCard((ContractPP) getItem());
                                    cp.setProc(proc);
                                    cp.setActive(false);
                                    cp.setSortOrder(cardProcFrame.calculateSortOrder());
                                    cardProcDs.addItem(cp);
                                    cardProcFrame.getCardRolesFrame()
                                            .procChanged(cp.getProc());
                                    cardProcFrame.getCardRolesFrame()
                                            .initDefaultActors(cp.getProc());
                                }
                                cardProcFrame.startProcess(cp);
                            }
                        },
                        new DialogAction(DialogAction.Type.NO)
                }
        );

    }

    @Override
    protected void initActionsFrame(Card card, ActionsFrame actionsFrame) {
        actionsFrame.initActions(card, isCommentVisible());
    }

    private void openCardLookup(String entityName) {
        if (entityName == null) {
            return;
        }

        openLookup(entityName + ".browse", new Lookup.Handler() {
            public void handleLookup(Collection items) {
                if (items != null && items.size() > 0) {
                    ((Card) getItem()).setParentCard((Card) items.iterator().next());
                }
            }
        }, WindowManager.OpenType.THIS_TAB, Collections
                .<String, Object>singletonMap("exclItem", getItem()));
    }

    //инициализация  lazy-закладок
    private void initLazyTabs() {
        Tabsheet tabsheet = getComponent("tabsheet");
        accessData = getContext().getParamValue("accessData");
        tabsheet.addListener(new Tabsheet.TabChangeListener() {
            public void tabChanged(Tabsheet.Tab newTab) {
                if ("cardProjectsTab".equals(newTab.getName())) {
                    CardProjectsFrame cardProjectsFrame = getComponent("cardProjectsFrame");
                    cardProjectsFrame.init();
                    cardProjectsFrame.getComponent("add").setVisible(true);
                    cardProjectsFrame.getComponent("delete").setVisible(true);
                    if (!accessData.getNotVersion()) {
                        cardProjectsFrame.setEditable(false);
                    }
                } else if ("history".equals(newTab.getName())) {
                    initHistoryTab();
                } else if ("hierarchy".equals(newTab.getName())) {
                    initHierarchyTab();
                } else if ("securityTab".equals(newTab.getName())) {
                    CardSecurityFrame securityFrame = getComponent("securityFrame");
                    securityFrame.init();
                    securityFrame.refreshDs();
                    securityFrame.setEditable(accessData.getNotVersion());
                } else if ("modullTab".equals(newTab.getName())) {
                    initModullsTab();
                }
            }
        });
    }

    private boolean _commit() {
        if (modullDs.isModified()) {
            DatasourceImplementation modullDsImpl = (DatasourceImplementation) modullDs;

            CommitContext ctx = new CommitContext(Collections.emptyList(), modullDsImpl.getItemsToDelete());
            dataService.commit(ctx);

            ArrayList modifiedModull = new ArrayList(modullDsImpl.getItemsToCreate());
            modifiedModull.addAll(modullDsImpl.getItemsToUpdate());
            modullDsImpl.committed(Collections.<Entity>emptySet());
            for (Object modull : modifiedModull) {
                modullDsImpl.modified((Entity) modull);
            }
        }

        boolean isNew = PersistenceHelper.isNew(cardDs.getItem());
        return true;

    }

    @Override
    public void commitAndClose() {
        if (_commit()) {
            super.commitAndClose();
        }
    }

    private class AddModullAction extends AbstractAction {

        public AddModullAction() {
            super("add");
            icon = "icons/add.png";
        }

        public void actionPerform(Component component) {
            Map<String, Object> lookupParams = Collections.<String, Object>singletonMap("windowOpener", "ext$ContractPP.edit");
            openLookup("ext$Modull.browse", new Lookup.Handler() {
                public void handleLookup(Collection items) {
                    ContractPP contractPP = (ContractPP) cardDs.getItem();
                    Collection<String> existingModullNames = getExistingModullNames();
                    for (Object item : items) {
                        ExtModull modull = (ExtModull)item;
                        if (existingModullNames.contains(modull.getName())) continue;

                        final MetaClass metaClass = modullDs.getMetaClass();
                        ContractppModull contractppModull = dataService.newInstance(metaClass);
                        contractppModull.setModull(modull);
                        contractppModull.setContractPP((ContractPP) cardDs.getItem());
                        contractppModull.setCount("1");
                        contractppModull.setTotalCost(modull.getPrice());
                        modullDs.addItem(contractppModull);
                        existingModullNames.add(modull.getName());
                    }
                }

                private Collection<String> getExistingModullNames() {
                    ContractPP contractPP = (ContractPP) cardDs.getItem();
                    Collection<String> existingModullNames = new HashSet<String>();
                    if (contractPP.getModull() != null) {
                        for (ContractppModull contractppModull : contractPP.getModull()) {
                            if (contractppModull.getModull() != null) {
                                existingModullNames.add(contractppModull.getModull().getName());
                            }
                        }
                    }
                    return existingModullNames;
                }

            }, WindowManager.OpenType.THIS_TAB, lookupParams);
        }
    }

    private class RemoveModullAction extends RemoveAction {

        private boolean hasDefaultRole = false;

        public RemoveModullAction(ListComponent owner, boolean autocommit) {
            super(owner, autocommit);
        }

        @Override
        protected void confirmAndRemove(Set selected) {
         //   hasDefaultRole = hasDefaultRole(selected);
            super.confirmAndRemove(selected);
        }

        @Override
        public String getConfirmationMessage(String messagesPackage) {
            if (hasDefaultRole)
                return getMessage("dialogs.Confirmation.RemoveDefaultRole");
            else
                return super.getConfirmationMessage(messagesPackage);
        }

        private boolean hasDefaultRole(Set selected) {
            for (Object roleObj : selected) {
                UserRole role = (UserRole) roleObj;
                if (Boolean.TRUE.equals(role.getRole().getDefaultRole()))
                    return true;
            }
            return false;
        }

    }

    protected void initModullsTab() {
        if (versionsTabInitialized)
            return;

        modullTable = getComponent("modullTable");
        modullDs = getDsContext().get("modullDs");
     //   modullTable.getDatasource().setItem(null);
        modullDs.setItem(null);
        modullTable.addAction(new AddModullAction());
        modullTable.addAction(new RemoveModullAction(modullTable, false));
        versionsTabInitialized = true;
    }

    private void initHistoryTab() {
        if (historyTabInited) {
            return;
        }

        Tabsheet tabsheet = getComponent("historyTabsheet");
        tabsheet.addListener(new Tabsheet.TabChangeListener() {
            public void tabChanged(Tabsheet.Tab newTab) {
                if ("versionsTab".equals(newTab.getName())) {
                    initVersionsTab();
                }
            }
        });

        historyTabInited = true;
    }

    protected void initVersionsTab() {
        if (versionsTabInitialized)
            return;

        Table versionsTable = getComponent("versionsTable");
        OpenVersionAction openVersionAction = new OpenVersionAction(versionsTable);
        versionsTable.addAction(openVersionAction);
        versionsTabInitialized = true;
    }

    protected class OpenVersionAction extends AbstractAction {

        private final Table versionsTable;

        public OpenVersionAction(Table versionsTable) {
            super("edit");
            this.versionsTable = versionsTable;
        }

        @Override
        public String getCaption() {
            return getMessage("open");
        }

        public void actionPerform(Component component) {
            Set selected = versionsTable.getSelected();
            if (selected.size() != 1)
                return;

            Doc docVersion = (Doc) selected.iterator().next();

            openEditor(versionsTable.getDatasource().getMetaClass().getName() + ".edit",
                    docVersion, WindowManager.OpenType.THIS_TAB);
        }
    }

    private void initHierarchyTab() {
        final CardTreeFrame cardTreeFrame = getComponent("cardTreeFrame");
        accessData = getContext().getParamValue("accessData");
        if (hierarchyTabInited) {
            cardTreeFrame.refreshDs();
            return;
        }
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("card", getItem());
        cardTreeFrame.init(params);
        cardTreeFrame.refreshDs();
        final CollectionDatasource cardRelationsDs = getDsContext().get("cardRelationsDs");
        final Tabsheet tabsheet = getComponent("hierarchyTabsheet");
        CardService cardService = ServiceLocator.lookup(CardService.NAME);
        Integer count = cardService.getCardRelationsCount((Card) getItem());
        if (count > 0) {
            tabsheet.getTab("cardRelationsTab").setCaption(getMessage("cardRelationsTab") + " (" + count + ")");
        } else {
            tabsheet.getTab("cardRelationsTab").setCaption(getMessage("cardRelationsTab"));
        }
        cardRelationsDs.addListener(new CollectionDsListenerAdapter() {
            @Override
            public void collectionChanged(CollectionDatasource ds, Operation operation) {
                super.collectionChanged(ds, operation);
                Tabsheet.Tab tab = tabsheet.getTab("cardRelationsTab");
                if (tab != null) {
                    int count = cardRelationsDs.size();
                    if (count > 0) {
                        tab.setCaption(getMessage("cardRelationsTab") + " (" + count + ")");
                    } else {
                        tab.setCaption(getMessage("cardRelationsTab"));
                    }
                }
            }
        });

        tabsheet.addListener(new Tabsheet.TabChangeListener() {
            public void tabChanged(Tabsheet.Tab newTab) {
                if ("docTreeTab".equals(newTab.getName())) {
                    cardTreeFrame.refreshDs();
                } else if ("cardRelationsTab".equals(newTab.getName())) {
                    CardRelationsFrame cardRelationsFrame = getComponent("cardRelationsFrame");
                    cardRelationsFrame.init();
                    cardRelationsFrame.setEditable(accessData.getNotVersion());
                }
            }
        });

        hierarchyTabInited = true;
    }

    protected void adjustForVersion(Map<String, Object> params) {
        accessData = getContext().getParamValue("accessData");
        if (accessData != null) {
            Tabsheet ts = getComponent("tabsheet");
            Component resolutionsPane = getComponent("resolutionsPane");
            if (!accessData.getNotVersion()) {
                ts.removeTab("history");
                ts.removeTab("processTab");
                resolutionsPane.setVisible(false);
                setAttachmentButtonsEnabled(false);
                attachmentsDs.addListener(new CollectionDsListenerAdapter() {
                    @Override
                    public void itemChanged(Datasource ds, Entity prevItem, Entity item) {
                        setAttachmentButtonsEnabled(false);
                    }
                });
            }
        }
    }

    protected void setAttachmentButtonsEnabled(boolean enable) {
        String[] componentNames = {"createAttachBtn", "editAttach", "removeAttach", "pasteAttach"};
        for (String componentName : componentNames) {
            Component component = cardAttachmentsFrame.getComponent(componentName);
            if (component != null)
                component.setEnabled(false);
        }
    }
}
