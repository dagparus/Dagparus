/*
 * Copyright (c) 2013 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.ext.web.ui.Call;

import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.cuba.core.app.DataService;
import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.core.global.MessageProvider;
import com.haulmont.cuba.core.global.UserSessionProvider;
import com.haulmont.cuba.gui.ServiceLocator;
import com.haulmont.cuba.gui.components.DatasourceComponent;
import com.haulmont.cuba.gui.components.Field;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.docflow.core.app.SignatureService;
import com.haulmont.docflow.core.entity.DocKind;
import com.haulmont.docflow.core.entity.FieldInfo;
import com.haulmont.ext.core.entity.Call;
import com.haulmont.ext.core.entity.Call;
import com.haulmont.workflow.core.app.WfUtils;
import com.haulmont.workflow.web.ui.base.AbstractWfAccessData;
import com.vaadin.data.util.TextFileProperty;
import org.apache.commons.lang.StringUtils;

import java.awt.*;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created by mahdi on 12/11/13.
 */
public class CallAccessData extends AbstractWfAccessData {

    protected Call call;
    protected List<FieldInfo> fieldInfos;

    public CallAccessData(Map<String, Object> params) {
        super(params);
        call = (Call) params.get("param$item");
        if (call == null)
            throw new IllegalStateException("Doc not found in param$item");

        DocKind docKind = null;
        if (call.getDocKind() == null) {
            LoadContext ctx = new LoadContext(DocKind.class);
            ctx.setQueryString("select dk from df$DocKind dk where dk.docType = " +
                    "(select dt from df$DocType dt where dt.name = :typeName)")
                    .addParameter("typeName", "ext$Call");
            ctx.setView("edit");
            DataService dataService = ServiceLocator.lookup(DataService.NAME);
            docKind = dataService.load(ctx);
        } else {
            docKind = call.getDocKind();
        }
        if (docKind != null) {
            fieldInfos = docKind.getFields();
        }
    }

    @Override
    public boolean getSaveEnabled() {
        return getNotVersion();
    }

    @Override
    public boolean getSaveAndCloseEnabled() {
        return getNotVersion();
    }

    @Override
    public boolean getStartProcessEnabled() {
        return false;
    }

    @Override
    public boolean getStartCardProcessEnabled() {
        return WfUtils.isCardInState((Call) item, null);
    }

    public boolean getNotVersion() {
        return call.getVersionOf() == null;
    }

    public boolean getGlobalEditable() {
        if (!getNotVersion()) {
            return false;
        }
        else {
            return true;
        }
    }

    @Override
    public boolean getRemoveCardProcessEnabled() {
        return Arrays.asList(UserSessionProvider.getUserSession().getRoles()).contains("invoice_initiator");
    }

    public boolean getAddCardProcessEnabled() {
        return Arrays.asList(UserSessionProvider.getUserSession().getRoles()).contains("invoice_initiator");
    }


    public void visitComponent(Component component, Collection<Component> components) {
        if (component instanceof DatasourceComponent) {
            Datasource datasource = ((DatasourceComponent) component).getDatasource();
            if (datasource != null && datasource.getMetaClass().equals((call).getMetaClass())) {
                MetaProperty property = ((DatasourceComponent) component).getMetaProperty();
                FieldInfo fieldInfo = findField(property);
                if (fieldInfo != null) {
                    Label label = findLabel(component, components);
                    if (!fieldInfo.getVisible()) {
                        component.setVisible(false);
                        if (label != null) {
                            label.setVisible(false);
                        }
                    }
                    if (fieldInfo.getRequired() && component instanceof Field) {
                        ((Field) component).setRequired(true);
                        if (label != null && label.getText() != null)
                            ((Field) component).setRequiredMessage(
                                    MessageProvider.getMessage(getClass(), "error.requiredValue") + " \"" + label.getText().toString() + "\"");
                    }
                    if (!StringUtils.isBlank(call.getSignatures()) && fieldInfo.getSigned() && component instanceof Field) {
                        ((Field) component).setEditable(false);
                    }
                }
                if (!StringUtils.isBlank(call.getSignatures()) && component instanceof Field) {
                    SignatureService ss = ServiceLocator.lookup(SignatureService.NAME);
                    boolean signed = ss.isPropertySigned(call, property);
                    if (((Field) component).isEditable()) {
                        ((Field) component).setEditable(!signed);
                    }
                }
            }
        }
    }

    private Label findLabel(Component component, Collection<Component> components) {
        String id = component.getName();
        if (id == null)
            return null;

        String labelId = id + "Label";
        for (Component c : components) {
            if (c instanceof Label && labelId.equals(c.getName())) {
                return (Label) c;
            }
        }
        return null;
    }

    private FieldInfo findField(MetaProperty property) {
        if (fieldInfos == null) return null;
        for (FieldInfo fieldInfo : fieldInfos) {
            if (fieldInfo.getName().equals(property.getName()))
                return fieldInfo;
        }
        return null;
    }


}