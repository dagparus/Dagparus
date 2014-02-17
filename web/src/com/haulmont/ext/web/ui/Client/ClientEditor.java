package com.haulmont.ext.web.ui.Client;


import com.haulmont.cuba.gui.components.AbstractEditor;
import com.haulmont.cuba.gui.components.OptionsGroup;
import com.haulmont.cuba.gui.components.TextField;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.impl.DsListenerAdapter;
import com.haulmont.ext.core.entity.ExtClient;
import com.haulmont.taskman.core.enums.SexEnum;

import javax.inject.Inject;
import java.util.Map;

public class ClientEditor extends AbstractEditor {

    protected OptionsGroup sexField;
    protected TextField nameField;
    protected TextField surnameCField;
    protected TextField nameCField;
    protected TextField patronymicCField;
    protected TextField surnameRField;
    protected TextField nameRField;
    protected TextField patronymicRField;

    @Inject
    protected Datasource ClientDs;

    @Override
    public void init(Map<String, Object> params) {
        super.init(params);

        ClientDs = getDsContext().get("ClientDs");
        sexField = getComponent("sex");
        nameField = getComponent("name");
        surnameCField = getComponent("surnameC");
        nameCField = getComponent("nameC");
        patronymicCField = getComponent("patronymicC");
        surnameRField = getComponent("surnameR");
        nameRField = getComponent("nameR");
        patronymicRField = getComponent("patronymicR");

        ClientDs.addListener(new DsListenerAdapter() {
            public void valueChanged(Object source, String property, Object prevValue, Object value) {

                ExtClient SexObjext = (ExtClient) source;
                SexEnum sexIn = SexObjext.getSex();
                String surnameCValue = surnameCField.getValue();
                String nameCValue = nameCField.getValue();
                String patronymicCValue = patronymicCField.getValue();
                String nameValue = nameField.getValue();
                if ("sex".equals(property) & (surnameCField.getValue() != null || nameCField.getValue() != null || patronymicCField.getValue() != null)) {
                    if (value == SexEnum.FEMALE) {
                        surnameRField.setValue(surnameCValue.substring(0, surnameCValue.length() - 1) + (char) 1086 + (char) 1081);
                        nameRField.setValue(nameCValue.substring(0, nameCValue.length() - 1) + (char) 1099);
                        patronymicRField.setValue(patronymicCValue.substring(0, patronymicCValue.length() - 1) + (char) 1099);

                    } else {
                        surnameRField.setValue(surnameCValue + (char) 1072);
                        nameRField.setValue(nameCValue + (char) 1072);
                        patronymicRField.setValue(patronymicCValue + (char) 1072);
                    }

                }
                if ("surnameC".equals(property)) {
                    if (sexIn == SexEnum.FEMALE) {
                        surnameRField.setValue(surnameCValue.substring(0, surnameCValue.length() - 1) + (char) 1086 + (char) 1081);
                        nameField.setValue(surnameCValue);
                    } else {
                        surnameRField.setValue(surnameCValue + (char) 1072);
                        nameField.setValue(surnameCValue);
                    }
                }
                if ("nameC".equals(property)) {
                    if (sexIn == SexEnum.FEMALE) {
                        nameRField.setValue(nameCValue.substring(0, nameCValue.length() - 1) + (char) 1099);
                        nameField.setValue(surnameCValue + " " + nameCValue.substring(0,1) + ".");
                    } else {
                        nameRField.setValue(nameCValue + (char) 1072);
                        nameField.setValue(surnameCValue + " " + nameCValue.substring(0,1) + ".");
                    }
                }
                if ("patronymicC".equals(property)) {
                    if (sexIn == SexEnum.FEMALE) {
                        patronymicRField.setValue(patronymicCValue.substring(0, patronymicCValue.length() - 1) + (char) 1099);
                        nameField.setValue(surnameCValue + " " + nameCValue.substring(0,1) + "." + patronymicCValue.substring(0,1) + ".");
                    } else {
                        patronymicRField.setValue(patronymicCValue + (char) 1072);
                        nameField.setValue(surnameCValue + " " + nameCValue.substring(0,1) + "." + patronymicCValue.substring(0,1) + ".");
                    }
                }
            }

        });
    }
}

