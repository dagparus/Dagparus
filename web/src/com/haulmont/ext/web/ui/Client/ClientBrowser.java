package com.haulmont.ext.web.ui.Client;


import com.haulmont.cuba.gui.ComponentsHelper;
import com.haulmont.cuba.gui.components.AbstractLookup;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.IFrame;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.components.actions.RefreshAction;
import com.haulmont.cuba.web.filestorage.WebExportDisplay;
import com.haulmont.cuba.web.gui.components.WebButtonsPanel;
import com.haulmont.cuba.web.gui.components.WebFilter;
import com.haulmont.docflow.web.actions.ThesisExcelAction;

import javax.inject.Inject;
import java.util.Map;

public class ClientBrowser extends AbstractLookup {

    @Inject
    protected Table clientTable;
    @Inject
    protected WebFilter genericFilter;
    @Inject
    protected Component verificationactsPanel;

    public ClientBrowser(IFrame frame) {
        super(frame);
    }
    @Override
    public void init(Map<String, Object> params) {
        super.init(params);
        clientTable.setMultiSelect(true);
        verificationactsPanel = getComponent("verificationactsPanel");
        if (verificationactsPanel != null) {
            WebButtonsPanel buttonsPanel = new WebButtonsPanel();
            buttonsPanel.add(verificationactsPanel);
            clientTable.setButtonsPanel(buttonsPanel);
            clientTable.setRowsCount(clientTable.getRowsCount());
        }
        ComponentsHelper.createActions(clientTable);
        clientTable.addAction(new RefreshAction(clientTable));
        clientTable.addAction(new ThesisExcelAction(clientTable,
                new WebExportDisplay(), genericFilter));
    }
}
