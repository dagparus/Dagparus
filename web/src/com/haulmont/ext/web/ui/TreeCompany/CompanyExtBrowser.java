/*
 * Copyright (c) 2014 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.ext.web.ui.TreeCompany;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.core.global.MessageProvider;
import com.haulmont.cuba.core.global.UserSessionProvider;
import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.actions.CreateAction;
import com.haulmont.cuba.gui.components.actions.EditAction;
import com.haulmont.cuba.gui.components.actions.RefreshAction;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.impl.CollectionDatasourceImpl;
import com.haulmont.cuba.gui.data.impl.CollectionDsListenerAdapter;
import com.haulmont.cuba.security.entity.EntityOp;
import com.haulmont.cuba.security.global.UserSession;
import com.haulmont.cuba.web.App;
import com.haulmont.cuba.web.gui.components.WebComponentsHelper;
import com.haulmont.docflow.web.ui.correspondencehistory.CorrespondenceHistoryFrame;
import com.haulmont.ext.core.entity.*;
import com.haulmont.ext.core.entity.CompanyExt;
import com.vaadin.ui.Select;

import javax.inject.Inject;
import java.util.*;

/**
 * Created by mahdi on 1/15/14.
 */
public class CompanyExtBrowser extends AbstractLookup {

    protected Table extClientTable;
    protected TreeTable extCompanysTable;
    protected CollectionDatasource extCompanysDs;
    protected CollectionDatasource republicDs, areadDs, villaDs;

    @Inject
    protected LookupField republic, aread, villa;
    GroupTable table;
    Boolean treeExpanded = false;

    public CompanyExtBrowser(IFrame frame){
        super(frame);
    }

