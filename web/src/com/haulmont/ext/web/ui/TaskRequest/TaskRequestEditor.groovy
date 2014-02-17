package com.haulmont.ext.web.ui.TaskRequest

import com.haulmont.chile.core.model.Instance
import com.haulmont.cuba.core.entity.CategoryAttributeValue
import com.haulmont.cuba.core.entity.Entity
import com.haulmont.cuba.core.entity.SoftDelete
import com.haulmont.cuba.core.global.CommitContext
import com.haulmont.cuba.core.global.ConfigProvider
import com.haulmont.cuba.core.global.LoadContext
import com.haulmont.cuba.core.global.MessageProvider
import com.haulmont.cuba.core.global.MessageUtils
import com.haulmont.cuba.core.global.MetadataProvider
import com.haulmont.cuba.core.global.PersistenceHelper
import com.haulmont.cuba.core.global.TimeProvider
import com.haulmont.cuba.core.global.UserSessionProvider
import com.haulmont.cuba.core.global.View
import com.haulmont.cuba.core.sys.AppContext
import com.haulmont.cuba.core.sys.SetValueEntity
import com.haulmont.cuba.gui.AppConfig
import com.haulmont.cuba.gui.ServiceLocator
import com.haulmont.cuba.gui.UserSessionClient
import com.haulmont.cuba.gui.WindowManager
import com.haulmont.cuba.gui.app.core.lookup.SimpleLookup
import com.haulmont.cuba.gui.components.AbstractAction
import com.haulmont.cuba.gui.components.Action
import com.haulmont.cuba.gui.components.ActionAdapter
import com.haulmont.cuba.gui.components.ActionsField
import com.haulmont.cuba.gui.components.ActionsFieldHelper
import com.haulmont.cuba.gui.components.BoxLayout
import com.haulmont.cuba.gui.components.Button
import com.haulmont.cuba.gui.components.CheckBox
import com.haulmont.cuba.gui.components.Component
import com.haulmont.cuba.gui.components.DateField
import com.haulmont.cuba.gui.components.DialogAction
import com.haulmont.cuba.gui.components.FileUploadField
import com.haulmont.cuba.gui.components.IFrame
import com.haulmont.cuba.gui.components.Label
import com.haulmont.cuba.gui.components.LookupField
import com.haulmont.cuba.gui.components.PopupButton
import com.haulmont.cuba.gui.components.ResizableTextField
import com.haulmont.cuba.gui.components.RuntimePropertiesFrame
import com.haulmont.cuba.gui.components.Table
import com.haulmont.cuba.gui.components.Tabsheet
import com.haulmont.cuba.gui.components.TextField
import com.haulmont.cuba.gui.components.Window
import com.haulmont.cuba.gui.data.CollectionDatasource
import com.haulmont.cuba.gui.data.CollectionDatasourceListener
import com.haulmont.cuba.gui.data.Datasource
import com.haulmont.cuba.gui.data.DsContext
import com.haulmont.cuba.gui.data.HierarchicalDatasource
import com.haulmont.cuba.gui.data.RuntimePropertiesEntity
import com.haulmont.cuba.gui.data.RuntimePropsDatasource
import com.haulmont.cuba.gui.data.impl.CollectionDatasourceImpl
import com.haulmont.cuba.gui.data.impl.CollectionDsListenerAdapter
import com.haulmont.cuba.gui.data.impl.CollectionPropertyDatasourceImpl
import com.haulmont.cuba.gui.data.impl.DsListenerAdapter
import com.haulmont.cuba.gui.data.impl.RuntimePropsDatasourceImpl
import com.haulmont.cuba.gui.settings.Settings
import com.haulmont.cuba.security.entity.PermissionType
import com.haulmont.cuba.security.entity.User
import com.haulmont.cuba.security.global.UserSession
import com.haulmont.cuba.web.App
import com.haulmont.cuba.web.gui.components.WebButton
import com.haulmont.cuba.web.gui.components.WebComponentsHelper
import com.haulmont.cuba.web.gui.components.WebHBoxLayout
import com.haulmont.cuba.web.gui.components.WebLabel
import com.haulmont.cuba.web.gui.components.WebPopupButton
import com.haulmont.cuba.web.gui.components.WebVBoxLayout
import com.haulmont.cuba.web.toolkit.ui.Box
import com.haulmont.docflow.core.app.CardService
import com.haulmont.docflow.core.app.NumerationService
import com.haulmont.docflow.core.app.ThesisConstants
import com.haulmont.docflow.core.app.ThesisService
import com.haulmont.docflow.core.entity.FieldInfo
import com.haulmont.docflow.core.entity.NumeratorType
import com.haulmont.docflow.web.DocflowAppWindow
import com.haulmont.docflow.web.ui.common.CardAttachmentHelper
import com.haulmont.docflow.web.ui.common.DocTypeSelector
import com.haulmont.docflow.web.ui.common.DocflowHelper
import com.haulmont.docflow.web.ui.common.ThesisPrintHelper
import com.haulmont.ext.core.entity.TaskRequest
import com.haulmont.taskman.core.app.TaskmanService
import com.haulmont.taskman.core.app.TmConfig
import com.haulmont.taskman.core.entity.AbstractTask
import com.haulmont.taskman.core.entity.TaskGroup
import com.haulmont.taskman.core.entity.TaskPattern
import com.haulmont.taskman.web.ui.common.CardProjectsFrame
import com.haulmont.taskman.web.ui.common.CardRelationsFrame
import com.haulmont.taskman.web.ui.common.CardSecurityFrame
import com.haulmont.taskman.web.ui.common.CardTreeFrame
import com.haulmont.taskman.web.ui.common.CreateDependentTaskAction
import com.haulmont.taskman.web.ui.common.CreateSubCardAction
import com.haulmont.taskman.web.ui.common.ReminderBrowser
import com.haulmont.taskman.web.ui.settings.ResizeHelper
import com.haulmont.taskman.web.ui.task.ReassignForm
import com.haulmont.taskman.web.ui.task.TaskAccessData
import com.haulmont.taskman.web.ui.task.TaskDependentFrame
import com.haulmont.taskman.web.ui.templates.TemplateSettingHelper
import com.haulmont.workflow.core.app.WfService
import com.haulmont.workflow.core.app.WfUtils
import com.haulmont.workflow.core.app.WorkCalendarService
import com.haulmont.workflow.core.entity.Assignment
import com.haulmont.workflow.core.entity.Attachment
import com.haulmont.workflow.core.entity.Card
import com.haulmont.workflow.core.entity.CardAttachment
import com.haulmont.workflow.core.entity.CardRole
import com.haulmont.workflow.core.entity.Proc
import com.haulmont.workflow.core.entity.ProcRole
import com.haulmont.workflow.core.global.TimeUnit
import com.haulmont.workflow.core.global.WfConstants
import com.haulmont.workflow.web.ui.base.AbstractCardEditor
import com.haulmont.workflow.web.ui.base.CardAttachmentsFrame
import com.haulmont.workflow.web.ui.base.CardRolesFrame
import com.haulmont.workflow.web.ui.base.action.ActionsFrame
import com.haulmont.workflow.web.ui.base.action.ProcessAction
import com.vaadin.data.Property
import com.vaadin.ui.Select
import org.apache.commons.lang.BooleanUtils
import org.apache.commons.lang.StringUtils
import org.apache.commons.lang.time.DateUtils

import javax.inject.Inject

/**
 * Created with IntelliJ IDEA.
 * User: mahdi
 * Date: 11/1/13
 * Time: 3:20 PM
 * To change this template use File | Settings | File Templates.
 */
public class TaskRequestEditor extends AbstractCardEditor {

    protected static final String PARENT_CARD_FIELD_ID = 'parentCard'
    protected static final String PARENT_CARD_ACCESS_FIELD_ID = 'parentCardAccess'
    protected static final String ADD_CARD_ROLE_IN_PARENT_CARD = 'TaskRequest.addCardRoleInParentCard'
    private static final String EDIT_VIEW_ID = "edit"

    protected AbstractTask taskPattern
    protected TaskRequest task
    protected boolean isAutoFinishDate = false

    protected DateField finishDateTimePlanDateField
    protected TextField durationTextField

    protected TextField taskName
    protected TextField fullDescr
    protected WebVBoxLayout percentCompletion
    protected Label percentCompletionLabel

    protected ActionsField primaryTask;
    protected Label primaryTaskLabel;

    protected com.vaadin.ui.Slider percentCompletionSlider
    protected CollectionDatasource taskGroupsDs;

    protected Button openParentTaskButton

    protected NumeratorType numeratorType
    boolean enableNumField

    protected CollectionDatasource cardRolesDs

    protected Boolean isTabComment;
    protected TaskAccessData accessData;
    protected WfService wfService;

    protected boolean isYouTask;

    protected Label finishDateTimePlanLabel;
    protected DateField finishDateTimePlan;
    protected Button reminderButton

    protected java.util.List<Entity> remindersToCommit = new ArrayList<Entity>()
    protected java.util.List<CardAttachment> addedAttachment = new LinkedList<CardAttachment>()

    protected ActionsFrame actionsFrame

    @Inject
    protected PopupButton createTaskButton;

    @Inject
    protected Button createFromPatternButton;

    protected CheckBox parentCardAccess
    protected boolean parentCardAccessEditable
    protected boolean fromReminder;
    protected boolean cardIsEditable;

    def TaskRequestEditor(IFrame frame) {
        super(frame);
    }

