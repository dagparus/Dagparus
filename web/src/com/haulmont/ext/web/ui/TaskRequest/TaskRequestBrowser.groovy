package com.haulmont.ext.web.ui.TaskRequest

import com.haulmont.charts.core.entity.GanttChartItem
import com.haulmont.charts.gui.components.charts.GanttChart
import com.haulmont.chile.core.datatypes.Datatypes
import com.haulmont.chile.core.model.MetaPropertyPath
import com.haulmont.cuba.core.entity.Entity
import com.haulmont.cuba.core.global.CommitContext
import com.haulmont.cuba.core.global.LoadContext
import com.haulmont.cuba.core.global.MetadataProvider
import com.haulmont.cuba.core.global.TimeProvider
import com.haulmont.cuba.core.global.UserSessionProvider
import com.haulmont.cuba.core.global.View
import com.haulmont.cuba.core.sys.AppContext
import com.haulmont.cuba.gui.ServiceLocator
import com.haulmont.cuba.gui.UserSessionClient
import com.haulmont.cuba.gui.WindowManager
import com.haulmont.cuba.gui.app.core.lookup.SimpleLookup
import com.haulmont.cuba.gui.components.AbstractLookup
import com.haulmont.cuba.gui.components.Action
import com.haulmont.cuba.gui.components.ActionAdapter
import com.haulmont.cuba.gui.components.Button
import com.haulmont.cuba.gui.components.CheckBox
import com.haulmont.cuba.gui.components.ExpandingLayout
import com.haulmont.cuba.gui.components.GroupTable
import com.haulmont.cuba.gui.components.IFrame
import com.haulmont.cuba.gui.components.ListActionsHelper
import com.haulmont.cuba.gui.components.PopupButton
import com.haulmont.cuba.gui.components.SplitPanel
import com.haulmont.cuba.gui.components.TableActionsHelper
import com.haulmont.cuba.gui.components.Tabsheet
import com.haulmont.cuba.gui.components.Window
import com.haulmont.cuba.gui.data.CollectionDatasource
import com.haulmont.cuba.gui.data.CollectionDatasourceListener
import com.haulmont.cuba.gui.data.Datasource
import com.haulmont.cuba.gui.data.ValueListener
import com.haulmont.cuba.gui.data.impl.CollectionDsListenerAdapter
import com.haulmont.cuba.gui.data.impl.DsListenerAdapter
import com.haulmont.cuba.gui.settings.Settings
import com.haulmont.cuba.report.Report
import com.haulmont.cuba.security.app.UserSettingService
import com.haulmont.cuba.security.entity.EntityOp
import com.haulmont.cuba.security.entity.User
import com.haulmont.cuba.web.App
import com.haulmont.cuba.web.app.LinkColumnHelper
import com.haulmont.cuba.web.filestorage.WebExportDisplay
import com.haulmont.cuba.web.gui.components.WebComponentsHelper
import com.haulmont.cuba.web.gui.components.WebFilter
import com.haulmont.cuba.web.gui.components.WebPopupButton
import com.haulmont.docflow.core.app.CardService
import com.haulmont.docflow.core.app.ThesisConstants
import com.haulmont.docflow.web.DocflowAppWindow
import com.haulmont.docflow.web.actions.CardLinkAction
import com.haulmont.docflow.web.actions.ThesisExcelAction
import com.haulmont.docflow.web.ui.common.DocflowHelper
import com.haulmont.docflow.web.ui.common.ThesisPrintHelper
import com.haulmont.ext.core.entity.TaskRequest
import com.haulmont.taskman.core.app.TaskmanService
import com.haulmont.taskman.core.entity.CardUserInfo
import com.haulmont.taskman.core.entity.Priority
import com.haulmont.taskman.core.entity.TaskInfo
import com.haulmont.taskman.core.entity.TaskPattern
import com.haulmont.taskman.web.ui.TaskDatasource
import com.haulmont.taskman.web.ui.common.CardTreeFrame
import com.haulmont.taskman.web.ui.common.CreateSubCardAction
import com.haulmont.taskman.web.ui.common.RemoveCardNullChildAction
import com.haulmont.taskman.web.ui.task.AttachmentMenuBuilderAction
import com.haulmont.taskman.web.ui.task.GanttChartLoader
import com.haulmont.taskman.web.ui.task.ProcessMenuBuilderAction
import com.haulmont.taskman.web.ui.task.TaskBrowser
import com.haulmont.workflow.core.app.WfService
import com.haulmont.workflow.core.app.WfUtils
import com.haulmont.workflow.core.entity.Card
import com.haulmont.workflow.core.entity.CardInfo
import com.haulmont.workflow.web.ui.base.ResolutionsFrame
import com.vaadin.terminal.ThemeResource
import com.vaadin.ui.AbstractOrderedLayout
import com.vaadin.ui.AbstractSelect
import com.vaadin.ui.Component
import com.vaadin.ui.Table
import org.apache.commons.lang.BooleanUtils
import org.apache.commons.lang.StringUtils
import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory

/**
 * Created with IntelliJ IDEA.
 * User: mahdi
 * Date: 11/5/13
 * Time: 9:40 AM
 * To change this template use File | Settings | File Templates.
 */