    @Override
    public void init(Map<String, Object> params){
        super.init(params);

        table = getComponent("extClientTable");

        Button expandCollapseTrigger = (Button) getComponent("expandCollapseTrigger");
        if (expandCollapseTrigger != null)
            expandCollapseTrigger.setDescription(getMessage("expandCollapseTrigger"));

        addAction(new AbstractAction("expandCollapseTrigger") {
            @Override
            public void actionPerform(Component component) {
                if (treeExpanded)
                    table.collapseAll();
                else table.expandAll();
                treeExpanded = !treeExpanded;
            }
            @Override
            public String getCaption() {
                return "+/-";
            }
        });

        extCompanysDs = getDsContext().get("extCompanyDs");
        extCompanysTable = getComponent("extCompanysTable");


        republicDs = getDsContext().get("republicDs");
        areadDs = getDsContext().get("areadDs");
        villaDs = getDsContext().get("villaDs");

        republicDs.refresh();
        republicDs.addListener(new CollectionDsListenerAdapter(){
            @Override
            public void itemChanged(Datasource ds, Entity prevItem, Entity item) {
                refreshRDs();
                extCompanysTable.expandAll();
            }
        });



        ((Select) WebComponentsHelper.unwrap(republic)).setNullSelectionAllowed(false);

        if (republicDs.size() > 0) {
            Republic defaultRepublic = (Republic) republicDs.getItem(republicDs.getItemIds().iterator().next());
            if (defaultRepublic != null)
                defaultRepublic = (Republic) republicDs.getItem(republicDs.getItemIds().iterator().next());
            republic.setValue(defaultRepublic);
        }

        villaDs.setQuery("select v from ext$Villa v where v.republic.id= :ds$republicDs and v.aread.id is null order by v.villaname");
        villaDs.refresh();
        final CompanyExt[] companyExt = {null};
        areadDs.refresh();
        areadDs.addListener(new CollectionDsListenerAdapter(){
            @Override
            public void itemChanged(Datasource ds, Entity prevItem, Entity item) {
                refreshADs();
                extCompanysTable.expandAll();
                if (areadDs.getItem() == null) {
                    villaDs.setQuery("select v from ext$Villa v where v.republic.id= :ds$republicDs and v.aread.id is null order by v.villaname");
                    villaDs.refresh();
                } else {
                    villaDs.setQuery("select v from ext$Villa v where v.aread.areadname= '" +((Aread) areadDs.getItem()).getAreadname()+ "' order by v.villaname");
                    villaDs.refresh();
                }
            }
        });



        villaDs.addListener(new CollectionDsListenerAdapter(){
            @Override
            public void itemChanged(Datasource ds, Entity prevItem, Entity item) {
                refreshVDs();
                extCompanysTable.expandAll();
            }
        });

        extCompanysTable.addAction(new RefreshAction(extCompanysTable));

        extCompanysTable.addAction(new CreateAction(extCompanysTable, WindowManager.OpenType.DIALOG) {
            @Override
            public void actionPerform(Component component) {
                if (areadDs.getItem() != null && villaDs.getItem() == null) areadDs.refresh();
                else if (villaDs.getItem() != null) villaDs.refresh();
                else republicDs.refresh();
                if (republicDs.getItem() == null) {
                    showNotification(MessageProvider.getMessage(this.getClass(), "RepublicDoesNotExit"), IFrame.NotificationType.ERROR);
                    return;
                }
                super.actionPerform(component);
            }

            @Override
            protected void afterCommit(Entity entity) {
                refreshRDs();
                if (areadDs.getItem() != null) refreshADs();
                if (villaDs.getItem() != null) refreshVDs();
                CompanyExt parent = ((CompanyExt) entity).getParentCompanyExt();
                if (parent != null)
                    extCompanysTable.expand(parent.getId());
            }

            @Override
            public Map<String, Object> getInitialValues() {
                Map<String, Object> params = new HashMap<String, Object>();
                params.put("parentCompanyExt", extCompanysDs.getItem());
                params.put("republic", republicDs.getItem());
                if (areadDs.getItem() != null && villaDs.getItem() != null) {
                    params.put("aread", areadDs.getItem());
                    params.put("villa", villaDs.getItem());
                } else if (areadDs.getItem() != null) {
                    params.put("aread", areadDs.getItem());
                } else if (villaDs.getItem() != null) {
                    params.put("villa", villaDs.getItem());
                }
                return params;
            }

            @Override
            public Map<String, Object> getWindowParams() {
                Map<String, Object> params = new HashMap<String, Object>();
                params.put("param$republic", republicDs.getItem());
                if (areadDs.getItem() != null && villaDs.getItem() != null) {
                    params.put("param$aread", areadDs.getItem());
                    params.put("param$villa", villaDs.getItem());
                } else if (areadDs.getItem() != null) {
                    params.put("param$aread", areadDs.getItem());
                } else if (villaDs.getItem() != null) {
                    params.put("param$villa", villaDs.getItem());
                }
                params.put("visibleCompanyField", false);
                return params;
            }
        });
        extCompanysTable.addAction(new EditAction(extCompanysTable, WindowManager.OpenType.DIALOG) {
            @Override
            protected  void afterCommit(Entity entity) {
                refreshRDs();
                if (areadDs.getItem() !=null) refreshADs();
                if (villaDs.getItem() !=null) refreshVDs();
                CompanyExt parent = ((CompanyExt) entity).getParentCompanyExt();
                if (parent != null)
                    extCompanysTable.expand(parent.getId());
            }

            @Override
            public Map<String, Object> getWindowParams(){
                Map<String, Object> params = new HashMap<String, Object>();
                params.put("param$republic", republicDs.getItem());
                if (areadDs.getItem() != null && villaDs.getItem() != null) {
                    params.put("param$aread", areadDs.getItem());
                    params.put("param$villa", villaDs.getItem());
                } else if (areadDs.getItem() != null) {
                    params.put("param$aread", areadDs.getItem());
                } else if (villaDs.getItem() != null) {
                    params.put("param$villa", villaDs.getItem());
                }
                params.put("extCompany", extCompanysDs.getItem());
                params.put("visibleCompanyField", false);
                return params;
            }
        });
        extCompanysTable.addAction(new CompanyExtRemoveAction());

        extClientTable = getComponent("extClientTable");
        TableActionsHelper clientHelper = new TableActionsHelper(this, extClientTable);
        clientHelper.createCreateAction(new ValueProvider() {
            public Map<String, Object> getValues() {
                return Collections.<String, Object> singletonMap("extCompany", extCompanysDs.getItem());
            }

            public Map<String, Object> getParameters() {
                return null;
            }
        });
        clientHelper.createEditAction();

        extClientTable.addAction(new RemoveAction());
        extClientTable.addAction(new AbstractAction("add") {
            private UserSession userSession = UserSessionProvider.getUserSession();
            private MetaClass metaClass = extClientTable.getDatasource().getMetaClass();

            @Override
            public boolean isEnabled(){
                return super.isEnabled() && userSession.isEntityOpPermitted(metaClass, EntityOp.CREATE);
            }

            public void actionPerform(Component component) {
                final CompanyExt companyExt = extCompanysTable.getSingleSelected();
                if (companyExt == null){
                    showNotification(getMessage("selectCompany.msg"), IFrame.NotificationType.HUMANIZED);
                    return;
                }
                Map<String, Object> lookupParams = Collections.<String, Object>singletonMap("companyExtLookup", true);
                App.getInstance().getWindowManager().getDialogParams().setHeight(700);
                App.getInstance().getWindowManager().getDialogParams().setWidth(750);
                openLookup("ext$Client.browse", new Lookup.Handler() {
                    public void handleLookup(Collection items) {
                        CollectionDatasourceImpl extClientDs = (CollectionDatasourceImpl) extClientTable.getDatasource();
                        for (Object o : items){
                            ExtClient extClient = (ExtClient) o;
                            extClient.setExtCompany(companyExt);
                            extClientDs.modified(extClient);
                        }
                        extClientDs.commit();
                        extClientDs.refresh();
                    }
                },
                        WindowManager.OpenType.DIALOG, lookupParams);
            }

            @Override
            public String getCaption() {
                return getMessage("add");
            }
        });

        extClientTable.addAction(new AbstractAction("moveToCompany") {
            private UserSession userSession = UserSessionProvider.getUserSession();
            private MetaClass metaClass = extClientTable.getDatasource().getMetaClass();

            @Override
            public boolean isEnabled(){
                return super.isEnabled() && userSession.isEntityOpPermitted(metaClass, EntityOp.UPDATE);
            }

            public void actionPerform(Component component) {
                final ExtClient extClient = extClientTable.getSingleSelected();
                if (extClient == null) {
                    return;
                }
                App.getInstance().getWindowManager().getDialogParams().setWidth(750).setHeight(400);
                openLookup("ext$CompanyExt.lookup", new Window.Lookup.Handler(){
                    public  void handleLookup(Collection items) {
                        CompanyExt companyExt = (CompanyExt) items.iterator().next();
                        extClient.setExtCompany(companyExt);
                        extClientTable.getDatasource().commit();
                        extClientTable.getDatasource().refresh();
                    }
                },

                        WindowManager.OpenType.DIALOG, Collections.<String, Object>singletonMap("form", extCompanysDs.getItem()));
            }
            @Override
            public String getCaption() {
                return getMessage("moveToCompany");
            }
        });
        initLazyTabs();
    }