    @Override
    public void init(Map<String, Object> params) {
        super.init(params);
        wfService = ServiceLocator.lookup(WfService.NAME)

        accessData = getContext().getParamValue("accessData")
        isYouTask = params.get("isYouTask");
        fromReminder = BooleanUtils.isTrue((Boolean) params.get("fromReminder"));
        if (fromReminder)
            params.remove("fromReminder")

        TmConfig config = ConfigProvider.getConfig(TmConfig.class)
        numeratorType = config.getTaskNumeratorType()
        enableNumField = config.getTaskNumberFieldEnabled()

        Label durationLabel = getComponent("durationLabel")
        WebHBoxLayout durationBox = getComponent("durationBox")
        Label taskTypeLabel = getComponent("taskTypeLabel")
        LookupField taskTypeLookupField = getComponent("taskType")

        durationLabel.setVisible(false)
        durationBox.setVisible(false)
        taskTypeLabel.setVisible(false)
        taskTypeLookupField.setVisible(false)

        HierarchicalDatasource commentDs = ((IFrame) getComponent("cardCommentFrame")).getDsContext().get("commentDs");
        if (commentDs != null) {
            commentDs.addListener(
                    new CollectionDsListenerAdapter() {
                        @Override
                        public void collectionChanged(CollectionDatasource ds, CollectionDatasourceListener.Operation operation) {
                            for (Tabsheet.Tab tab: ((Tabsheet) getComponent("tabsheet")).getTabs()) {
                                if ("cardCommentTab".equals(tab.getName())) {
                                    int count = commentDs.size()
                                    if (count > 0) {
                                        tab.setCaption(getMessage("cardCommentTab") + " (" + count + ")");
                                    } else {
                                        tab.setCaption(getMessage("cardCommentTab"));
                                    }
                                }
                            }
                        }

                        @Override
                        void valueChanged(Object source, String property, Object prevValue, Object value) {

                        }

                    });
            commentDs.refresh();
        }

        taskPattern = params['param$taskPattern']
        taskName = getComponent("taskName")
        fullDescr = getComponent("fullDescr")
        percentCompletion = getComponent("percentCompletion")
        percentCompletionLabel = getComponent("percentCompletionLabel")
        primaryTask = getComponent("primaryTask")
        primaryTaskLabel = getComponent("primaryTaskLabel")
        task = params.get("item")
        Label labourIntensity = getComponent("labourIntensity");
        if (task.getLabourIntensity() != null && task.getFinishDateTimeFact() != null) {
            if (task.getLabourUnit() != null) {
                switch (task.getLabourUnit()) {
                    case TimeUnit.MINUTE:
                        labourIntensity.setValue(String.format(getMessage("labour"), task.getLabourIntensity(), getMessage("minute")));
                        break;
                    case TimeUnit.HOUR:
                        labourIntensity.setValue(String.format(getMessage("labour"), task.getLabourIntensity(), getMessage("hour")));
                        break;
                    case TimeUnit.DAY:
                        labourIntensity.setValue(String.format(getMessage("labour"), task.getLabourIntensity(), getMessage("day")));
                        break;
                }
            } else {
                labourIntensity.setValue(String.format(getMessage("labour"), task.getLabourIntensity(), ""));
            }
        }

        LookupField taskType = getComponent('taskType')
        Select taskTypeComponent = (Select) WebComponentsHelper.unwrap(taskType)
        taskTypeComponent.setNullSelectionAllowed(false)

        taskGroupsDs = getDsContext().get("taskGroupsDs");
        taskGroupsDs.refresh();
        if (taskGroupsDs.size() > 0) {
            WebVBoxLayout taskGroup = getComponent("taskGroups");
            com.vaadin.ui.Button vButton;
            for (UUID uuid: taskGroupsDs.getItemIds()) {
                final TaskGroup tg = taskGroupsDs.getItem(uuid);
                final WebButton button = new WebButton();
                button.setCaption(tg.getName());
                vButton = WebComponentsHelper.unwrap(button);
                vButton.addListener(new com.vaadin.ui.Button.ClickListener() {
                    @Override
                    void buttonClick(com.vaadin.ui.Button.ClickEvent event) {
                        openTaskGroup(tg, button);
                    }
                });
                vButton.setStyleName("link");
                vButton.addStyleName("field");
                taskGroup.add(button);
            }
        } else {
            getComponent("taskGroups").setVisible(false);
            getComponent("taskGroupLabel").setVisible(false);
        }
        percentCompletionSlider = new com.vaadin.ui.Slider(0, 100)
        com.vaadin.ui.Label labelSlider = new com.vaadin.ui.Label("0%");
        labelSlider.addStyleName("slider")
        labelSlider.setWidth("4em")
        percentCompletionSlider.setWidth("100%");
        percentCompletionSlider.setImmediate(true);
        com.vaadin.ui.HorizontalLayout vHLayount = new com.vaadin.ui.HorizontalLayout();
        vHLayount.setWidth("100%");
        vHLayount.addComponent(labelSlider);
        vHLayount.addComponent(percentCompletionSlider);
        vHLayount.setComponentAlignment(percentCompletionSlider, com.vaadin.ui.Alignment.MIDDLE_RIGHT);
        vHLayount.setComponentAlignment(labelSlider, com.vaadin.ui.Alignment.MIDDLE_LEFT);
        vHLayount.setExpandRatio(percentCompletionSlider, 1.0f);

        percentCompletion.addComponent(vHLayount)
        percentCompletionSlider.addListener(new Property.ValueChangeListener() {
            @Override
            void valueChange(Property.ValueChangeEvent event) {
                task.setPercentCompletion((Integer) percentCompletionSlider.getValue())
                labelSlider.setValue(((Integer) percentCompletionSlider.getValue()).toString() + "%")
            }
        })
        labelSlider.setValue(((Integer) percentCompletionSlider.getValue()).toString() + "%")


        cardRolesDs = ((CollectionDatasource) cardDs.getDsContext().get("cardRolesDs"))
        //listener for executor and initiator
        cardRolesDs.addListener(new CollectionDsListenerAdapter() {
            @Override
            void valueChanged(Object source, String property, Object prevValue, Object value) {
                if ('user'.equals(property)) {
                    TaskRequest task = getItem()
                    CardRole cardRole = source
                    def user = cardRole.getUser()
                    if (cardRole.getCode().equals(TaskRequest.INITIATOR_ROLE) &&
                            ((user != null && !user.equals(task.getInitiator())) || user == null)) {
                        task.setInitiator(user)
                    } else if (cardRole.getCode().equals(TaskRequest.EXECUTOR_ROLE) &&
                            ((user != null && !user.equals(task.getExecutor())) || user == null)) {
                        task.setExecutor(user)
                    }
                }
            }
        })

        if (cardRolesFrame) {
            cardDs.addListener(new DsListenerAdapter() {
                @Override
                void valueChanged(Object source, String property, Object prevValue, Object value) {
                    if (property == 'taskName') {
                        String name = taskName.getValue()
                        String descr = fullDescr.getValue()
                        if (descr == null || descr.trim() == '') {
                            fullDescr.setValue(name)
                        }
                    }
                    else if (property == 'proc') {
                        cardRolesFrame.procChanged(value)
                        if (value) {
                            cardRolesFrame.initDefaultActors(value)
                        }
                    }
                    else if (property == 'category') {
                        adjustForTaskType(getItem())
                    }
                    else if (property == 'parentCard') {

                        choiceSetEditableParentCardAccess()

                        def parentCardValue = task?.getParentCard()
                        def parentCardAccessValue = parentCardAccess
                        if (isParentCardAccess() && parentCardValue != null && parentCardAccessValue != null) {
                            def message = getMessage(ADD_CARD_ROLE_IN_PARENT_CARD)
                            showNotification(message, IFrame.NotificationType.HUMANIZED)
                        }
                    }
                }
            })
        }

        if (createFromPatternButton) {
            createFromPatternButton.setAction(new ActionAdapter('createFromPattern', [
                    actionPerform: {
                        App.getInstance().getWindowManager().getDialogParams().setWidth(750).setHeight(600);
                        SimpleLookup.LookupConfig lookupConfig = new SimpleLookup.LookupConfig(
                                entity: 'tm$TaskPattern',
                                componentType: SimpleLookup.ComponentType.TABLE,
                                query: 'select tp from tm$TaskPattern tp order by tp.patternName',
                                columns: ['patternName'])

                        Map<String, Object> lookupParams = ['lookupConfig': lookupConfig]

                        openLookup('core$SimpleLookup', new Window.Lookup.Handler() {
                            @Override
                            void handleLookup(Collection items) {
                                TaskPattern taskPattern = items.asList()[0]
                                addedAttachment.clear();
                                fillTask(taskPattern);
//                                            Tabsheet tabsheet = getComponent("tabsheet");
                                //                                            if ("attachmentsTab".equals(tabsheet.getTab().getName()))
                                //
                            }

                        },
                                WindowManager.OpenType.DIALOG,
                                lookupParams)
                    },
                    getCaption: {
                        return getMessage('actions.fillFromPattern')
                    }
            ]))
        }

        finishDateTimePlanDateField = getComponent("finishDateTimePlan")
        durationTextField = getComponent("duration")



        ((ActionsField) getComponent('parentCard')).addAction(new ActionAdapter(ActionsField.LOOKUP, [
                actionPerform: {
                    if (DocTypeSelector.isNeeded("read")) {
                        DocTypeSelector docTypeSelector = new DocTypeSelector('select', 'read')
                        docTypeSelector.addListener(new com.vaadin.ui.Window.CloseListener() {
                            @Override
                            void windowClose(com.vaadin.ui.Window.CloseEvent e) {
                                openCardLookup(docTypeSelector.entityName)
                            }

                        })
                        docTypeSelector.show()
                    } else {
                        openCardLookup(((Instance) getItem()).getMetaClass().name)
                    }
                },
                getCaption: {
                    return ''
                }
        ]))
        final Table cardAttachmentsTable = getComponent('cardAttachmentsFrame.attachmentsTable')

        primaryTask.addAction(new ActionAdapter(ActionsField.LOOKUP, [
                actionPerform: {
                    openLookup(((Instance) getItem()).getMetaClass().name + ".browse", new Window.Lookup.Handler() {
                        @Override
                        void handleLookup(Collection items) {
                            if (items && items.size() > 0) {
                                TaskRequest task = getItem()
                                task.primaryTask = (TaskRequest) items.iterator().next()
                            }
                        }

                    },
                            WindowManager.OpenType.THIS_TAB,
                            ["exclItem": getItem(), "SelectPrimaryTask": true])
                },
                getCaption: {
                    return ''
                }
        ]))

        attachmentsDs.addListener(new CollectionDsListenerAdapter() {
            @Override
            void valueChanged(Object source, String property, Object prevValue, Object value) {

            }

            @Override
            void itemChanged(Datasource ds, Entity prevItem, Entity item) {
                super.itemChanged(ds, prevItem, item)
                cardAttachmentsTable.getActions().each {action ->
                    if (task.state != ',Finished,' && action.id != 'actions.Create')// && action.id != 'actions.Copy')
                        action.enabled = item instanceof CardAttachment
                }
            }

            @Override
            void collectionChanged(CollectionDatasource ds, CollectionDatasourceListener.Operation operation) {
                super.collectionChanged(ds, operation)
                for (Tabsheet.Tab tab: ((Tabsheet) getComponent("tabsheet")).getTabs()) {
                    if (tab.getName() != null && tab.getName().equals("attachmentsTab")) {
                        if (ds.getItemIds().size() > 0)
                            tab.setCaption(getMessage("attachments") + " (" + ds.getItemIds().size() + ")");
                        else
                            tab.setCaption(getMessage("attachments"));
                        return
                    }
                }
            }


        })

        getDsContext().addListener(new DsContext.CommitListener() {
            @Override
            void beforeCommit(CommitContext<Entity> context) {
                if (NumeratorType.ON_COMMIT == numeratorType) {
                    TaskRequest t = context.getCommitInstances().find { it == task }
                    if (t && StringUtils.isEmpty(t.num)) {
                        NumerationService ns = ServiceLocator.lookup(NumerationService.NAME)
                        t.setNum(ns.getNextNumber(task))
                    }
                }

                CardSecurityFrame securityFrame = getComponent("securityFrame")
                if (securityFrame) {
                    context.getCommitInstances().addAll(securityFrame.getToCommit())
                }

                if (remindersToCommit) {
                    context.getCommitInstances().addAll(remindersToCommit)
                }
            }

            @Override
            void afterCommit(CommitContext<Entity> context, Set<Entity> result) {
                Set<Entity> list = new HashSet<Entity>();
                for (Entity entity: result) {
                    if (entity instanceof CardRole) {
                        list.add(entity);
                    }
                }
                CardRolesFrame frame = (CardRolesFrame) getComponent("cardRolesFrame");
                if (frame != null) {
                    frame.getActorActionsFieldsMap().clear();
                    CollectionDatasourceImpl tmpCardRolesDs = frame.getDsContext().get("tmpCardRolesDs");
                    if (tmpCardRolesDs != null)
                        tmpCardRolesDs.committed(list);
                    if (cardRolesDs != null)
                        ((CollectionPropertyDatasourceImpl) cardRolesDs).committed(list);
                }
                for (Entity entity: context) {
                    if (entity instanceof TaskRequest &&
                            (",Finished,".equals(((TaskRequest) entity).getState()) || ",FinishedByInitiator,".equals(((TaskRequest) entity).getState()))) {
                        createTaskButton.getAction("createDependentTask").setVisible(false);
                    }
                }
            }

        })

        addListener(new com.haulmont.cuba.gui.components.Window.CloseListener() {
            @Override
            void windowClosed(String actionId) {
                User executorUser = task.getExecutor()
                if (isParentCardAccess() && executorUser != null && task.getJbpmProcessId() != null) {
                    TaskmanService taskmanService = ServiceLocator.lookup(TaskmanService.NAME)
                    def cardRoleList = task.getRoles()
                    taskmanService.setTaskRolesAccessToParentCard(task, cardRoleList)
                }
                if (com.haulmont.cuba.gui.components.Window.COMMIT_ACTION_ID.equals(actionId)) {
                    ((DocflowAppWindow) App.getInstance().getAppWindow()).reloadAppFolders([getItem()])
                }
            }

        })


        if (durationTextField != null &&
                StringUtils.isEmpty(
                        (String) (durationTextField).getValue()
                ) &&
                BooleanUtils.isFalse
                        (
                                durationTextField.isEditable()
                        )
        ) {
            ((LookupField) getComponent("timeUnit")).setVisible(false);
        }

        initLazyTabs();

        CollectionDatasource cardProjectsDs = getDsContext().get("cardProjectsDs");
        cardProjectsDs.addListener(new CollectionDsListenerAdapter() {
            @Override
            void collectionChanged(ds, operation) {
                Tabsheet tabsheet = getComponent("tabsheet");
                for (Tabsheet.Tab tab: tabsheet.getTabs()) {
                    if ("cardProjectsTab".equals(tab.getName())) {
                        if (cardProjectsDs.size() > 0)
                            tab.setCaption(getMessage("cardProjectsTab") + " (" + cardProjectsDs.size() + ")");
                        else
                            tab.setCaption(getMessage("cardProjectsTab"));
                    }
                }
            }

            @Override
            void valueChanged(Object source, String property, Object prevValue, Object value) {

            }

        });
        isTabComment = params.get("isTabComment");
        CollectionDatasourceImpl cardRelationsDs = getDsContext().get("cardRelationsDs");
        if (cardRelationsDs != null) {
            cardRelationsDs.addListener(new CollectionDsListenerAdapter() {
                @Override
                void valueChanged(Object source, String property, Object prevValue, Object value) {

                }

                @Override
                void collectionChanged(CollectionDatasource ds, CollectionDatasourceListener.Operation operation) {
                    for (Tabsheet.Tab tab: ((Tabsheet) getComponent("tabsheet")).getTabs()) {
                        if (tab.getName() != null && tab.getName().equals("cardRelationsTab")) {
                            if (ds.getItemIds().size() > 0)
                                tab.setCaption(getMessage("cardRelationsTab") + " (" + ds.getItemIds().size() + ")");
                            else
                                tab.setCaption(getMessage("cardRelationsTab"));
                            return
                        }
                    }
                }
            })
        }

        initRemiderButton()

        if (getComponent("confirmRequiredLabel") != null) {
            com.vaadin.ui.Label vLabel = (com.vaadin.ui.Label) WebComponentsHelper.unwrap(getComponent("confirmRequiredLabel"));
            vLabel.setContentMode(com.vaadin.ui.Label.CONTENT_XHTML);
        }
        initCardProjectFrame();
    }