public class TaskRequestBrowser extends AbstractLookup {

    protected GroupTable table
    protected String nullActionId
    protected String createSubTaskActionId
    protected String createFromPatternActionId
    protected String createFromCurrentId

    protected AbstractSelect actionsSelect

    protected List<TaskInfo> infoList
    protected List<CardInfo> infoCardList

    protected boolean tableInitialized = false
    protected boolean treeExpanded = false
    protected Button exel;
    protected PopupButton create;

    protected ResolutionsFrame resolutionsFrame
    protected WfService wfService
    protected CheckBox showResolutions
    protected static Log log = LogFactory.getLog(TaskBrowser.class);
    protected CardTreeFrame cardTreeFrame;
    protected Tabsheet tabsheet;

    protected GanttChartLoader ganttLoader;

    TaskRequestBrowser(IFrame iFrame) {
        super(iFrame)
    }

    public void init(Map<String, Object> params) {
        super.init(params);

        wfService = ServiceLocator.lookup(WfService.NAME)
        hideCheckBoxIfLookup();
        initGanttChart();

        table = getComponent("tasksTable")
        TableActionsHelper helper = new TableActionsHelper(this, table)
        //helper.createCreateAction()

        boolean multiselect = params.get('multiselect')
        table.multiSelect = multiselect == null || multiselect == true;

        tabsheet = getComponent("tabsheet");
        tabsheet.addListener(new Tabsheet.TabChangeListener() {
            public void tabChanged(Tabsheet.Tab newTab) {
                Card card = (Card) table.getSingleSelected();
                if ("hierarchyTab".equals(newTab.getName())) {
                    if (cardTreeFrame != null)
                        cardTreeFrame.setCard(card);
                } else if ("resolutionsTab".equals(newTab.getName())) {
                    if (resolutionsFrame != null)
                        resolutionsFrame.setCard(card);
                }
            }
        });

        table.addAction(new ActionAdapter('deleteNotification', [
                actionPerform: {
                    Set selected = table.getSelected();
                    if (selected.size() != 1) return;

                    WfService wfService = ServiceLocator.lookup(WfService.NAME);
                    User user = UserSessionClient.getUserSession().getCurrentOrSubstitutedUser();
                    wfService.deleteNotifications(table.getDatasource().getItem(), user);

                    table.getDatasource().refresh();
                    ((DocflowAppWindow) App.getInstance().getAppWindow()).reloadAppFolders(selected.toList());
                },
                getCaption: {
                    return getMessage("deleteNotification")
                }
        ]))


        table.addAction(new ActionAdapter('saveAsTemplate', [
                actionPerform: {
                    Set selected = table.getSelected();
                    if (selected.size() != 1)
                        return;
                    TaskRequest taskRequest = table.getDatasource().getItem();
                    TaskmanService service = ServiceLocator.lookup(TaskmanService.NAME)
                    TaskPattern newTaskPattern = service.createTaskPattern(taskRequest)
                    newTaskPattern.creator = UserSessionClient.userSession.user
                    newTaskPattern.substitutedCreator = UserSessionClient.userSession.getCurrentOrSubstitutedUser()
                    openEditor('tm$TaskPattern.edit', newTaskPattern, WindowManager.OpenType.THIS_TAB);
                },
                getCaption: {
                    return getMessage("saveAsTemplate")
                },
                isEnabled: {
                    com.haulmont.chile.core.model.MetaClass metaClass = table.getDatasource().getMetaClass();
                    return UserSessionClient.getUserSession().isEntityOpPermitted(metaClass, EntityOp.CREATE);
                }
        ]))

        table.addAction(new ActionAdapter('create', [
                actionPerform: {
                    if (create != null)
                        create.setPopupVisible(false);
                    Window window = openEditor('ext$TaskRequest.edit', MetadataProvider.create(TaskRequest.class), WindowManager.OpenType.THIS_TAB)
                    window.addListener(new Window.CloseListener() {
                        @Override
                        void windowClosed(String actionId) {
                            table.getDatasource().refresh();
                        }

                    })
                },
                getCaption: {
                    return getMessage('actions.Create')
                },
                isEnabled: {
                    com.haulmont.chile.core.model.MetaClass metaClass = table.getDatasource().getMetaClass();
                    return UserSessionClient.getUserSession().isEntityOpPermitted(metaClass, EntityOp.CREATE);
                }
        ]))

        table.addAction(new RemoveCardNullChildAction("remove", this, table));

        Action removeAction = table.getAction("remove");
        TaskmanService taskmanService = ServiceLocator.lookup(TaskmanService.NAME)
        CollectionDatasource taskRequestsDs = getDsContext().get('taskRequestsDs')
        taskRequestsDs.addListener(new DsListenerAdapter() {
            @Override
            void valueChanged(Object source, String property, Object prevValue, Object value) {

            }

            @Override
            void itemChanged(Datasource ds, Entity prevItem, Entity item) {
                super.itemChanged(ds, prevItem, item)
                if (null == item) return

                // ADMINISTRATOR_ROLE can remove any task, the CREATOR,
                // NOT INITIATOR only not sent on process
                User currentUser = UserSessionClient.getUserSession().getCurrentOrSubstitutedUser();
                Iterator<TaskRequest> iterator = table.getSelected().iterator();
                boolean removeEnabled = true;
                while (iterator.hasNext()) {
                    TaskRequest ts = iterator.next();
                    if (isRemoveEnabled(ts, currentUser))
                        removeEnabled = false;
                }
                if (removeAction)
                    removeAction.enabled = removeEnabled;
            }


        })

        table.addAction(new ActionAdapter('edit', [
                actionPerform: {
                    Set selected = table.getSelected()
                    if (selected.size() == 1) {
                        openTask(selected.iterator().next())
                    }
                },
                getCaption: {
                    return getMessage('actions.Edit')
                }
        ]))

        table.addAction(new ThesisExcelAction(table, new WebExportDisplay(), (WebFilter) getComponent("genericFilter")));
        helper.createRefreshAction();
        PopupButton print = (PopupButton) getComponent("print");
        if (print != null)
            print.setDescription(getMessage("print"));
        exel = (Button) getComponent("excel");
        if (exel != null)
            exel.setDescription(getMessage("actions.Excel"));
        Button expandCollapseTrigger = (Button) getComponent("expandCollapseTrigger")
        if (expandCollapseTrigger != null)
            expandCollapseTrigger.setDescription(getMessage("expandCollapseTrigger"));
        create = getComponent("create");
        com.haulmont.chile.core.model.MetaClass taskMetaClass = table.getDatasource().getMetaClass();
        if (create != null) {
            if (DocflowHelper.createSubCardIsPermitted()) {
                create.addAction(new ActionAdapter('create', [
                        actionPerform: {
                            if (create != null)
                                create.setPopupVisible(false);
                            Window window = openEditor('ext\$TaskRequest.edit', MetadataProvider.create(TaskRequest.class), WindowManager.OpenType.THIS_TAB)
                            window.addListener(new Window.CloseListener() {
                                @Override
                                void windowClosed(String actionId) {
                                    table.getDatasource().refresh();
                                }
                            })
                        },
                        getCaption: {
                            return getMessage('createNew')
                        },
                        isEnabled: {
                            return UserSessionClient.getUserSession().isEntityOpPermitted(taskMetaClass, EntityOp.CREATE);
                        }
                ]));

              /* create.addAction(new ActionAdapter('createSubTask', [
                        actionPerform: {
                            createSubTask();
                            create.setPopupVisible(false);
                        },
                        getCaption: {
                            return getMessage("createSubCard")
                        }
                ]))

                create.addAction(new ActionAdapter('createFromPattern', [
                        actionPerform: {
                            createFromPattern();
                            create.setPopupVisible(false);
                        },
                        getCaption: {
                            return getMessage('createFromPattern')
                        },
                        isEnabled: {
                            return UserSessionClient.getUserSession().isEntityOpPermitted(taskMetaClass, EntityOp.CREATE);
                        }
                ]))

                create.addAction(new ActionAdapter('createFromCurrent', [
                        actionPerform: {
                            createFromCurrent();
                            create.setPopupVisible(false);
                        },
                        getCaption: {
                            return getMessage("createFromCurrent")
                        },
                        isEnabled: {
                            return UserSessionClient.getUserSession().isEntityOpPermitted(taskMetaClass, EntityOp.CREATE);
                        }
                ]))

                create.addAction(new ActionAdapter('createIsYouTask', [
                        actionPerform: {
                            HashMap<String, Object> createParams = new HashMap();
                            createParams.put("isYouTask", true);
                            Window window = openEditor("ext\$TaskRequest.edit", MetadataProvider.create(TaskRequest.class), WindowManager.OpenType.THIS_TAB, createParams);
                            window.addListener(new Window.CloseListener() {
                                @Override
                                void windowClosed(String actionId) {
                                    table.getDatasource().refresh();
                                }

                            })
                            create.setPopupVisible(false);
                        },
                        getCaption: { return getMessage("createIsYouTask") },
                        isEnabled: {
                            return currentUserInRole(ThesisConstants.SEC_ROLE_TASK_INITIATOR) &&
                                    currentUserInRole(ThesisConstants.SEC_ROLE_TASK_EXECUTOR) &&
                                    currentUserInRole(ThesisConstants.SEC_ROLE_TASK_CREATOR);
                        }
                ]))   */
            } else
                create.visible = false

        }

        if (!currentUserInRole(ThesisConstants.SEC_ROLE_TASK_CREATOR)) {
            if (removeAction)
                removeAction.visible = false
            if (table.getAction("saveAsTemplate"))
                table.getAction("saveAsTemplate").visible = false
        }

        table.addAction(new ActionAdapter('expandCollapseTrigger', [
                actionPerform: { if (treeExpanded) table.collapseAll(); else table.expandAll(); treeExpanded = !treeExpanded; },
                getCaption: { return "+/-"; }
        ]))

        table.addAction(new CardLinkAction(taskRequestsDs))

        PopupButton button = getComponent("print")
        if (button != null) {
            button.setDescription(getMessage("print"));
            org.vaadin.hene.popupbutton.PopupButton vPopupButton =
                (org.vaadin.hene.popupbutton.PopupButton) WebComponentsHelper.unwrap(button)

            vPopupButton.addPopupVisibilityListener(new org.vaadin.hene.popupbutton.PopupButton.PopupVisibilityListener() {
                @Override
                void popupVisibilityChange(org.vaadin.hene.popupbutton.PopupButton.PopupVisibilityEvent event) {
                    TaskRequest ttask = table.getSingleSelected()
                    if (ttask != null && button.getAction("printTask") == null) {
                        button.addAction(new ActionAdapter("printTask", [
                                actionPerform: {
                                    ThesisPrintHelper.printTask(table.getSingleSelected())
                                },
                                getCaption: {
                                    return getMessage("printTask")
                                }
                        ]))
                    }
                }

            })

            button.addAction(new ActionAdapter("printExecutorReport", [
                    actionPerform: {
                        LoadContext ctx = new LoadContext(Report.class)
                        ctx.setQueryString('select r from report$Report r where r.name = :param').addParameter('param', getMessage('executorReportName'))
                        ctx.setView('_local')
                        Report report = ServiceLocator.getDataService().load(ctx)
                        if (report != null) {
                            openWindow('runExecutorReport.form', WindowManager.OpenType.DIALOG, Collections.<String, Object> singletonMap("report", report));
                        }
                    },
                    getCaption: {
                        return getMessage("printExecutorReport")
                    }
            ]));
            ThesisPrintHelper.generateAction(button, "ext\$TaskRequest.browse",
                    new ThesisPrintHelper.ParamsHandler() {
                        @Override
                        public Map<String, Object> getParams() {
                            if (table.getSingleSelected())
                                return Collections.<String, Object> singletonMap("entity", table.getSingleSelected());
                            else
                                return Collections.<String, Object> emptyMap();
                        }
                    });
        }

        helper.addListener(new ListActionsHelper.Listener() {
            @Override
            void entityCreated(Entity entity) {
                getDsContext().get("taskRequestsDs").refresh()
            }

            @Override
            void entityEdited(Entity entity) {
                getDsContext().get("taskRequestsDs").refresh()
            }

            @Override
            void entityRemoved(Set<Entity> entity) {
                ((DocflowAppWindow) App.getInstance().getAppWindow()).reloadAppFolders(entity.toList());
            }
        });

        table.getDatasource().addListener(new CollectionDsListenerAdapter() {
            @Override
            void valueChanged(Object source, String property, Object prevValue, Object value) {

            }

            @Override
            void itemChanged(Datasource ds, Entity prevItem, Entity item) {
                super.itemChanged(ds, prevItem, item)
                def card = table.getSingleSelected()
                if ((showResolutions != null) && BooleanUtils.isTrue((Boolean) showResolutions.getValue()) &&
                        (item == null || table.getSelected().size() < 2)) {
                    String currentTab = tabsheet.getTab().getName();
                    if (currentTab.equals("resolutionsTab") && resolutionsFrame != null)
                        resolutionsFrame.setCard(card);
                    if (currentTab.equals("hierarchyTab") && cardTreeFrame != null)
                        cardTreeFrame.setCard(card);
                }
            }

            @Override
            void collectionChanged(CollectionDatasource ds, CollectionDatasourceListener.Operation operation) {
                super.collectionChanged(ds, operation)
                loadCardInfoList()
                initTableColoring()
            }

        })

        addHasAttachmentColumn();
        addImportantColumn();
        DocflowHelper.addCategoryAttrsGeneratedColumns(table);
    }