    private void initLazyTabs() {
        Tabsheet tabsheet = getComponent("tabsheet");
        tabsheet.addListener(new Tabsheet.TabChangeListener(){
            public void tabChanged(Tabsheet.Tab newTab) {
                String tabName = newTab.getName();
                if ("correspondenceHistoryTab".equals(newTab.getName())){
                    CorrespondenceHistoryFrame historyFrame = getComponent("correspondenceHistoryFrame");
                    historyFrame.init();
                    historyFrame.refreshDs();
                }
            }
        });
    }

    private void refreshRDs() {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("republic", republicDs.getItem());
        extCompanysDs.refresh(params);
    }

    private void refreshADs() {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("republic", republicDs.getItem());
        params.put("aread", areadDs.getItem());
        extCompanysDs.refresh(params);
    }

    private void refreshVDs() {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("republic", republicDs.getItem());
        if (areadDs.getItem() != null) params.put("aread", areadDs.getItem());
        params.put("villa", villaDs.getItem());
        extCompanysDs.refresh(params);
    }

    protected class RemoveAction extends AbstractAction{
        private UserSession userSession = UserSessionProvider.getUserSession();
        private MetaClass metaClass = extClientTable.getDatasource().getMetaClass();

        public  RemoveAction() {
            super("remove");
        }
        public String getCaption() {
            final  String messagesPackage = AppConfig.getMessagesPack();
            return MessageProvider.getMessage(messagesPackage, "actions.Remove");
        }
        @Override
        public boolean isEnabled() {
            return super.isEnabled() && userSession.isEntityOpPermitted(metaClass, EntityOp.DELETE);
        }

