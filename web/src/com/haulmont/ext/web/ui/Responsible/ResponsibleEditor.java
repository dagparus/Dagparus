
package com.haulmont.ext.web.ui.Responsible;

import com.haulmont.cuba.gui.components.AbstractEditor;
import com.haulmont.cuba.gui.data.Datasource;

import javax.inject.Inject;
import java.util.Map;

public class ResponsibleEditor extends AbstractEditor {
    @Inject
    protected Datasource ResponsibleDs;

    @Override
    public void init(Map<String, Object> params) {
        super.init(params);
    }
}