    protected void initCardProjectFrame(){
        CardProjectsFrame cardProjectsFrame = getComponent("cardProjectsFrame")
        cardProjectsFrame.init()
    }

    def choiceSetEditableParentCardAccess() {
        if (UserSessionProvider.userSession.isPermitted(PermissionType.ENTITY_OP, 'ext$TaskRequest:create')) {
            if (!isParentCardAccess() || !parentCardAccessEditable) {
                setEditableParentCardAccess(true)
                parentCardAccess?.value = false
                parentCardAccess?.setEditable(isParentCardFieldEmpty())
                boolean isEditable = isParentCardFieldEmpty()
                setEditableParentCardAccess(isEditable)
            } else {
                setEditableParentCardAccess(true)
                parentCardAccess?.value = false
                setEditableParentCardAccess(false)
            }
            adjustForTaskType(task);
        }
    }

    def setEditableParentCardAccess(boolean isEditable) {
        parentCardAccess?.setEditable(isEditable)
        refreshParentCardVisible();
    }

    boolean isParentCardFieldEmpty() {
        boolean isEditable = false
        if (task?.parentCard != null) {
            isEditable = true
        }
        return isEditable
    }

    boolean isParentCardAccess() {
        boolean access = false
        def valueAccess = parentCardAccess?.getValue()
        if (valueAccess != null) {
            access = valueAccess
        }
        return access
    }

    def reloadCurrentTask(Card currentCard) {
        return getDsContext().getDataService().reload(currentCard, EDIT_VIEW_ID)
    }

    def initParentCardAccess() {
        if (parentCardAccess == null) {
            parentCardAccess = getComponent(PARENT_CARD_ACCESS_FIELD_ID)
        }
        if (!PersistenceHelper.isNew(task)) {
            task = reloadCurrentTask(task)
        }
        def checkAccess = isParentCardAccess()
        def editable = false
        if (task.getParentCard() != null && !checkAccess) {
            editable = true
        }
        parentCardAccessEditable = editable
        parentCardAccess.setEditable(editable)
    }

//    def setParentCardVisible() {
    //        Boolean visible = getParentCardVisibleProperty();
    //        Label parentCardAccessLabel = getComponent("parentCardAccessLabel");
    //        CheckBox parentCardAccess = getComponent("parentCardAccess");
    //        if (visible)
    //            refreshParentCardVisible();
    //        else {
    //            parentCardAccess.setVisible(visible);
    //            parentCardAccessLabel.setVisible(visible);
    //        }
    //
    //    }

    def refreshParentCardVisible() {
        WebLabel parentCardAccessLabel = getComponent("parentCardAccessLabel");
        Boolean visible = getParentCardVisibleProperty();
        if (!visible) {
            parentCardAccess.setVisible(visible);
            parentCardAccessLabel.setVisible(visible);
        }
        else if (!cardIsEditable) {
            parentCardAccess.setVisible(false);
            if (parentCardAccess.getValue()) {
                parentCardAccessLabel.setStyleName("v-label-notice");
            }
            else parentCardAccessLabel.setVisible(false);
        }
        else {
            Boolean parentCardAccessVisible = null;
            List<FieldInfo> fieldInfos;
            if (task.getTaskType() != null) fieldInfos = task.getTaskType().getFields();
            if (fieldInfos != null)
                for (FieldInfo fieldInfo: fieldInfos) {
                    if ("parentCardAccess".equals(fieldInfo.getName())) {
                        parentCardAccessVisible = fieldInfo.getVisible();
                        break;
                    }
                }
            if (parentCardAccessVisible) {
                parentCardAccess.setVisible(isParentCardFieldEmpty());
                parentCardAccessLabel.setVisible(isParentCardFieldEmpty());
            } else {
                parentCardAccess.setVisible(false);
                parentCardAccessLabel.setVisible(false);
            }
        }
    }

    protected Boolean getParentCardVisibleProperty() {
        String value = AppContext.getProperty("thesis.parentCardVisible");
        return BooleanUtils.isTrue("true".equals(value));
    }

    def setReminderCaption() {
        int c
        if (PersistenceHelper.isNew(task)) {
            c = remindersToCommit.size()
        } else {
            CardService cardService = ServiceLocator.lookup(CardService.NAME)
            c = cardService.getCardRemindersCount(task)
        }

        reminderButton.caption = c > 0 ? '(' + c + ')' : '';
    }

    def initRemiderButton() {
        reminderButton = getComponent("reminderButton")
        finishDateTimePlanLabel = getComponent("finishDateTimePlanLabel");
        finishDateTimePlan = getComponent("finishDateTimePlan");
        reminderButton.setAction(new ActionAdapter("remiderButton", [
                actionPerform: {
                    App.getInstance().getWindowManager().getDialogParams().setWidth(500)
                    ReminderBrowser window = openWindow("tm\$Reminder.browse", WindowManager.OpenType.DIALOG,
                            ['card': getItem(), 'finishDate': ((TaskRequest) getItem()).finishDateTimePlan,
                                    'user': UserSessionClient.getUserSession().getCurrentOrSubstitutedUser(), 'toCommit': remindersToCommit])

                    window.addListener(new com.haulmont.cuba.gui.components.Window.CloseListener() {
                        @Override
                        void windowClosed(String actionId) {
                            remindersToCommit = window.getCommitInstances();
                            setReminderCaption()
                        }

                    })
                },

                getCaption: {
                    return "";
                }
        ]))
        setReminderCaption();
        WebComponentsHelper.unwrap(reminderButton).addStyleName('field')
    }

    protected List getFieldsToCopy() {
        return ["fullDescr", "description", "finishDateTimePlan", "duration", "taskName",
                "priority", "taskType", "refuseEnabled", "reassignEnabled", "controlEnabled",
                "timeUnit", "confirmRequired"]
    }

    protected void fillTask(TaskPattern taskPattern) {
        LoadContext ctx = new LoadContext(MetadataProvider.getReplacedClass(TaskPattern.class)).setId(taskPattern.id).setView('cloning')
        taskPattern = (TaskPattern) ServiceLocator.getDataService().load(ctx)
        def fieldsToCopy = getFieldsToCopy()
        fieldsToCopy.each {fieldName ->
            if ((fieldName == 'duration') && (taskPattern['finishDateTimePlan'] != null)) {
                return
            }
            if (StringUtils.isBlank((String) taskPattern[fieldName])) return;
            Component component = getComponent("$fieldName")
            if (fieldName == 'percentCompletion') {
                percentCompletionSlider.setValue(taskPattern["$fieldName"] ? ((Integer) taskPattern["$fieldName"]).doubleValue() : 0D)
            } else if (component) component.value = taskPattern["$fieldName"]
        }
        if (!isYouTask)
            fillRolesFromPattern(taskPattern)

        CollectionDatasource attachmentsDs = getDsContext().get('attachmentsDs')
        taskPattern.attachments.each {CardAttachment patternAttachment ->
            CardAttachment clonedAttachment = new CardAttachment(
                    createTs: TimeProvider.currentTimestamp(),
                    createdBy: UserSessionProvider.userSession.currentOrSubstitutedUser.loginLowerCase,
                    name: patternAttachment.name,
                    file: patternAttachment.file,
                    attachType: patternAttachment.attachType,
                    card: task
            )
            addedAttachment.add(clonedAttachment)
        }

        if (taskPattern.getProjects() != null && taskPattern.getProjects().size() > 0) {
            task.setProjects(taskPattern.getProjects());

            CollectionPropertyDatasourceImpl cardProjectsDs = getDsContext().get('cardProjectsDs')
            taskPattern.projects.each {project ->
                cardProjectsDs.addItem(project)
            }
        }

        if (task.duration) {
            WorkCalendarService service = ServiceLocator.lookup(WorkCalendarService.NAME)
            Date finishDateTimePlan = service.addInterval(task.createDateTime, (Integer) task.duration, task.timeUnit)
            task.finishDateTimePlan = finishDateTimePlan
        }

        fillCategoryAttrsFromPattern(taskPattern)
        cardDs.setItem(task)
        if (task.attachments)
            addedAttachment.addAll(task.attachments)
        task.attachments = addedAttachment
        initAttachments(task)
    }