    protected boolean isRemoveEnabled(TaskRequest ts, User currentUser) {
        TaskmanService taskmanService = ServiceLocator.lookup(TaskmanService.NAME)
        return ((WfUtils.isCardInState(ts, 'New') && !(currentUser.equals(ts.getSubstitutedCreator()) || taskmanService.isCurrentUserAdministrator()))
                || (!WfUtils.isCardInState(ts, 'New') && !taskmanService.isCurrentUserAdministrator()));
    }

    private void initGanttChart() {
        GanttChart ganttChart = getComponent("ganttChart");
        String prop = AppContext.getProperty("thesis.ganttChartColumns");
        ganttChart.showStartDate = false;
        ganttChart.showEndDate = false;
        List<String> visibleCols = StringUtils.isNotBlank(prop) ? Arrays.asList(StringUtils.split(StringUtils.trim(prop), ",")) : Collections.emptyList();
        for (String column: visibleCols) {
            def name = StringUtils.trim(column);
            ganttChart."show${name}" = true;
        }
        ganttChart.dateFormat = Datatypes.getFormatStrings(UserSessionProvider.getUserSession().getLocale()).getDateFormat();
        ganttChart.setTaskClickListener(new GanttChart.TaskClickListener() {
            @Override
            void performClick(GanttChartItem taskItem) {
                openTask(taskItem.relatedEntity);
            }
        });

        final TaskDatasource taskDatasource = getDsContext().get("taskRequestsDs");
        ganttLoader = GanttChartLoader.createLoader(getDsContext().get("ganttChartDs"));
        taskDatasource.addListener(new DsListenerAdapter() {
            @Override
            void valueChanged(Object source, String property, Object prevValue, Object value) {

            }

            @Override
            void stateChanged(Datasource ds, Datasource.State prevState, Datasource.State state) {
                super.stateChanged(ds, prevState, state)
                if ((prevState != state) && (state == Datasource.State.VALID))
                    ganttLoader.loadFromDatasource(ds);
            }

        })
        taskDatasource.setSortListener(new TaskDatasource.SortListener() {
            @Override
            void handleSort(CollectionDatasource.Sortable.SortInfo[] sortInfos) {
                ganttLoader.loadFromDatasource(taskDatasource);
            }
        });
        Button showGanttChart = getComponent("showGanttChart")
        Button ganttChartRefresh = getComponent("ganttChartRefresh");
        ganttChartRefresh.action = new ActionAdapter("actions.Refresh", [
                actionPerform: {
                    getDsContext().get("taskRequestsDs").refresh();
                }]) {
            public boolean isVisible() {return ganttChart.visible}
        };

        ExpandingLayout mainLayout = getComponent("mainLayout");
        SplitPanel ganttSplit = getComponent("splitGantt");
        SplitPanel tableSplit = getComponent("split");
        CheckBox logCheckbox = getComponent("hideResolutions");
        Boolean logCheckBoxIsVisible = logCheckbox.isVisible();
        ganttSplit.locked = true;
        showGanttChart.setAction(new ActionAdapter("", [
                actionPerform: {
                    def tableView = getComponent("table-panel");
                    tableView.visible = ganttChart.visible;
                    ganttChart.visible = !ganttChart.visible;
                    if (logCheckBoxIsVisible)
                        logCheckbox.visible = !ganttChart.visible;
                    ganttChartRefresh.visible = ganttChart.visible;
                    if (ganttChart.visible) {
                        showGanttChart.setIcon("icons/table_view.png?1");
                        mainLayout.expand(ganttSplit);
                        Component layout = WebComponentsHelper.unwrap(mainLayout);
                        if (layout instanceof AbstractOrderedLayout) {
                            ((AbstractOrderedLayout) layout).setExpandRatio(WebComponentsHelper.unwrap(tableSplit), 0.0f);
                        }
                    }
                    else {
                        showGanttChart.setIcon("icons/gantt_chart_view.png?1");
                        mainLayout.expand(tableSplit);
                        Component layout = WebComponentsHelper.unwrap(mainLayout);
                        if (layout instanceof AbstractOrderedLayout) {
                            ((AbstractOrderedLayout) layout).setExpandRatio(WebComponentsHelper.unwrap(ganttSplit), 0.0f);
                        }
                    }
                }
        ]));
    }

