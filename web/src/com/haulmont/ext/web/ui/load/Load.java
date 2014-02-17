/*
 * Copyright (c) 2013 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.ext.web.ui.load;

import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.web.gui.components.WebButton;
import com.haulmont.cuba.web.gui.components.WebVBoxLayout;
import com.haulmont.ext.core.app.LoadClientService;

import javax.inject.Inject;
import java.util.Map;

/**
 * Created by mahdi on 12/19/13.
 */
public class Load  extends AbstractLookup {

    @Inject
    protected WebVBoxLayout importRecord;

    @Inject
    protected LoadClientService lCS;

    private TextField pathToCSV;

    @Override
    public void init(Map<String, Object> params) {
        super.init(params);

        pathToCSV = getComponent("pathToCSV");

        Button buttonImport = new WebButton();
        buttonImport.setWidth("200px");
        buttonImport.setAction(new ImportAction());
        importRecord.add(buttonImport);
    }

    protected class ImportAction extends AbstractAction {

        protected ImportAction() {
            super("importRecord");
        }

        @Override
        public void actionPerform(Component component) {
            try {
                String path = (String)pathToCSV.getValue();
                lCS.loadFromFile(path);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}