        public void actionPerform(Component component) {
            if (!isEnabled()) return;
            final Set selected = extClientTable.getSelected();
            if (!selected.isEmpty()) {
                final String messagesPackage = AppConfig.getMessagesPack();
                frame.showOptionDialog(
                        MessageProvider.getMessage(messagesPackage, "dialogs.Confirmation"),
                        MessageProvider.getMessage(messagesPackage, "dialogs.Confirmation.Remove"),
                        IFrame.MessageType.CONFIRMATION,
                        new DialogAction[]{
                                new DialogAction(DialogAction.Type.OK) {
                                    public void actionPerform(Component component) {
                                        CollectionDatasourceImpl ClientDs = (CollectionDatasourceImpl) extClientTable.getDatasource();
                                        for (Object item : selected) {
                                            ExtClient extClient = (ExtClient) item;
                                            extClient.setExtCompany(null);
                                        }
                                        ClientDs.commit();
                                        ClientDs.refresh();
                                    }
                                }, new DialogAction(DialogAction.Type.CANCEL) {
                        }
                        }
                );
            }
        }
    }

    protected class CompanyExtRemoveAction extends AbstractAction {
        private UserSession userSession = UserSessionProvider.getUserSession();
        private MetaClass metaClass = extCompanysTable.getDatasource().getMetaClass();

        public CompanyExtRemoveAction() {
            super("remove");
        }

        @Override
        public boolean isEnabled(){
            return super.isEnabled()&& userSession.isEntityOpPermitted(metaClass, EntityOp.DELETE);
        }
        public String getCaption(){
            final String messagesPackage = AppConfig.getMessagesPack();
            return MessageProvider.getMessage(messagesPackage, "actions.Remove");
        }
        public void actionPerform(Component component) {
            if (!isEnabled()) return;
            final Set selected = extCompanysTable.getSelected();
            if (!selected.isEmpty()){
                final CollectionDatasourceImpl extCompanysDs = (CollectionDatasourceImpl) extCompanysTable.getDatasource();
                for (Object item :selected) {
                    CompanyExt companyExt = (CompanyExt) item;
                    for (UUID uuid : (Collection<UUID>) extCompanysDs.getItemIds()) {
                        if (companyExt.equals(((CompanyExt) extCompanysDs.getItem(uuid)).getParentCompanyExt())){
                            frame.showNotification(frame.getMessage("notPossibleToRemoveCompany"), NotificationType.WARNING);
                            return;
                        }
                    }
                }
                if (!extClientTable.getDatasource().getItemIds().isEmpty()) {
                    frame.showNotification(frame.getMessage("notPossibleToRemoveCompany"), NotificationType.WARNING);
                    return;
                }
                final String messagesPackage = AppConfig.getMessagesPack();
                frame.showOptionDialog(
                        MessageProvider.getMessage(messagesPackage, "dialogs.Confirmation"),
                        MessageProvider.getMessage(messagesPackage, "dialogs.Confirmation.Remove"),
                        IFrame.MessageType.CONFIRMATION,
                        new DialogAction[]{
                                new DialogAction(DialogAction.Type.OK) {
                                    public void actionPerform(Component component) {
                                        for (Object item :selected) {
                                            extCompanysDs.removeItem((Entity) item);
                                        }
                                        try {
                                            extCompanysDs.commit();
                                        } finally {
                                            refreshRDs();
                                            if (areadDs.getItem() !=null) refreshADs();
                                            if (villaDs.getItem() !=null) refreshVDs();
                                        }
                                    }
                                }, new DialogAction(DialogAction.Type.CANCEL){
                        }
                        }
                );

            }
        }
    }

}