    protected void setInitiator() {
//Current user will become an initiator only if he has initiator security role or initiator hasn't been set up yet
        User user = UserSessionClient.getUserSession().getCurrentOrSubstitutedUser()
        if (user.userRoles.find {userRole -> userRole.role.name == TaskRequest.INITIATOR_SEC_ROLE}
                && !task.roles.find {role -> role.code == TaskRequest.INITIATOR_ROLE}) {
            ProcRole initiatorProcRole = task.proc.roles.find {it.code == TaskRequest.INITIATOR_ROLE}
            cardRolesFrame.setProcActor(task.proc, initiatorProcRole, user, true)
        }
    }

    protected void fillRolesFromPattern(AbstractTask taskPattern) {
        if (taskPattern.roles != null && taskPattern.roles.size() > 0) {
            //cardRolesFrame.deleteAllActors()
            taskPattern.roles.each {role ->
                if (role.user == null || role.user.active)
                    cardRolesFrame.addProcActor(taskPattern.proc, role.code, role.user, role.getNotifyByEmail(), role.getNotifyByCardInfo())
            }
            com.vaadin.ui.Table table = (com.vaadin.ui.Table) WebComponentsHelper.unwrap(cardRolesFrame.getComponent("rolesTable"));
            table.setEditable(table.isEditable());
        }
    }

    protected void fillCategoryAttrsFromPattern(TaskPattern taskPattern) {
//      LoadContext ctx = new LoadContext(CategoryAttributeValue.class).setView("categoryAttributeValue")
        //      def query = ctx.setQueryString('select c from sys$CategoryAttributeValue c where c.entityId = :entityId')
        //      query.addParameter('entityId', taskPattern.id)
        //      List<CategoryAttributeValue> categoryAttributesValues = ServiceLocator.getDataService().loadList(ctx)
        ThesisService thesisService = ServiceLocator.lookup(ThesisService.NAME)
        List<CategoryAttributeValue> categoryAttributesValues = thesisService.getCategoryAttributeValues(taskPattern.getId())
        def runtimePropsDs = getDsContext().get('runtimePropsDs')
        RuntimePropertiesEntity rDsItem = runtimePropsDs.getItem()
        categoryAttributesValues.each {CategoryAttributeValue attrValue ->
            String dataType = attrValue.getCategoryAttribute().getDataType();
            RuntimePropsDatasource.PropertyType propertyType = attrValue.getCategoryAttribute().getIsEntity() ? null :
                RuntimePropsDatasource.PropertyType.valueOf(dataType);
            def value = RuntimePropsDatasource.PropertyType.ENUMERATION.equals(propertyType) ? new SetValueEntity(attrValue.getStringValue()) : attrValue.getValue()
            if (value instanceof UUID)
                value = thesisService.findEntity(attrValue.categoryAttribute?.dataType, value);
            rDsItem.setValue(attrValue.categoryAttribute.name, value)
        }
    }

    protected void setTaskParamEnabled(boolean key) {

       /* TextField taskName = getComponent("taskName"),
                  fullDescr = getComponent("fullDescr"),
                  number = getComponent("number");
        taskName.setEditable(key);
        fullDescr.setEditable(key);
        number.setEditable(key);

        DateField finishDateTimePlan = getComponent("finishDateTimePlan");
        finishDateTimePlan.setEditable(key);

        WebHBoxLayout finishDateTimeFactBox = getComponent("finishDateTimeFactBox");
        finishDateTimeFactBox.setEnabled(key);

        LookupField priority = getComponent("priority");
        priority.setEditable(key)

        ActionsField parentCard = getComponent("parentCard"),
                     extClient = getComponent("extClient");
        parentCard.setEditable(key);
        extClient.setEditable(key);

        CheckBox refuseEnabled = getComponent("refuseEnabled"),
                 reassignEnabled = getComponent("reassignEnabled"),
                 confirmRequired = getComponent("confirmRequired");
        reassignEnabled.setEditable(key);
        refuseEnabled.setEditable(key);
        confirmRequired.setEditable(key);

        WebVBoxLayout percentCompletion = getComponent("percentCompletion");
        percentCompletion.setEnabled(key);     */

        ActionsField extClient = getComponent("extClient")
        extClient.setEditable(key)

        TextField fullDescr = getComponent("fullDescr"),
                  number = getComponent("number");
        fullDescr.setEditable(!key);
        number.setEditable(key);
    }

