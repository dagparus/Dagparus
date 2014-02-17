
package com.haulmont.ext.web.ui.Aread;


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

public class AreadBrowser extends AbstractLookup {
    @Inject
    protected Table areadTable;

    @Inject
    protected WebFilter genericFilter;

    @Inject
    protected Component verificationactsPanel;

    public AreadBrowser(IFrame frame) {
        super(frame);
    }

    @Override
    public void init(Map<String, Object> params) {
        super.init(params);
        areadTable.setMultiSelect(true);
        verificationactsPanel = getComponent("verificationactsPanel");
        if (verificationactsPanel != null) {
            WebButtonsPanel buttonsPanel = new WebButtonsPanel();
            buttonsPanel.add(verificationactsPanel);
            areadTable.setButtonsPanel(buttonsPanel);
            areadTable.setRowsCount(areadTable.getRowsCount());
        }

        ComponentsHelper.createActions(areadTable);
        areadTable.addAction(new RefreshAction(areadTable));
        areadTable.addAction(new ThesisExcelAction(areadTable,
                new WebExportDisplay(), genericFilter));
    }

}
