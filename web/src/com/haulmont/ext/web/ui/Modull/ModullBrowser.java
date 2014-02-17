/*
 * Copyright (c) 2014 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.ext.web.ui.Modull;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.CommitContext;
import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.gui.ComponentsHelper;
import com.haulmont.cuba.gui.ServiceLocator;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.actions.ExcelAction;
import com.haulmont.cuba.gui.components.actions.ListActionType;
import com.haulmont.cuba.gui.components.actions.RefreshAction;
import com.haulmont.cuba.security.entity.EntityOp;
import com.haulmont.cuba.security.entity.Role;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.security.entity.UserRole;
import com.haulmont.cuba.web.filestorage.WebExportDisplay;
import com.haulmont.cuba.web.gui.components.WebButtonsPanel;
import com.haulmont.cuba.web.gui.components.WebFilter;
import com.haulmont.docflow.web.actions.ThesisExcelAction;
import com.haulmont.ext.core.entity.ExtModull;

import javax.inject.Inject;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Магомедзапир
 * Date: 24.01.14
 * Time: 12:36
 * To change this template use File | Settings | File Templates.
 */
public class ModullBrowser  extends AbstractLookup {

    @Inject
    protected Table modullTable;

    @Inject
    protected WebFilter genericFilter;

    @Inject
    protected Component verificationactsPanel;

    public ModullBrowser(IFrame frame) {
        super(frame);
    }

    @Override
    public void init(Map<String, Object> params) {
        super.init(params);
        modullTable.setMultiSelect(true);
        verificationactsPanel = getComponent("verificationactsPanel");
        if (verificationactsPanel != null) {
            WebButtonsPanel buttonsPanel = new WebButtonsPanel();
            buttonsPanel.add(verificationactsPanel);
            modullTable.setButtonsPanel(buttonsPanel);
            modullTable.setRowsCount(modullTable.getRowsCount());
        }

        ComponentsHelper.createActions(modullTable);
        modullTable.addAction(new RefreshAction(modullTable));
        modullTable.addAction(new ThesisExcelAction(modullTable,
                new WebExportDisplay(), genericFilter));
    }
}