    private void hideCheckBoxIfLookup() {
        if (getFrame() instanceof Window.Lookup) {
            getComponent("hideResolutions").setVisible(false);
        }
    }

    protected void addHasAttachmentColumn() {
        CollectionDatasource ds = table.getDatasource();
        MetaPropertyPath pp = ds.getMetaClass().getPropertyEx('hasAttachments');
        com.vaadin.ui.Table vTable = (com.vaadin.ui.Table) WebComponentsHelper.unwrap(table);
        vTable.removeGeneratedColumn(pp);
        vTable.addGeneratedColumn(pp, new com.vaadin.ui.Table.ColumnGenerator() {
            @Override
            Component generateCell(Table source, Object itemId, Object columnId) {
                TaskRequest task = ds.getItem((UUID) itemId);
                if (task.hasAttachments) {
                    com.vaadin.ui.Component component;
                    PopupButton popupButton = new WebPopupButton()
                    popupButton.setCaption("");
                    popupButton.setWidth("17px");
                    popupButton.setIcon("icons/attachment-small.png");
                    component = WebComponentsHelper.unwrap(popupButton);
                    component.addStyleName("without-indicator");
                    component.addListener(new AttachmentMenuBuilderAction(task, table, popupButton));
                    return component;
                }
                return null;
            }
        });
    }