    public void setItem(Entity item) {
        super.setItem(item);
        initParentCardAccess()
        task = (TaskRequest) getItem()

        if (task.getState() == ",Finished,") {
            setTaskParamEnabled(false)
        }
        else {
            TextField fullDescr = getComponent("fullDescr");
            fullDescr.setEditable(true);
        }

        if (createTaskButton) {
            if (PersistenceHelper.isNew(task))
                createTaskButton.visible = false
            else {
                createTaskButton.addAction(new CreateSubCardAction('createSubCard', this));
                createTaskButton.addAction(new CreateDependentTaskAction('createDependentTask', this));

                createTaskButton.addAction(new ActionAdapter('createFromCurrent', [
                        actionPerform: {
                            TaskRequest currentTask = (TaskRequest) getItem()
                            if (currentTask) {
                                if (currentTask.attachments != null && !currentTask.attachments.isEmpty()) {
                                    this.showOptionDialog(
                                            getMessage('taskCopyAttachments.dialogHeader'),
                                            getMessage('taskCopyAttachments.dialogMessage'),
                                            IFrame.MessageType.CONFIRMATION,
                                            [
                                                    new DialogAction(DialogAction.Type.YES) {
                                                        @Override
                                                        public void actionPerform(Component component) {
                                                            cloneTask(currentTask, true)
                                                        }
                                                    },
                                                    new DialogAction(DialogAction.Type.NO) {
                                                        @Override
                                                        public void actionPerform(Component component) {
                                                            cloneTask(currentTask, false)
                                                        }
                                                    }
                                            ]);
                                } else {
                                    cloneTask(currentTask, false)
                                }

                            } else {
                                showNotification(getMessage("selectTask.msg"), IFrame.NotificationType.HUMANIZED)
                            }
                        },
                        getCaption: {
                            return getMessage("createFromCurrent")
                        }
                ]))
            }
        }

        if (DocflowHelper.createSubCardIsPermitted()) {
            UserSession userSession = UserSessionClient.getUserSession();
            if (!userSession.isPermitted(PermissionType.ENTITY_OP, 'ext$TaskRequest:create')) {
                if (createFromPatternButton)
                    createFromPatternButton.visible = false
                if (createTaskButton) {
                    createTaskButton.getAction('createFromCurrent').visible = false
                    createTaskButton.getAction('createDependentTask').visible = false
                }
            }

            if (createTaskButton && ",Finished,".equals(task.getState()) || ",FinishedByInitiator,".equals(task.getState())) {
                createTaskButton.getAction("createDependentTask").setVisible(false);
            }
        } else
            createTaskButton.visible = false

        initAttachments(task);
        if (isTabComment){
            selectCommentTab();
        }
        caption = MessageProvider.formatMessage(getClass(), "taskEditor.caption", (task.num != null) ? task.num : "");
        ActionsFrame actionsFrame = getComponent("actionsFrame")
        if (task != null && task.percentCompletion != null) {
            percentCompletionSlider.setValue(task.percentCompletion)
        } else {
            percentCompletionSlider.setValue(0)
        }

        if (!task.proc) {
            TaskmanService service = ServiceLocator.lookup(TaskmanService.NAME)
            LoadContext ctx = new LoadContext(Proc.class);
            Proc defaultProc = service.getDefaultProc();
            ctx.setId(defaultProc.getUuid());
            ctx.setView("task-edit");
            task.proc = ServiceLocator.getDataService().load(ctx)
        }

        if (PersistenceHelper.isNew(task)) {
//      TaskmanService taskmanService = ServiceLocator.lookup(TaskmanService.NAME);
            //      getComponent("num").value = taskmanService.getNextTaskNum();
            task.createDateTime = TimeProvider.currentTimestamp();
            task.creator = UserSessionClient.userSession.user
            task.substitutedCreator = UserSessionClient.userSession.currentOrSubstitutedUser
            task.state = ',New,'
            TmConfig config = ConfigProvider.getConfig(TmConfig.class)
            if (!task.priority && StringUtils.isNotBlank(config.getPriorityDefault())) {
                CollectionDatasource prioritiesDs = (CollectionDatasource) getDsContext().get("prioritiesDs");
                prioritiesDs.refresh();
                task.priority = prioritiesDs.getItem(UUID.fromString(config.getPriorityDefault()))
            }
            if (!task.taskType && StringUtils.isNotBlank(config.getTaskTypeDefault())) {
                CollectionDatasource taskTypesDs = (CollectionDatasource) getDsContext().get("taskTypesDs");
                taskTypesDs.refresh();
                task.taskType = taskTypesDs.getItem(UUID.fromString(config.getTaskTypeDefault()))
            }

            //If we create task from pattern or another task, card roles aren't persisted. We need to set them as modified manually
            task.roles.each {CardRole cardRole ->
                cardRolesDs.modifyItem(cardRole)
            }
            //If we create task from pattern attachments aren't persisted
            task.attachments.each {CardAttachment attachment ->
                attachmentsDs.modifyItem(attachment)
            }

            //if we create task from pattern with filled duration field we must calculate finishDateTime
            if (task.duration) {
                WorkCalendarService service = ServiceLocator.lookup(WorkCalendarService.NAME)
                Date finishDateTimePlan = service.addInterval(task.createDateTime, (Integer) task.duration, task.timeUnit)
                task.finishDateTimePlan = finishDateTimePlan
            }

            if (task.proc && actionsFrame)
                actionsFrame.initActions(item, isCommentVisible())

            //if create as copy
            if (!taskPattern)
                initDefaultFields()
            else if (taskPattern instanceof TaskPattern) {
                fillCategoryAttrsFromPattern(taskPattern)
                ((RuntimePropsDatasourceImpl) getDsContext().get('runtimePropsDs')).valid();
            }

//      TextField numField = getComponent("num")
            if (numeratorType == NumeratorType.ON_CREATE) {
                if (StringUtils.isEmpty(task.num)) {
                    NumerationService ns = ServiceLocator.lookup(NumerationService.NAME)
                    task.setNum(ns.getNextNumber(task))
                }
                caption = MessageProvider.formatMessage(getClass(), "taskEditor.caption", task.getNum());
            }

            if (task.parentCard) {
                ((ActionsField) getComponent('parentCard')).setEditable(false)
            }

            cardRolesFrame.procChanged(task.getProc())

            if (isYouTask != null && isYouTask) {
                task.setReassignEnabled(true);
                CollectionDatasource tmpCardRolesDs = cardRolesFrame.getDsContext().get("tmpCardRolesDs");
                CollectionDatasource procRolesDs = cardRolesFrame.getDsContext().get("procRolesDs");
                for (UUID uuid: new ArrayList<UUID>(tmpCardRolesDs.getItemIds())) {
                    tmpCardRolesDs.removeItem(tmpCardRolesDs.getItem(uuid));
                }
                for (UUID uuid: procRolesDs.getItemIds()) {
                    ProcRole procRole = procRolesDs.getItem(uuid);
                    if (TaskRequest.INITIATOR_ROLE.equals(procRole.getCode()) || TaskRequest.EXECUTOR_ROLE.equals(procRole.getCode())) {
                        CardRole cr = new CardRole();
                        cr.setProcRole(procRole);
                        cr.setCode(procRole.getCode());
                        cr.setUser(UserSessionClient.getUserSession().getCurrentOrSubstitutedUser());
                        cr.setCard(task);
                        tmpCardRolesDs.addItem(cr);
                    }
                }
            }

            if (task.primaryTask) {
                primaryTask.setEditable(false);
            }
            TemplateSettingHelper templateHelper = TemplateSettingHelper.getInstance();
            UUID templateId = templateHelper.getSettingUuid("template.ext\$TaskRequest");
            TaskPattern pattern = (TaskPattern) templateHelper.loadTemplateEntity("template.ext\$TaskRequest", templateId);
            TaskmanService taskmanService = ServiceLocator.lookup(TaskmanService.NAME);
            if (!taskPattern && pattern) {
                getDsContext().get("taskTypesDs").refresh();
                fillTask(pattern);
            }
        } else {
            setNullSelectionAllowedToFalse();
            if (task.proc) {
                cardRolesFrame.procChanged(task.proc)
                if (fromReminder)
                    cardRolesFrame.initDefaultActors(task.proc)
            }
            CardService cardService = ServiceLocator.lookup(CardService.NAME);
            Integer count = cardService.getCardRelationsCount(task);
            if (count > 0) {
                for (Tabsheet.Tab tab: ((Tabsheet) getComponent("tabsheet")).getTabs()) {
                    if (tab.getName() != null && tab.getName().equals("cardRelationsTab")) {
                        tab.setCaption(getMessage("cardRelationsTab") + " (" + count + ")");
                    }
                }
            }
            if (task.proc && actionsFrame && UserSessionProvider.userSession.currentOrSubstitutedUser.equals(task.initiator) &&
                    WfUtils.isCardInStateList(task, 'Assigned', 'InWork', 'InControl')) {
                TextField descr = actionsFrame.getComponent("descrText")
                descr.visible = false
            }
        }

        TaskmanService taskmanService = ServiceLocator.lookup(TaskmanService.NAME);
        if (task.primaryTask) {
            if (!taskmanService.isCurrentUserAdministrator()
                    && !taskmanService.isCurrentUserInProcRole(task, TaskRequest.INITIATOR_ROLE))
                finishDateTimePlanDateField.setEnabled(false)

            Button button = getButtonFromFrame('actionsFrame', WfConstants.ACTION_START)
            if (button != null) button.setVisible(false)
        }

        Button reassignByInitiatorButton
        if (actionsFrame && (taskmanService.isCurrentUserInProcRole(task, TaskRequest.INITIATOR_ROLE)
                && taskmanService.isCurrentUserInProcRole(task, TaskRequest.EXECUTOR_ROLE) && !task.getReassignEnabled() ||
                taskmanService.isCurrentUserInProcRole(task, TaskRequest.INITIATOR_ROLE)
                && !taskmanService.isCurrentUserInProcRole(task, TaskRequest.EXECUTOR_ROLE)
                || isUserDepartmentChiefAndNotExecutor()
                && taskmanService.isExecutorBelongsDepartment(task))
                && (WfUtils.isCardInStateList(task, 'InWork', 'Assigned'))) {
            reassignByInitiatorButton = new WebButton()
            reassignByInitiatorButton.setWidth('100%')
//      reassignByInitiatorButton.setAction(new ProcessAction(task, "${task.state}.ReassignByInitiator", actionsFrame))

            reassignByInitiatorButton.setAction(new AbstractAction('reassignByExecutor') {
                void actionPerform(Component component) {
                    if (!commit()) return;
                    User prevUser = task.executor
                    LoadContext ctx = new LoadContext(Assignment.class)
                    LoadContext.Query query = ctx.setQueryString('select a from wf$Assignment a where a.card.id = :card and a.finished is null')
                    query.addParameter('card', task)
                    Assignment assignment = ServiceLocator.getDataService().load(ctx)
                    Map windowParams = ['param$card': task, 'roleCode': '20-Executor', 'secRole': 'task_executor',
                            'assignmentId': (assignment ? assignment.id : null), 'reassignedByInitiator': 'true']
                    ReassignForm reassignForm = openWindow('reassignExecutor.form', WindowManager.OpenType.DIALOG, windowParams)
                    reassignForm.addListener(new ReassignByExecutorCloseListener(assignment, prevUser, reassignForm));
                }

                def String getCaption() {
                    return MessageProvider.getMessage(task.proc.messagesPack, "${task.state[1..-2]}.ReassignByInitiator");
                }


            })
            actionsFrame.addButton(reassignByInitiatorButton)
        }

        if (actionsFrame && taskmanService.isCurrentUserInProcRole(task, TaskRequest.EXECUTOR_ROLE)
                && (WfUtils.isCardInStateList(task, 'InWork', 'Assigned'))) {
            Map<String, Object> processVariables = wfService.getProcessVariables(task)

            PopupButton toInitiatorButton = new WebPopupButton();
            toInitiatorButton.width = '100%'
            toInitiatorButton.setStyleName("wf-failure")
//      toInitiatorButton.menuWidth = '200px'
            toInitiatorButton.setCaption(MessageProvider.getMessage(task.proc.messagesPack, 'ReturnToInitiator'))

            toInitiatorButton.addAction(new ProcessAction(task, task.state[1..-2] + '.NotCompleted', actionsFrame) {
                def void actionPerform(Component component) {
                    String commentTemplate = MessageProvider.getMessage(task.proc.messagesPack, 'RequestPostponementComment')
                    processVariables.put("commentTemplate", commentTemplate)
                    setProcessVariables(task, processVariables)
                    super.actionPerform(component);
                }

                def String getCaption() {
                    return MessageProvider.getMessage(task.proc.messagesPack, 'RequestPostponement')
                }
            })

            toInitiatorButton.addAction(new ProcessAction(task, task.state[1..-2] + '.NotCompleted', actionsFrame) {
                def void actionPerform(Component component) {
                    String commentTemplate = MessageProvider.getMessage(task.proc.messagesPack, 'RequestPreciseComment')
                    processVariables.put("commentTemplate", commentTemplate)
                    setProcessVariables(task, processVariables)
                    super.actionPerform(component);
                }

                def String getCaption() {
                    return MessageProvider.getMessage(task.proc.messagesPack, 'RequestPrecise')
                }
            })

            TaskAccessData tad = getContext().getParamValue("accessData");
            if (task.refuseEnabled && tad.isRefuse()) {
                toInitiatorButton.addAction(new ProcessAction(task, task.state[1..-2] + '.Refuse', actionsFrame))
            }
            boolean isYouTask = taskmanService.isYouTask(task, UserSessionClient.getUserSession().getCurrentOrSubstitutedUser());
            if (reassignByInitiatorButton != null)
                reassignByInitiatorButton.setVisible(!isYouTask)
            toInitiatorButton.setVisible(!isYouTask);
            //actionsFrame.addButton(toInitiatorButton)
        }

        if (actionsFrame && taskmanService.isCurrentUserInProcRole(task, TaskRequest.OBSERVER_ROLE)
                && !(WfUtils.isCardInStateList(task, 'Finished', 'FinishedByInitiator', 'Canceled'))) {
            PopupButton stopObservingButton = new WebPopupButton();
            stopObservingButton.width = '100%'
            stopObservingButton.setCaption(getMessage('stopObserving'))
            stopObservingButton.addAction(new ActionAdapter('deleteFromObservers', [
                    actionPerform: {
                        showOptionDialog(
                                MessageProvider.getMessage(getClass(), "stopObserving.title"),
                                MessageProvider.getMessage(getClass(), "deleteFromObservers.message"),
                                IFrame.MessageType.CONFIRMATION,
                                [
                                        new DialogAction(DialogAction.Type.YES) {
                                            @Override
                                            public void actionPerform(Component component) {
                                                cardRolesFrame.removeProcActor(TaskRequest.OBSERVER_ROLE, UserSessionClient.getUserSession().getCurrentOrSubstitutedUser())
                                                if (commit())
                                                    close(COMMIT_ACTION_ID, true)
                                            }
                                        },
                                        new DialogAction(DialogAction.Type.NO)
                                ]
                        );
                    },
                    getCaption: {
                        return MessageProvider.getMessage(getClass(), 'deleteFromObservers')
                    }
            ]))

            stopObservingButton.addAction(new ActionAdapter('removeNotifications', [
                    actionPerform: {

                        def removeObserversNotifications = {
                            CollectionDatasource tmpCardRolesDs = cardRolesFrame.getDsContext().get('tmpCardRolesDs')
                            if (tmpCardRolesDs) {
                                User currentUser = UserSessionClient.getUserSession().getCurrentOrSubstitutedUser()
                                CardRole observerCardRole = null
                                tmpCardRolesDs.getItemIds().each {id ->
                                    CardRole cr = tmpCardRolesDs.getItem(id)
                                    if ((cr.code == TaskRequest.OBSERVER_ROLE) && (currentUser.equals(cr.user))) {
                                        observerCardRole = cr
                                        return
                                    }
                                }

                                if (observerCardRole) {
                                    observerCardRole.notifyByEmail = null
                                    observerCardRole.notifyByCardInfo = null
                                }
                            }
                        }

                        showOptionDialog(
                                MessageProvider.getMessage(getClass(), "stopObserving.title"),
                                MessageProvider.getMessage(getClass(), "removeNotifications.message"),
                                IFrame.MessageType.CONFIRMATION,
                                [
                                        new DialogAction(DialogAction.Type.YES) {
                                            @Override
                                            public void actionPerform(Component component) {
                                                removeObserversNotifications()
                                                if (commit())
                                                    close(COMMIT_ACTION_ID, true)
                                            }
                                        },
                                        new DialogAction(DialogAction.Type.NO)
                                ]
                        );
                    },
                    getCaption: {
                        return MessageProvider.getMessage(getClass(), 'removeNotifications')
                    }
            ]))

            actionsFrame.addButton(stopObservingButton)
        }

        //calculate finishDateTime when duration changes and clear duration when finishDateTime changes
        cardDs.addListener(new DsListenerAdapter<Card>() {
            @Override
            void valueChanged(Card source, String property, Object prevValue, Object value) {
                if ((property == 'duration') || (property == 'timeUnit')) {
                    TaskRequest task = (TaskRequest) getItem()
                    if (task.duration && (!task.primaryTask || taskmanService.isCurrentUserAdministrator()
                            || taskmanService.isCurrentUserInProcRole(task, TaskRequest.INITIATOR_ROLE))) {
                        Date createDateTime = task.createDateTime
                        Integer duration = task.duration
                        TimeUnit timeUnit = task.timeUnit

                        if (!createDateTime) {
                            showNotification(getMessage("WorkCalendar.fillCreateDate"), IFrame.NotificationType.HUMANIZED)
                            return
                        } else if (!timeUnit) {
                            showNotification(getMessage("WorkCalendar.fillTimeUnit"), IFrame.NotificationType.HUMANIZED)
                            return
                        }

                        WorkCalendarService service = ServiceLocator.lookup(WorkCalendarService.NAME)
                        Date finishDateTimePlan = service.addInterval(createDateTime, duration, timeUnit)
                        isAutoFinishDate = true
                        task.finishDateTimePlan = finishDateTimePlan
                    }
                    if (task.getPrimaryTask() && !WfUtils.isCardInState(task.getPrimaryTask(), "Finished")) {
                        finishDateTimePlan.setValue(null);
                    }
                } else if (property == 'finishDateTimePlan') {
                    if (value != null && value.getHours() == 0 && value.getMinutes() == 0 && value.getSeconds() == 0) {
                        def currTime = TimeProvider.currentTimestamp();
                        def dateH = DateUtils.setHours(value, currTime.getHours())
                        def dateM = DateUtils.setMinutes(dateH, currTime.getMinutes())
                        finishDateTimePlanDateField.setValue(dateM)
                    }
                    if (isAutoFinishDate) {
                        isAutoFinishDate = false
                        return
                    }
                    if (value != null) {
                        durationTextField.value = null
                    }
                } else if (property == 'primaryTask') {
                    Button button = getButtonFromFrame('actionsFrame', WfConstants.ACTION_START)
                    if (value != null) {
                        finishDateTimePlanDateField.setEnabled(false)
                        finishDateTimePlanDateField.setValue(null)
                        if (button != null) button.setVisible(false)
                    }
                    else {
                        finishDateTimePlanDateField.setEnabled(true)
                        if (button != null) button.setVisible(true)
                    }
                }
            }
        })

        adjustForTaskType(task)
        setComponentsEnabled()
        setComponentsVisibility()

        deleteNotifications(task)
        ((DocflowAppWindow) App.getInstance().getAppWindow()).reloadAppFolders([task]);

        PopupButton printButton = getComponent("print");

        printButton.addAction(new ActionAdapter('printTask', [
                actionPerform: {
                    if (getDsContext().isModified()) {
                        showOptionDialog(
                                getMessage("printUnsave.caption"),
                                getMessage("printUnsave.msg"),
                                IFrame.MessageType.CONFIRMATION,
                                [
                                        new DialogAction(DialogAction.Type.YES) {
                                            @Override
                                            public void actionPerform(Component component) {
                                                if (commit()) {
                                                    close(Window.COMMIT_ACTION_ID)
                                                    reopen(Collections.<String, Object> emptyMap())
                                                    ThesisPrintHelper.printTask(task)
                                                }
                                            }
                                        },
                                        new DialogAction(DialogAction.Type.NO)
                                ]
                        );
                    } else {
                        ThesisPrintHelper.printTask(task)
                    }
                },
                getCaption: {
                    return MessageProvider.getMessage(getClass(), 'printTask')
                }
        ]))

        printButton.addAction(new ActionAdapter('printExecutionList', [
                actionPerform: {
                    if (getDsContext().isModified()) {
                        showOptionDialog(
                                getMessage("printUnsave.caption"),
                                getMessage("printUnsave.msg"),
                                IFrame.MessageType.CONFIRMATION,
                                [
                                        new DialogAction(DialogAction.Type.YES) {
                                            @Override
                                            public void actionPerform(Component component) {
                                                if (commit()) {
                                                    close(Window.COMMIT_ACTION_ID)
                                                    reopen(Collections.<String, Object> emptyMap())
                                                    ThesisPrintHelper.printTaskExecutionList(task)
                                                }
                                            }
                                        },
                                        new DialogAction(DialogAction.Type.NO)
                                ]
                        );
                    } else {
                        ThesisPrintHelper.printTaskExecutionList(task)
                    }
                },
                getCaption: {
                    return MessageProvider.getMessage(getClass(), 'printExecutionList')
                }
        ]))

        RuntimePropertiesFrame runtimePropertiesFrame = getComponent("runtimePropertiesFrame");
        if (runtimePropertiesFrame != null) {
            runtimePropertiesFrame.setCategoryFieldVisible(false);
            if (!cardIsEditable)
                runtimePropertiesFrame.setCategoryFieldEditable(false);
        }
//        setParentCardVisible()
        refreshParentCardVisible();
        if (printButton) {
            ThesisPrintHelper.generateAction(printButton, "ext\$TaskRequest.edit",
                    new ThesisPrintHelper.ParamsHandler() {
                        @Override
                        public Map<String, Object> getParams() {
                            return Collections.<String, Object> singletonMap("entity", getItem());
                        }
                    });
        }
    }

