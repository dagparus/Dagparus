/*
 * Copyright (c) 2014 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.ext.web.ui.TreeCompany;

import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.PickerField;
import com.haulmont.cuba.gui.data.impl.CollectionDatasourceImpl;
import com.haulmont.cuba.gui.components.AbstractEditor;

import javax.inject.Inject;
import java.util.Map;

/**
 * Created by mahdi on 1/15/14.
 */
public class CompanyExtEditor extends AbstractEditor {
    @Inject
    private PickerField parentCompanyExt;
    protected CollectionDatasourceImpl extCompanyDs;

    @Override
    public void init(Map<String, Object> params) {
        super.init(params);
        initDep(params);
    }

    private void initDep(final Map<String, Object> params) {
        parentCompanyExt.removeAction(parentCompanyExt.getAction(PickerField.LookupAction.NAME));
        final PickerField.LookupAction parentCompanyExtLookupAction = new PickerField.LookupAction(parentCompanyExt) {
            @Override
            public void actionPerform(Component component) {
                super.actionPerform(component);

            }
        };
        parentCompanyExtLookupAction.setLookupScreen("ext$CompanyExt.lookup");
        parentCompanyExtLookupAction.setLookupScreenParams(params);
        parentCompanyExt.addAction(parentCompanyExtLookupAction);
    }
}