    protected void addImportantColumn() {
        CollectionDatasource ds = table.getDatasource();
        MetaPropertyPath pp = ds.getMetaClass().getPropertyEx('important');
        User user = UserSessionClient.getUserSession().getCurrentOrSubstitutedUser();
        com.vaadin.ui.Table vTable = (com.vaadin.ui.Table) WebComponentsHelper.unwrap(table);
        vTable.removeGeneratedColumn(pp);
        vTable.addGeneratedColumn(pp, new com.vaadin.ui.Table.ColumnGenerator() {
            @Override
            Component generateCell(Table source, Object itemId, Object columnId) {
                final TaskRequest task = ds.getItem((UUID) itemId);
                def button = new com.vaadin.ui.Button("");
                button.setStyleName("link");
                def cardUserInfo = task.getCardUserInfo()
                if (task.getImportant())
                    button.setIcon(new ThemeResource("icons/ico-star-active.png"));
                else
                    button.setIcon(new ThemeResource("icons/ico-star-inactive.png"));

                def listener = [
                        buttonClick: {com.vaadin.ui.Button.ClickEvent event ->
                            if (cardUserInfo == null) {
                                cardUserInfo = new CardUserInfo();
                                cardUserInfo.setCard(task);
                                cardUserInfo.setUser(user);
                            }
                            cardUserInfo.setImportant(!BooleanUtils.isTrue(cardUserInfo.getImportant()));
                            def commitedSet = ServiceLocator.getDataService().commit(new CommitContext([cardUserInfo]))
                            task.setCardUserInfo(commitedSet.iterator().next())
                            ds.updateItem(task)
                        }
                ] as com.vaadin.ui.Button.ClickListener
                button.addListener(listener);
                button.setWidth("-1px");
                return button;
            }
        });
    }

