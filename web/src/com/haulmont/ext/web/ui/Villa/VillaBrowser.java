package com.haulmont.ext.web.ui.Villa;

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


public class VillaBrowser extends AbstractLookup {
    @Inject
    protected Table villaTable;

    @Inject
    protected WebFilter genericFilter;

    @Inject
    protected Component verificationactsPanel;

    public VillaBrowser(IFrame frame) {
        super(frame);
    }

    @Override
    public void init(Map<String, Object> params) {
        super.init(params);
        villaTable.setMultiSelect(true);
        verificationactsPanel = getComponent("verificationactsPanel");
        if (verificationactsPanel != null) {
            WebButtonsPanel buttonsPanel = new WebButtonsPanel();
            buttonsPanel.add(verificationactsPanel);
            villaTable.setButtonsPanel(buttonsPanel);
            villaTable.setRowsCount(villaTable.getRowsCount());
        }

        ComponentsHelper.createActions(villaTable);
        villaTable.addAction(new RefreshAction(villaTable));
        villaTable.addAction(new ThesisExcelAction(villaTable,
                new WebExportDisplay(), genericFilter));
    }
}

