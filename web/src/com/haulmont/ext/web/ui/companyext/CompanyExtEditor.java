/*
 * Copyright (c) 2010 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Maxim Gorbunkov
 * Created: 22.03.2010 12:01:11
 *
 * $Id: CompanyEditor.java 4358 2012-03-23 10:34:53Z zudin $
 */
package com.haulmont.ext.web.ui.companyext;

import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.impl.CollectionDatasourceImpl;
import com.haulmont.cuba.gui.data.impl.CollectionDsListenerAdapter;
import com.haulmont.cuba.gui.data.impl.CollectionPropertyDatasourceImpl;
import com.haulmont.cuba.gui.data.impl.DsListenerAdapter;
import com.haulmont.cuba.gui.settings.Settings;
import com.haulmont.docflow.web.ui.account.AccountsFrame;
import com.haulmont.docflow.web.ui.contractor.ContractorAttachmentsFrame;
import com.haulmont.docflow.web.ui.contractor.ContractorLogFrame;
import com.haulmont.docflow.web.ui.correspondencehistory.CorrespondenceHistoryFrame;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class CompanyExtEditor extends AbstractEditor {
    private java.util.List<String> initializedTabs = new ArrayList<String>();
    private Datasource companyDs;
    protected CollectionPropertyDatasourceImpl attachmentsDs;
    @Inject
    private PickerField parentCompanyExt;
    private CollectionDatasource republicDs;

    public CompanyExtEditor(IFrame frame) {
        super(frame);
    }

    @Override
    public void init(Map<String, Object> params) {
        super.init(params);
        initDep(params);
        republicDs = getDsContext().get("republicDs");
        companyDs = getDsContext().get("companyDs");
        Table contactPersonsTable = getComponent("contactPersonsTable");
        TableActionsHelper helper = new TableActionsHelper(this, contactPersonsTable);
        helper.createCreateAction(new ValueProvider() {
            public Map<String, Object> getValues() {
                return Collections.<String, Object>singletonMap("company", companyDs.getItem());
            }

            public Map<String, Object> getParameters() {
                return null;
            }
        }, WindowManager.OpenType.DIALOG);
        helper.createEditAction(WindowManager.OpenType.DIALOG);
        helper.createRemoveAction(false);
      /*  companyDs.addListener(new DsListenerAdapter() {
            public void valueChanged(Object source, String property, Object prevValue, Object value) {

                if ("republic".equals(property)) {
                    params.put("param$republic", republic.getValue());
                    params.put("visibleTypeCompanyField", false);
                }
            }
        });*/
        attachmentsDs = getDsContext().get("attachmentsDs");
        attachmentsDs.addListener(new CollectionDsListenerAdapter() {
            @Override
            public void collectionChanged(CollectionDatasource ds, Operation operation) {
                for (Tabsheet.Tab tab : ((Tabsheet) getComponent("tabsheet")).getTabs()) {
                    if (tab.getName() != null && tab.getName().equals("attachmentsTab")) {
                        if(ds.getItemIds().size()>0)
                            tab.setCaption(getMessage("attachmentsTab") + " (" + ds.getItemIds().size() + ")");
                        else
                            tab.setCaption(getMessage("attachmentsTab"));
                    }
                }
            }
        });


        initLazyTabs();
        initDialogParams();
    }

    private void initLazyTabs() {
        Tabsheet tabsheet = getComponent("tabsheet");
        tabsheet.addListener(new Tabsheet.TabChangeListener() {
            public void tabChanged(Tabsheet.Tab newTab) {
                String tabName = newTab.getName();
                if ("attachmentsTab".equals(tabName) && !initializedTabs.contains(tabName)) {
                    ContractorAttachmentsFrame attachmentsFrame = getComponent("attachmentsFrame");
                    attachmentsFrame.setContractorDs(companyDs);
                    attachmentsFrame.init();
                    initializedTabs.add(tabName);
                } else if ("accountsTab".equals(tabName) && !initializedTabs.contains(tabName)) {
                    AccountsFrame accountsFrame = getComponent("accountsFrame");
                    accountsFrame.setPropertyName("contractor");
                    accountsFrame.setParentDs(companyDs);
                    accountsFrame.init();
                    initializedTabs.add(tabName);
                }  else if ("correspondenceHistoryTab".equals(newTab.getName())) {
                    CorrespondenceHistoryFrame historyFrame = getComponent("correspondenceHistoryFrame");
                    historyFrame.init();
                    historyFrame.refreshDs();
                } else if ("contractorLogTab".equals(newTab.getName())) {
                    ContractorLogFrame contractorLogTab = getComponent("contractorLogFrame");
                    if (contractorLogTab != null) {
                        Map<String, Object> param = new HashMap<String, Object>();
                        param.put("companyDs", companyDs.getItem());
                        contractorLogTab.init(param);
                        contractorLogTab.refresh(param);
                    }
                }
            }
        });
    }

    private void initDep(final Map<String, Object> params) {
        parentCompanyExt.removeAction(parentCompanyExt.getAction(PickerField.LookupAction.NAME));
        final PickerField.LookupAction parentCompanyExtLookupAction = new PickerField.LookupAction(parentCompanyExt) {
            @Override
            public void actionPerform(Component component) {
                params.put("param$republic", republicDs.getItem());
                params.put("visibleTypeCompanyField", false);
                params.put("extCompany", companyDs.getItem());
                super.actionPerform(component);
            }
        };
        parentCompanyExtLookupAction.setLookupScreen("ext$CompanyExt.lookup");
        parentCompanyExtLookupAction.setLookupScreenParams(params);
        parentCompanyExt.addAction(parentCompanyExtLookupAction);
    }

    @Override
    public void applySettings(Settings settings) {
        super.applySettings(settings);
        getDialogParams().reset();
    }

    protected void initDialogParams() {
        getDialogParams().setWidth(690);
        getDialogParams().setHeight(780);
    }
}