    protected void addPriorityColumn() {
        final CollectionDatasource ds = table.getDatasource();
        MetaPropertyPath nameProperty = ds.getMetaClass().getPropertyEx('priority');
        com.vaadin.ui.Table vTable = WebComponentsHelper.unwrap(table)
        vTable.addGeneratedColumn(nameProperty, new com.vaadin.ui.Table.ColumnGenerator() {
            @Override
            Component generateCell(Table source, Object itemId, Object columnId) {
                TaskRequest item = (TaskRequest) ds.getItem(itemId)
                Priority priority = item.getPriority()
                if (priority) {
                    com.vaadin.ui.Embedded priorityImage = new com.vaadin.ui.Embedded("",
                            new ThemeResource("icons/priority-$priority.orderNo" + ".png"))
                    return priorityImage
                }
                return null
            }
        })
    }

    void initTableColoring() {
        Long notifyOverdueTaskInterval = new Long(AppContext.getProperty('taskman.notifyOverdueTaskInterval'))
        Calendar currentTime = Calendar.getInstance();
        currentTime.setTime(TimeProvider.currentTimestamp())

        def getStyleName = {TaskRequest task ->
            String reminderStyle;
            if (!WfUtils.isCardInState(task, 'Finished') && task.finishDateTimePlan &&
                    !WfUtils.isCardInState(task, 'Canceled') && !WfUtils.isCardInState(task, 'New') &&
                    !WfUtils.isCardInState(task, 'FinishedByInitiator')) {
                Calendar taskFinishTime = Calendar.getInstance()
                taskFinishTime.setTime(task.finishDateTimePlan)
                Long timeDiff = taskFinishTime.getTimeInMillis() - currentTime.getTimeInMillis()
                if ((timeDiff > 0) && (timeDiff < notifyOverdueTaskInterval))
                    return 'tasknotifyoverdue'
                else if (taskFinishTime.compareTo(currentTime) < 0) {
                    if (getRemiderStyle(task) != null)
                        return 'taskremindoverdue'
                    else
                        return 'taskoverdue'
                }
            } else if (WfUtils.isCardInStateList(task, 'Finished', 'FinishedByInitiator')) {
                return 'taskfinished';
            }
            reminderStyle = getRemiderStyle(task)
            if (reminderStyle != null)
                return reminderStyle;
            return ''
        }

        com.vaadin.ui.Table vTable = WebComponentsHelper.unwrap(table)
        vTable.setCellStyleGenerator(new com.vaadin.ui.Table.CellStyleGenerator() {
            @Override
            String getStyle(Object itemId, Object propertyId) {
                if (!propertyId) {
                    if (!(itemId instanceof UUID)) return ''
                    TaskRequest task = table.getDatasource().getItem(itemId)
                    return getStyleName(task)
                }
                return ''
            }
        })
    }

    protected void loadTaskInfoList() {
        LoadContext lc = new LoadContext(TaskInfo.class)
        LoadContext.Query q = lc.setQueryString('select ti from tm$TaskInfo ti where ti.user.id = :userId')
        q.addParameter('userId', UserSessionClient.currentOrSubstitutedUserId())
        lc.setView('task-browse')
        infoList = ServiceLocator.getDataService().loadList(lc)
    }

    protected void loadCardInfoList() {
        LoadContext lc = new LoadContext(CardInfo.class);
        LoadContext.Query q = lc.setQueryString("select ci from wf\$CardInfo ci where ci.type<>:type and ci.user.id = :userId");
        q.addParameter("userId", UserSessionClient.currentOrSubstitutedUserId());
        q.addParameter("type", CardInfo.TYPE_SIMPLE);
        lc.setView("card-browse");
        infoCardList = ServiceLocator.getDataService().loadList(lc);
    }

    public boolean currentUserInRole(String nameRole) {
        String[] roles = UserSessionClient.getUserSession().getRoles();
        return Arrays.asList(roles).contains(nameRole);
    }