    protected void selectCommentTab() {
        Tabsheet.Tab cardCommentTab = ((Tabsheet) getComponent("tabsheet")).getTab("cardCommentTab");
        if (cardCommentTab != null) {
            ((Tabsheet) getComponent("tabsheet")).setTab("cardCommentTab")
        }
    }

    @Override
    protected void initActionsFrame(Card card, ActionsFrame actionsFrame) {
        super.initActionsFrame(card, actionsFrame);
        TextField descrField = getComponent("actionsFrame.descrText");
        if (descrField != null) {
            String description = descrField.getValue();
            if (!((TaskRequest) card).getRefuseEnabled() && card.getProc() != null &&
                    description.equals(MessageUtils.loadString(card.getProc().getMessagesPack(), "msg://Assigned.description"))) {
                descrField.setEditable(true);
                descrField.setValue(MessageUtils.loadString(card.getProc().getMessagesPack(), "msg://DontRefuseEnabled"));
                descrField.setEditable(false);
            }
        }
    }

    protected def initRuntimePropertiesTab() {
        Tabsheet ts = getComponent("tabsheet");
        Tabsheet.Tab runtimePropertiesTab = ts.getTab("runtimePropertiesTab");
        if (accessData != null && accessData.getRuntimePropertiesInSeparateTab()) {
            if (runtimePropertiesTab != null) {
                if (task.category) {
                    View view = new View(com.haulmont.cuba.core.entity.Category.class).addProperty("categoryAttrs");
                    LoadContext ctx = new LoadContext(com.haulmont.cuba.core.entity.Category.class).setId(task.category.id).setView(view);
                    com.haulmont.cuba.core.entity.Category category = ServiceLocator.getDataService().load(ctx);
                    runtimePropertiesTab.setVisible(category.categoryAttrs && !category.categoryAttrs.empty);
                }
                else
                    runtimePropertiesTab.setVisible(false);
            }
        } else {
            if (runtimePropertiesTab != null)
                runtimePropertiesTab.setVisible(false);
        }
    }

    protected void setProcessVariables(Card card, Map<String, Object> variables) {
        try {
            wfService.setProcessVariables(task, variables);
        } catch (Exception e) {
            if (StringUtils.endsWith(e.getMessage(), "doesn't exist")) {
                String msg = MessageProvider.getMessage(AppContext.getProperty(AppConfig.MESSAGES_PACK_PROP), "assignmentAlreadyFinished.message");
                App.getInstance().getWindowManager().showNotification(msg, IFrame.NotificationType.ERROR);
            } else {
                throw e.getCause();
            }
        }
    }

    protected void adjustForTaskType(TaskRequest task) {
        accessData.refreshFieldInfos(task)
        def components = getComponents()
        components.each {Component c ->
            accessData.visitComponent(c, components)
        }
        initRuntimePropertiesTab();
        Tabsheet ts = getComponent("tabsheet");
        if (task.getTaskType() != null) {
            for (FieldInfo fieldInfo: task.getTaskType().getFields()) {
                if ("propertySecurityTab".equals(fieldInfo.getName())) {
                    ts.getTab("securityTab").setVisible(ts.getTab("securityTab").isVisible() && fieldInfo.getVisible());
                } else if ("propertyHistoryTab".equals(fieldInfo.getName())) {
                    ts.getTab("historyTab").setVisible(fieldInfo.getVisible());
                } else if ("propertyCardCommentTab".equals(fieldInfo.getName())
                        && ts.getTab("cardCommentTab") != null) {
                    ts.getTab("cardCommentTab").setVisible(fieldInfo.getVisible());
                } else if ("propertyTaskDependentTab".equals(fieldInfo.getName())) {
                    ts.getTab("taskDependentTab").setVisible(fieldInfo.getVisible());
                } else if ("propertyCardRelationsTab".equals(fieldInfo.getName())) {
                    ts.getTab("cardRelationsTab").setVisible(fieldInfo.getVisible());
                } else if ("propertyTaskTreeTab".equals(fieldInfo.getName())) {
                    ts.getTab("taskTreeTab").setVisible(fieldInfo.getVisible());
                } else if ("propertyAttachmentsTab".equals(fieldInfo.getName())) {
                    ts.getTab("attachmentsTab").setVisible(fieldInfo.getVisible());
                } else if ("propertyCardProjectsTab".equals(fieldInfo.getName())) {
                    ts.getTab("cardProjectsTab").setVisible(fieldInfo.getVisible());
                } else if ("propertyRolesTab".equals(fieldInfo.getName())) {
                    ts.getTab("rolesTab").setVisible(fieldInfo.getVisible());
                }
            }
        }
    }

    protected boolean isProcessAutoSetNeeded() {
        return true
    }

    protected initLazyTabs() {
        Tabsheet tabsheet = getComponent("tabsheet")
        tabsheet.addListener(new Tabsheet.TabChangeListener() {
            @Override
            void tabChanged(Tabsheet.Tab newTab) {
                if ("taskTreeTab".equals(newTab.name)) {
                    CardTreeFrame cardTreeFrame = getComponent("cardTreeFrame")
                    Map<String, Object> params = new HashMap<String, Object>()
                    params.put("card", getItem())
                    cardTreeFrame.init(params)
                    cardTreeFrame.refreshDs()
                } else if ("taskDependentTab".equals(newTab.name)) {
                    TaskDependentFrame taskDependentFrame = getComponent("taskDependentFrame")
                    taskDependentFrame.init()
                    taskDependentFrame.refreshDs()
                } else if ("cardRelationsTab".equals(newTab.name)) {
                    CardRelationsFrame cardRelationsFrame = getComponent("cardRelationsFrame")
                    cardRelationsFrame.init()
                    cardRelationsFrame.refreshDs()
                    if (WfUtils.isCardInStateList(task, 'Finished', 'FinishedByInitiator')) {
                        disableCardRelationFrame()
                    }
                } else if ("cardProjectsTab".equals(newTab.name)) {
                    CardProjectsFrame cardProjectsFrame = getComponent("cardProjectsFrame")
                    cardProjectsFrame.init()
                    TaskmanService taskmanService = ServiceLocator.lookup(TaskmanService.NAME)
                    if (taskmanService.isCurrentUserInProcRole(task, TaskRequest.OBSERVER_ROLE)) {
                        Table cardProjectsTable = ((Table) cardProjectsFrame.getComponent("cardProjectsTable"))
                        cardProjectsTable.getActions().each {action ->
                            action.visible = false
                        }
                    }
                    if (WfUtils.isCardInStateList(task, 'Finished', 'FinishedByInitiator')) {
                        disableCardProjectsFrame()
                    }
                } else if ("securityTab".equals(newTab.name)) {
                    CardSecurityFrame securityFrame = getComponent("securityFrame")
                    securityFrame.init()
                    securityFrame.refreshDs()
//                    } else if ("attachmentsTab".equals(newTab.name)) {
                    //                        commitAttachmentsQuestion();
                }
            }
        })
    }

