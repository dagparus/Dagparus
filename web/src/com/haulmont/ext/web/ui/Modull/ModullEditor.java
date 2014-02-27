/*
 * Copyright (c) 2014 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.ext.web.ui.Modull;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.gui.components.AbstractEditor;
import com.haulmont.cuba.gui.components.IFrame;
import com.haulmont.cuba.gui.components.TextField;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.impl.DsListenerAdapter;

import javax.inject.Inject;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Магомедзапир
 * Date: 24.01.14
 * Time: 12:36
 * To change this template use File | Settings | File Templates.
 */
public class ModullEditor extends AbstractEditor{

    public ModullEditor(IFrame frame){
        super(frame);
    }
    @Override
    public void setItem(Entity item) {
        super.setItem(item);
    }

    @Override
    public void init(Map<String, Object> params){
        super.init(params);
     }
}