    protected Window createFromPattern() {
        App.getInstance().getWindowManager().getDialogParams().setWidth(750).setHeight(600);
        SimpleLookup.LookupConfig lookupConfig = new SimpleLookup.LookupConfig(
                entity: 'tm$TaskPattern',
                componentType: SimpleLookup.ComponentType.TABLE,
                query: 'select tp from tm$TaskPattern tp order by tp.patternName',
                columns: ['patternName'])

        Map<String, Object> lookupParams = ['lookupConfig': lookupConfig]

        return openLookup('core$SimpleLookup', new Window.Lookup.Handler() {
            @Override
            void handleLookup(Collection items) {
                TaskPattern taskPattern = items.asList()[0]
                LoadContext ctx = new LoadContext(TaskPattern.class).setId(taskPattern.id).setView('edit')
                taskPattern = ServiceLocator.getDataService().load(ctx)
                TaskmanService taskmanService = ServiceLocator.lookup(TaskmanService.NAME)
                def filledTask = taskmanService.cloneTask(taskPattern)
                def params = ['taskPattern': taskPattern]
                Window window = openEditor("tm\$TaskRequest.edit", filledTask, WindowManager.OpenType.THIS_TAB, params)
                window.addListener(new Window.CloseListener() {
                    @Override
                    void windowClosed(String actionId) {
                        TaskRequestBrowser.this.loadCardInfoList()
                        TaskRequestBrowser.this.table.getDatasource().refresh()
                    }
                })
            }
        },
                WindowManager.OpenType.DIALOG,
                lookupParams)

    }

    protected TaskRequest createFromCurrent() {
        TaskRequest currentTask = getDsContext().get("taskRequestsDs").getItem()
        TaskRequest newTask = null
        if (currentTask) {

            //We have to reload task with edit view, 'cause we need roles collection
            Class taskClass = MetadataProvider.getReplacedClass(TaskRequest.class)
            View taskEditView = MetadataProvider.getViewRepository().getView(taskClass, "edit")
            def ctx = new LoadContext(taskClass).setId(currentTask.id).setView(taskEditView)
            currentTask = ServiceLocator.getDataService().load(ctx)

            TaskmanService taskmanService = ServiceLocator.lookup(TaskmanService.NAME)
            newTask = taskmanService.cloneTask(currentTask)
            def params = ['taskPattern': currentTask]
            Window window = openEditor("ext\$TaskRequest.edit", newTask, WindowManager.OpenType.THIS_TAB, params)
            window.addListener(new Window.CloseListener() {
                @Override
                void windowClosed(String actionId) {
                    loadCardInfoList()
                    table.getDatasource().refresh()
                }
            })
        } else {
            showNotification(getMessage("selectTask.msg"), IFrame.NotificationType.HUMANIZED)
        }
    }

    protected def createSubTask() {
        TaskRequest parentTask = getDsContext().get("taskRequestsDs").getItem()
        if (parentTask) {
            new CreateSubCardAction('', this).actionPerform(null);
        } else {
            showNotification(getMessage("selectParentTask.msg"), IFrame.NotificationType.HUMANIZED)
        }
    }

    protected void openTask(Entity entity) {
        UserSettingService uss = ServiceLocator.lookup(UserSettingService.NAME);
        WindowManager.OpenType openType = "newTab".equals(uss.loadSetting("openEditorMode")) ?
            WindowManager.OpenType.NEW_TAB : WindowManager.OpenType.THIS_TAB;
        Window window = openEditor('ext$TaskRequest.edit', entity, openType)
        window.addListener(new Window.CloseListener() {
            @Override
            void windowClosed(String actionId) {
                if (actionId == Window.COMMIT_ACTION_ID) {
                    //loadTaskInfoList()
                    table.getDatasource().refresh()
                }

                loadCardInfoList()
            }
        })
    }

    def void applySettings(Settings settings) {
        boolean isLookup = (getFrame() instanceof Window.Lookup)
        super.applySettings(settings);
        initAfterSetLookup(isLookup)

        table.removeAction(table.getAction("print"));
        table.removeAction(table.getAction("expandCollapseTrigger"));
        table.removeAction(table.getAction("refresh"));
        table.removeAction(table.getAction("excel"));
        if (exel != null)
            exel.setCaption("");
    }


    protected String getRemiderStyle(TaskRequest task) {
        if (infoCardList != null) {
            for (CardInfo cardInfo: infoCardList) {
                if (cardInfo.card.equals(task) && cardInfo.deleteTs == null) {
                    return 'taskremind'
                }
            }
        }
        return null;
    }