    protected void setComponentsEnabled() {
        def fieldsToDisable = ['num', 'createDateTime', 'createDateTime', 'finishDateTimeFact', 'description', 'parentCard', 'state', 'finishDateTimePlan', 'fullDescr',
                'duration', 'priority', 'taskType', 'hidden', 'timeUnit', 'percentCompletion', 'taskGroup', 'confirmRequired',
                'reassignEnabled', 'refuseEnabled', 'project', 'projectSelect', 'taskName', 'primaryTask', 'parentCardAccess']
        boolean isCardRolesFrameEnabled = false
        cardIsEditable = false;

        if (task.primaryTask) {
            primaryTask.setEnabled(false)
            primaryTaskLabel.setEnabled(false)
            if (!WfUtils.isCardInState(task.getPrimaryTask(), 'Finished') && !WfUtils.isCardInState(task.getPrimaryTask(), 'FinishedByInitiator')) {
                finishDateTimePlan.setEnabled(false);
            }
        }
        else {
            primaryTask.setVisible(false)
            primaryTaskLabel.setVisible(false)
        }

        percentCompletionSlider.setEnabled(false)

        TaskmanService taskmanService = ServiceLocator.lookup(TaskmanService.NAME)

        if (WfUtils.isCardInState(task, 'New')) {
            percentCompletionSlider.setVisible(false)
            percentCompletion.setVisible(false)
            percentCompletionLabel.setVisible(false)
            primaryTask.setVisible(true)
            primaryTaskLabel.setVisible(true)
            primaryTask.setEnabled(true)
            primaryTaskLabel.setEnabled(true)
        }
        if (WfUtils.isCardInStateList(task, 'Finished', 'FinishedByInitiator')) {
            reminderButton.setVisible(false)
            isCardRolesFrameEnabled = false
            disableCardAttachments()
        } else if (taskmanService.isCurrentUserAdministrator()) {
            fieldsToDisable = ['finishDateTimeFact']
            percentCompletionSlider.setEnabled(true)
            cardIsEditable = true;
            isCardRolesFrameEnabled = true
        } else {
            if (PersistenceHelper.isNew(task)) {
                fieldsToDisable = ['num', 'createDate', 'createDateTime', 'finishDateTimeFact', 'percentCompletion']
                percentCompletionSlider.setEnabled(true)
                cardIsEditable = true;
            } else {
                if ((taskmanService.isCurrentUserTaskCreatorOrInitiator(task)) && (task.jbpmProcessId == null)) {
                    fieldsToDisable = ['num', 'createDate', 'createDateTime', 'finishDateTimeFact', 'percentCompletion']
                    percentCompletionSlider.setEnabled(true)
                    cardIsEditable = true;
                } else if (taskmanService.isCurrentUserInProcRole(task, TaskRequest.INITIATOR_ROLE)) {
                    fieldsToDisable = ['num', 'createDate', 'createDateTime', 'finishDateTimeFact', 'percentCompletion']
                    percentCompletionSlider.setEnabled(true)
                    cardIsEditable = true;
                } else

                if (taskmanService.isCurrentUserInProcRole(task, TaskRequest.INITIATOR_ROLE) && (WfUtils.isCardInStateList(task, 'Refused', 'NotCompleted'))) {
                    fieldsToDisable = ['num', 'createDate', 'createDateTime', 'finishDateTimeFact', 'parentCard', 'percentCompletion', 'primaryTask']
                    percentCompletionSlider.setEnabled(true)
                    cardIsEditable = true;
                } else

                if (taskmanService.isCurrentUserInProcRole(task, TaskRequest.EXECUTOR_ROLE) && (WfUtils.isCardInStateList(task, 'Assigned', 'InWork'))) {
                    fieldsToDisable -= ['percentCompletion']
                    percentCompletionSlider.setEnabled(true)
                } else
                if (taskmanService.isCurrentUserInProcRole(task, TaskRequest.OBSERVER_ROLE)) {
                    enableNumField = false;
                }

                if (taskmanService.isCurrentUserInProcRole(task, TaskRequest.INITIATOR_ROLE)) {
                    fieldsToDisable -= ['project', 'projectSelect']
                    cardIsEditable = true;
                }
            }

            if ((taskmanService.isCurrentUserTaskCreatorOrInitiator(task) && WfUtils.isCardInState(task, 'New')
                    || taskmanService.isCurrentUserAdministrator() || PersistenceHelper.isNew(task))
                    && !(WfUtils.isCardInStateList(task, 'Finished', 'FinishedByInitiator', 'Canceled'))) {
                isCardRolesFrameEnabled = true
                cardIsEditable = true;
            }
        }

        if (enableNumField)
            fieldsToDisable -= ['num']

        if (!isCardRolesFrameEnabled)
            disableCardRolesFrame()

        if (!cardIsEditable){
            disableCardProjectsFrame();
        }

        fieldsToDisable.each {
            Component component = getComponent("$it")
            if (component && component instanceof Component.Editable) {
                component.setEditable(false)
            } else
            if (component && component instanceof Button) {
                component.setVisible(false)
            }
        }
    }

    protected void disableCardRolesFrame() {
        cardRolesFrame.setEditable(false)
    }

    protected void disableCardProjectsFrame() {
        Table cardPojectsTable = getComponent('cardProjectsFrame.cardProjectsTable')
        cardPojectsTable.getActions().each {action ->
            action.visible = false
        }
    }

    protected void disableCardAttachments() {
        Table cardAttachmentsTable = getComponent('cardAttachmentsFrame.attachmentsTable')
        cardAttachmentsTable.getActions().each {action ->
            if (action.id != 'actions.Copy' || action.id != 'copyAttach')
                action.visible = false
        }

        PopupButton createPopup = getComponent("cardAttachmentsFrame.createAttachBtn")
        createPopup.getActions().each {action ->
            action.visible = false
        }
        createPopup.visible = false

        CardAttachmentsFrame attachmentsFrame = getComponent("cardAttachmentsFrame");
        FileUploadField fastUploadButton = attachmentsFrame.fastUploadButton;
        fastUploadButton.setVisible(false);
    }

    protected void disableCardRelationFrame() {
        Table cardRelationTable = getComponent('cardRelationsTab.cardRelationsTable')
        cardRelationTable.getActions().each {action ->
            action.visible = false
        }
    }

    protected void setComponentsVisibility() {
        BoxLayout l = getComponent("resolutionsPane");
        if (((Table) getComponent("resolutionsPane.resolutionsTable")).getDatasource().getItemIds().isEmpty())
            l.setVisible(false);

        if (!finishDateTimePlanDateField.isEditable()) {
            getComponent('durationLabel').setVisible(false)
            getComponent('durationBox').setVisible(false)
        }

        if (task.getFinishDateTimeFact() != null) {
            getComponent('finishDateTimeFactLabel').setVisible(true)
            getComponent('finishDateTimeFactBox').setVisible(true)
        }

        Tabsheet.Tab taskDependentTab = ((Tabsheet) getComponent("tabsheet")).getTab("taskDependentTab");
        taskDependentTab.setVisible((task.getDependentTasks() != null && task.getDependentTasks().size() > 0) || task.getPrimaryTask() != null ? true : false);

        Tabsheet.Tab taskTreeTab = ((Tabsheet) getComponent("tabsheet")).getTab("taskTreeTab");
        taskTreeTab.setVisible((task.getSubCards() != null && task.getSubCards().size() > 0) || task.getParentCard() != null ? true : false);

        setDisplayCheckBoxes();
    }

    protected void initDefaultFields() {
        LookupField timeUnit = getComponent('timeUnit')
        timeUnit.setValue(TimeUnit.DAY)
        Select timeUnitComponent = (Select) WebComponentsHelper.unwrap(timeUnit)
        timeUnitComponent.setNullSelectionAllowed(false)
    }

    protected void setNullSelectionAllowedToFalse() {
        LookupField timeUnit = getComponent('timeUnit')
        Select timeUnitComponent = (Select) WebComponentsHelper.unwrap(timeUnit)
        timeUnitComponent.setNullSelectionAllowed(false)
    }


    protected boolean isCommentVisible() {
        return false
    }

    protected void openCardLookup(String entityName) {
        if (!entityName) {
            return;
        }

        openLookup(entityName + ".browse", new Window.Lookup.Handler() {
            @Override
            void handleLookup(Collection items) {
                if (items && items.size() > 0) {
                    task = getItem()
                    task.parentCard = items.iterator().next()
                    showCopyResolutionConfirmation(entityName)
                }
            }

        }, WindowManager.OpenType.THIS_TAB,
                ["exclItem": getItem()])
    }

    protected <T extends Entity<UUID>> java.util.List<T> getDsItems(CollectionDatasource<T, UUID> ds) {
        java.util.List<T> items = new ArrayList<T>();
        for (UUID id: ds.getItemIds()) {
            items.add(ds.getItem(id));
        }
        return items;
    }

    protected void showCopyResolutionConfirmation(String entityName) {
        String resolution
        if (!DocTypeSelector.TASK_ENTITY_NAME.equals(entityName))
            resolution = task.parentCard.resolution
        List cardAttachments = CardAttachmentHelper.getAttachmentByCard(task.parentCard)
        if (resolution || cardAttachments)
            if (!resolution && BooleanUtils.toBoolean(AppContext.getProperty("thesis.createSubCardAction.cloneAttachmentDialogEnable"))) {
                frame.showOptionDialog(
                        getMessage('dialogs.copyResolutionCaption'),
                        getMessage(resolution ? 'dialogs.copyResolutionFullText' : 'dialogs.copyResolutionText'),
                        IFrame.MessageType.CONFIRMATION,
                        [
                                new DialogAction(DialogAction.Type.OK) {
                                    @Override
                                    public void actionPerform(Component component) {
                                        if (resolution) {
                                            task.taskName = resolution
                                            task.fullDescr = resolution
                                        }

                                        if (cardAttachments) {
                                            TaskRequest task = getItem()
                                            List<CardAttachment> cloneCardAttachments = CardAttachmentHelper.
                                                    cloneAttachments(cardAttachments, task)
                                            if (task.getAttachments() == null)
                                                task.setAttachments(cloneCardAttachments)
                                            else {
                                                task.getAttachments().addAll(cloneCardAttachments)
                                            }
                                            initAttachments(task)
                                            if (!PersistenceHelper.isNew(task))
                                                attachmentsDs.commit()
                                        }
                                    }
                                },
                                new DialogAction(DialogAction.Type.CANCEL)
                        ])
            } else {
                TaskRequest task = getItem()
                List<CardAttachment> cloneCardAttachments = CardAttachmentHelper.
                        cloneAttachments(cardAttachments, task)
                if (task.getAttachments() == null)
                    task.setAttachments(cloneCardAttachments)
                else {
                    task.getAttachments().addAll(cloneCardAttachments)
                }
                initAttachments(task)
                if (!PersistenceHelper.isNew(task))
                    attachmentsDs.commit()
            }
        afterCopyResolutionConfirmation();
    }

    protected void afterCopyResolutionConfirmation(){}

