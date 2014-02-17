
package com.haulmont.ext.web.ui.Head;

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


public class HeadBrowser extends AbstractLookup {

    @Inject
    protected Table headTable;
    @Inject
    protected WebFilter genericFilter;
    @Inject
    protected Component verificationactsPanel;

    public HeadBrowser(IFrame frame) {
        super(frame);
    }
    @Override
    public void init(Map<String, Object> params) {
        super.init(params);
        verificationactsPanel = getComponent("verificationactsPanel");
        if (verificationactsPanel != null) {
            WebButtonsPanel buttonsPanel = new WebButtonsPanel();
            buttonsPanel.add(verificationactsPanel);
            headTable.setButtonsPanel(buttonsPanel);
            headTable.setRowsCount(headTable.getRowsCount());
        }
        ComponentsHelper.createActions(headTable);
        headTable.addAction(new RefreshAction(headTable));
        headTable.addAction(new ThesisExcelAction(headTable,
                  new WebExportDisplay(), genericFilter));
    }
}