    @Override
    void closeAndRun(String actionId, Runnable runnable) {
        Settings settings = getSettings();
        if (settings != null && showResolutions != null) {
            settings.get("hideResolutions").addAttribute("value", showResolutions.getValue().toString());
        }
        super.closeAndRun(actionId, runnable)
    }
//    public boolean close(String actionId) {
    //        Settings settings = getSettings();
    //        if (settings != null) {
    //            settings.get("hideResolutions").addAttribute("value", showResolutions.getValue().toString());
    //        }
    //
    //        return super.close(actionId);
    //    }
    private void initAfterSetLookup(boolean isLookup) {
        def datasource = table.getDatasource()
        User user = UserSessionClient.getUserSession().getCurrentOrSubstitutedUser()
        SplitPanel splitPanel = getComponent('split')
        splitPanel.setSplitPosition(100);
        splitPanel.setLocked(true)
        if (isLookup) {
            table.setMultiSelect(false)
        } else {
            LinkColumnHelper.initColumn(table, "num", new LinkColumnHelper.Handler() {
                @Override
                void onClick(Entity entity) {
                    openTask(entity)
                }

            });

            final Card exclItem = (Card) context.getParamValue('exclItem')
            if (exclItem) {
                this.setLookupValidator(new Window.Lookup.Validator() {
                    @Override
                    boolean validate() {
                        Card selectedCard = table.getSingleSelected()
                        CardService cardService = ServiceLocator.lookup(CardService.NAME);
                        if (selectedCard && cardService.isDescendant(exclItem, selectedCard,
                                MetadataProvider.getViewRepository().getView(Card.class, "_minimal"), "parentCard")) {
                            showNotification(getMessage('cardIsChild'), IFrame.NotificationType.WARNING);
                            return false;
                        }
                        return true
                    }
                });
            }
//    Map<Object, com.vaadin.ui.Component> stateMap = new HashMap();
            ((com.vaadin.ui.Table) WebComponentsHelper.unwrap(table)).addGeneratedColumn(datasource.getMetaClass().getPropertyEx("locState"),
                    new com.vaadin.ui.Table.ColumnGenerator() {
                        @Override
                        Component generateCell(Table source, Object itemId, Object columnId) {
                            com.vaadin.ui.Component component// = stateMap.get(itemId)
                            //                      if (component != null)
                            //                        return component
                            TaskRequest card = datasource.getItem(itemId);
                            if (card != null && !(WfUtils.isCardInStateList(card, 'Finished', 'FinishedByInitiator', 'Canceled')) &&
                                    (user.equals(card.currentActor) || (WfUtils.isCardInState(card, 'New') &&
                                            (user.equals(card.getInitiator()) || user.equals(card.getSubstitutedCreator()) || user.getLogin().equals(card.getCreatedBy()))))) {
                                PopupButton popupButton = new WebPopupButton()
                                popupButton.setCaption(datasource.getItem(itemId).locState)
                                component = WebComponentsHelper.unwrap(popupButton)
                                component.addListener(new ProcessMenuBuilderAction(card, table, popupButton))
                                component.addStyleName('link')
                                component.addStyleName('dashed')
                            } else {
                                component = new com.vaadin.ui.Label(card == null ? '' : card.getLocState(), com.vaadin.ui.Label.CONTENT_XHTML)
//                        component.addStyleName(ThesisTheme.LABEL_CENTER)
                            }
                            component.setWidth("-1px")
//                      stateMap.put(itemId, component)
                            return component
                        }
                    });

            resolutionsFrame = getComponent('resolutionsFrame')
            resolutionsFrame.init()


            cardTreeFrame = getComponent("cardTreeFrame");
            Map<String, Object> paramsFrame = new HashMap<String, Object>();
            paramsFrame.put("card", null);
            cardTreeFrame.init(paramsFrame);


            showResolutions = getComponent("hideResolutions")
            SplitPanel split = getComponent('split')
            showResolutions.addListener(new ValueListener() {
                @Override
                void valueChanged(Object source, String property, Object prevValue, Object value) {
                    boolean showResolutionsValue = BooleanUtils.isTrue(value)
                    int pos = (!showResolutionsValue ? 100 : 60)
                    split.setSplitPosition(pos);
                    split.setLocked(!showResolutionsValue)

                    def card = table.getSingleSelected()
                    if (showResolutionsValue && card != null) {
                        String currentTab = tabsheet.getTab().getName();
                        if (currentTab.equals("resolutionsTab"))
                            resolutionsFrame.setCard(card);
                        if (currentTab.equals("hierarchyTab"))
                            cardTreeFrame.setCard(card);
                    }
                }
            })

            def value = settings.get("hideResolutions").attributeValue("value")
            showResolutions.setValue(value == null ? false : BooleanUtils.toBoolean(value))
        }
        //    Map<Object, com.vaadin.ui.Component> fullDescrMap = new HashMap();
        ((com.vaadin.ui.Table) WebComponentsHelper.unwrap(table)).addGeneratedColumn(datasource.getMetaClass().getPropertyEx("fullDescr"),
                new com.vaadin.ui.Table.ColumnGenerator() {
                    @Override
                    Component generateCell(Table source, Object itemId, Object columnId) {
                        com.vaadin.ui.Component component// = fullDescrMap.get(itemId)
                        //                      if (component != null)
                        //                        return component

                        TaskRequest card = datasource.getItem(itemId);
                        String descr = card.getFullDescr()
                        int enterIdx = descr != null ? (descr.size() > 40 ? 40 : descr.indexOf('\n')) : -1;
                        if (enterIdx != -1) {
                            com.vaadin.ui.TextField content = new com.vaadin.ui.TextField(null, descr);
                            content.setReadOnly(true)
                            content.setWidth("300px")
                            content.setHeight("300px")
                            component = new com.vaadin.ui.PopupView("<span>" + descr.substring(0, enterIdx) + "...</span>", content)
                            component.addStyleName("longtext");
                        } else {
                            component = new com.vaadin.ui.Label(descr == null ? '' : descr)
                        }

                        component.setWidth("-1px")

//                      fullDescrMap.put(itemId, component)
                        return component
                    }
                });
    }
}