    protected void cloneTask(TaskRequest currentTask, Boolean cloneAttachmentsFlag) {
        TaskmanService taskmanService = ServiceLocator.lookup(TaskmanService.NAME)
        TaskRequest newTask = taskmanService.cloneTask(currentTask, cloneAttachmentsFlag, true)
        java.util.Map<String, Object> params = new java.util.HashMap<String, Object>();
        params.put("taskPattern", newTask);
        openEditor("ext\$TaskRequest.edit", newTask, WindowManager.OpenType.NEW_TAB, params)
    }

    def boolean close(String actionId) {
        return super.close(actionId);
    }

    protected void openTaskGroup(TaskGroup taskGroup, WebButton button) {
        if (taskGroup instanceof SoftDelete && ((SoftDelete) taskGroup).isDeleted()) {
            showNotification(
                    MessageProvider.getMessage(ActionsFieldHelper.class, "ActionsFieldHelper.openMsg"),
                    IFrame.NotificationType.HUMANIZED);
            return;
        }

        if (taskGroup != null) {
            LoadContext ctx = new LoadContext(taskGroup.getClass());
            ctx.setId(taskGroup.getId());
            ctx.setView("edit");
            taskGroup = ServiceLocator.getDataService().load(ctx);
        }

        if (taskGroup != null) {
            Window window = openEditor(getTaskGroupEditAlias(), taskGroup, WindowManager.OpenType.THIS_TAB);
            window.addListener(new com.haulmont.cuba.gui.components.Window.CloseListener() {
                @Override
                void windowClosed(String actionId) {
                    if (actionId == Window.COMMIT_ACTION_ID) {
                        LoadContext ctx = new LoadContext(taskGroup.getClass()).setId(taskGroup.getId()).setView("edit");
                        TaskGroup reloadTaskGroup = ServiceLocator.getDataService().load(ctx);
                        button.setCaption(reloadTaskGroup.getName());
                    }
                }

            });
        } else {
            showNotification(getMessage("TaskRequestEditor.openTaskGroupMsg"),
                    IFrame.NotificationType.HUMANIZED);
        }
    }

    protected String getTaskGroupEditAlias(){
        return "tm\$TaskGroup.edit"
    }

    protected Card loadCard(Card card) {
        LoadContext ctx = new LoadContext(card.getClass());
        ctx.setView(MetadataProvider.getViewRepository().getView(card.getClass(), "cycle"));
        ctx.setId(card.getId());
        return ServiceLocator.getDataService().load(ctx);
    }

    @Override
    public boolean commit() {
        TaskRequest task = (TaskRequest) getItem();
        TaskRequest primaryTask = task.getPrimaryTask();
        if (primaryTask != null && (!hasTaskRole(task.roles, TaskRequest.INITIATOR_ROLE) ||
                !hasTaskRole(task.roles, TaskRequest.EXECUTOR_ROLE))) {
            showNotification(getMessage("saveDependentTaskError"), IFrame.NotificationType.HUMANIZED);
            return false;
        }

        if (primaryTask != null) {
            CardService cardService = ServiceLocator.lookup(CardService.NAME);
            if (cardService.isDescendant(task, primaryTask,
                    MetadataProvider.getViewRepository().getView(TaskRequest.class, "_minimal"), "primaryTask")) {
                showNotification(getMessage("cycle.primaryTask"), IFrame.NotificationType.ERROR);
                return false;
            }
        }

        String message = null;
        Card card = task.getParentCard();
        if (card != null && card.getParentCard() != null) {
            CardService cardService = ServiceLocator.lookup(CardService.NAME);
            if (cardService.isDescendant(task, card,
                    MetadataProvider.getViewRepository().getView(Card.class, "_minimal"), "parentCard")) {
                message = getMessage("cycle.parentCard");
            }
        }
        if (message != null) {
            this.showNotification(message, IFrame.NotificationType.ERROR);
            return false;
        }
        if (super.commit()) {
            if (createFromPatternButton)
                createFromPatternButton.setVisible(false)
            return true
        }
        return false;
    }

    protected boolean hasTaskRole(java.util.List cardRoles, String role) {
        for (CardRole cardRole in cardRoles) {
            if ((cardRole.getCode()).equals(role) && cardRole.getUser() != null) return true;
        }
        return false;
    }

    protected Button getButtonFromFrame(String frameName, String actionName) {
        IFrame actionsFrame = getComponent(frameName)
        if (actionsFrame == null) return null
        for (Component component: actionsFrame.getComponents()) {
            if (component instanceof Button) {
                Action action = ((Button) component).getAction();
                String id = action.getId();
                if ((actionName).equals(id)) {
                    return (Button) component
                }
            }
        }
        return null;
    }

    protected Boolean isUserDepartmentChiefAndNotExecutor() {
        TaskmanService taskmanService = ServiceLocator.lookup(TaskmanService.NAME)
        Boolean result = (taskmanService.isCurrentUserInRole(ThesisConstants.SEC_ROLE_DEPARTMENT_CHIEF)
                || taskmanService.isCurrentUserInRole(ThesisConstants.SEC_ROLE_SUBDIVISION_CHIEF));
        User currentUser = UserSessionProvider.getUserSession().getCurrentOrSubstitutedUser();
        for (UUID uuid: cardRolesDs.getItemIds()) {
            CardRole cardRole = cardRolesDs.getItem(uuid);
            if (cardRole.user == currentUser && TaskRequest.EXECUTOR_ROLE.equals(cardRole.code)) {
                result = false;
                break;
            }
        }
        return result;
    }

//    protected void commitAttachmentsQuestion() {
    //        final TaskRequest task = (TaskRequest) getItem();
    //        if (!PersistenceHelper.isNew(task) && (containsNewAttachment(task.getAttachments()) || attachmentsDs.isModified())) {
    //            showOptionDialog(
    //                    getMessage("saveDialog.caption"),
    //                    getMessage("saveDialog"),
    //                    IFrame.MessageType.WARNING,
    //                    [
    //                            new ActionAdapter(MessageProvider.getMessage(WebWindow.class, "actions.Yes"), [
    //                                    getIcon: {
    //                                        return "icons/ok.png";
    //                                    },
    //
    //                                    actionPerform: {
    //                                        commit();
    //                                    }]),
    //                            new ActionAdapter(MessageProvider.getMessage(WebWindow.class, "actions.No"),
    //                                    [
    //                                            getIcon: {
    //                                                return "icons/cancel.png";
    //                                            },
    //
    //                                            actionPerform: {
    //                                                task.setAttachments(null);
    //                                                addedAttachment.clear()
    //                                                attachmentsDs.clear();
    //                                                attachmentsDs.refresh();
    //                                            }])
    //                    ]);
    //        }
    //    }
    //
    protected boolean containsNewAttachment(Collection<Attachment> attachments) {
        if (attachments != null) {
            for (Attachment attachment: attachments) {
                if (PersistenceHelper.isNew(attachment)) {
                    return true;
                }
            }
        }
        return false;
    }

    public void applySettings(Settings settings) {
        super.applySettings(settings);
        ResizableTextField comment = (ResizableTextField) fullDescr;
        if (comment != null) {
            ResizeHelper.setSize(comment, settings);
            comment.addResizeListener(ResizeHelper.createResizeListener(comment.getId(), settings));
        }
    }

    protected void setDisplayCheckBoxes() {
        if (cardIsEditable)
            return;
        CheckBox reassignEnabled = getComponent("reassignEnabled");
        CheckBox refuseEnabled = getComponent("refuseEnabled");
        CheckBox confirmRequired = getComponent("confirmRequired");
        CheckBox parentCardAccess = getComponent("parentCardAccess");

        Label reassignEnabledLabel = getComponent("reassignEnabledLabel");
        Label refuseEnabledLabel = getComponent("refuseEnabledLabel");
        Label confirmRequiredLabel = getComponent("confirmRequiredLabel");
        Label parentCardAccessLabel = getComponent("parentCardAccessLabel");

        int counter = 0;
        if (task.getReassignEnabled()) {
            reassignEnabled.setVisible(false);
            reassignEnabledLabel.setVisible(false);
            displayLabel(getMessage("TaskRequest.reassignEnabled"), counter);
            counter++;
        } else {
            reassignEnabled.setVisible(false);
            reassignEnabledLabel.setVisible(false);
        }

        if (task.getRefuseEnabled()) {
            refuseEnabled.setVisible(false);
            refuseEnabledLabel.setVisible(false);
            displayLabel(getMessage("TaskRequest.refuseEnabled"), counter);
            counter++;
        } else {
            refuseEnabled.setVisible(false);
            refuseEnabledLabel.setVisible(false);
        }

        if (task.getConfirmRequired()) {
            confirmRequired.setVisible(false);
            confirmRequiredLabel.setVisible(false);
            displayLabel(getMessage("TaskRequest.confirmRequired"), counter);
            counter++;
        } else {
            confirmRequiredLabel.setVisible(false);
            confirmRequired.setVisible(false);
        }

        if (task.getParentCardAccess() && task.getParentCard() != null) {
            parentCardAccess.setVisible(false);
            parentCardAccessLabel.setVisible(false);
            displayLabel(getMessage("TaskRequest.parentCardAccess"), counter);
        } else {
            parentCardAccess.setVisible(false);
            parentCardAccessLabel.setVisible(false);
        }
    }

    private void displayLabel(String value, int counter) {
        def componentNames = ["reassignEnabledLabel", "refuseEnabledLabel", "confirmRequiredLabel", "parentCardAccessLabel"];
        Label label = getComponent(componentNames[counter]);
        com.vaadin.ui.Label vLabel = (com.vaadin.ui.Label) WebComponentsHelper.unwrap(label);
        vLabel.setContentMode(com.vaadin.ui.Label.CONTENT_XHTML);
        label.setStyleName("v-label-notice");
        label.setValue(value);
        label.setVisible(true);
    }

    private class ReassignByExecutorCloseListener implements com.haulmont.cuba.gui.components.Window.CloseListener {
        private Assignment assignment;
        private User prevUser;
        private ReassignForm reassignForm;



        def ReassignByExecutorCloseListener(assignment, prevUser, reassignForm) {
            this.assignment = assignment;
            this.prevUser = prevUser;
            this.reassignForm = reassignForm;
        }

        @Override
        void windowClosed(String actionId) {
            if (actionId == Window.COMMIT_ACTION_ID) {
                TaskmanService taskmanService = ServiceLocator.lookup(TaskmanService.NAME);
                try {
                    if (taskmanService.isCurrentUserInRole(ThesisConstants.SEC_ROLE_DEPARTMENT_CHIEF)
                            || taskmanService.isCurrentUserInRole(ThesisConstants.SEC_ROLE_SUBDIVISION_CHIEF))
                        taskmanService.reassignTaskByDepartmentChief(task, assignment, prevUser, reassignForm.getNewUser(), reassignForm.getComment())
                    else
                        taskmanService.reassignTaskByInitiator(task, assignment, prevUser, reassignForm.getNewUser(), reassignForm.getComment())
                } catch (Exception e) {
                    if (e.getLocalizedMessage().startsWith('there is no transition ReassignByInitiator'))
                        showNotification(getMessage('processError'), getMessage('processDoesnotSupportReassign'), IFrame.NotificationType.ERROR)
                    else
                        showNotification(getMessage('processError'), e.getLocalizedMessage(), IFrame.NotificationType.ERROR)
                    return;
                }
                close(COMMIT_ACTION_ID)
            }
        }
    };
}
