package com.haulmont.ext.web.ui.Head;


import com.haulmont.cuba.gui.components.AbstractEditor;
import com.haulmont.cuba.gui.components.OptionsGroup;
import com.haulmont.cuba.gui.components.TextField;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.impl.DsListenerAdapter;
import com.haulmont.ext.core.entity.ExtHead;
import com.haulmont.taskman.core.enums.SexEnum;

import javax.inject.Inject;
import java.util.Map;

public class HeadEditor extends AbstractEditor {

    protected OptionsGroup sexField;
    protected TextField nameField;
    protected TextField surnameField;
    protected TextField namehField;
    protected TextField patronymicField;
    protected TextField surnameRField;
    protected TextField nameRField;
    protected TextField patronymicRField;

    @Inject
    protected Datasource HeadDs;

    @Override
    public void init(Map<String, Object> params) {
        super.init(params);

        HeadDs = getDsContext().get("HeadDs");
        sexField = getComponent("sex");
        nameField = getComponent("name");
        surnameField = getComponent("surname");
        namehField = getComponent("nameh");
        patronymicField = getComponent("patronymic");
        surnameRField = getComponent("surnameR");
        nameRField = getComponent("nameR");
        patronymicRField = getComponent("patronymicR");

        HeadDs.addListener(new DsListenerAdapter()
        {   public void valueChanged(Object source, String property, Object prevValue, Object value) {

                ExtHead SexObjext = (ExtHead) source;
                SexEnum sexIn = SexObjext.getSex();
                String surnameValue = surnameField.getValue();
                String namehValue = namehField.getValue();
                String patronymicValue = patronymicField.getValue();
                String nameValue = nameField.getValue();

                if ("sex".equals(property) & (surnameField.getValue() != null || namehField.getValue() != null || patronymicField.getValue() != null)) {
                    if (value == SexEnum.FEMALE) {
                        surnameRField.setValue(surnameValue.substring(0,surnameValue.length()-1) + (char)1086 + (char)1081);
                        nameRField.setValue(namehValue.substring(0,namehValue.length()-1) + (char)1099);
                        patronymicRField.setValue(patronymicValue.substring(0,patronymicValue.length()-1) + (char)1099);
                    }
                    else {
                        surnameRField.setValue(surnameValue + (char)1072);
                        nameRField.setValue(namehValue + (char)1072);
                        patronymicRField.setValue(patronymicValue + (char)1072);
                    }
                }
                if ("surname".equals(property)) {
                    if (sexIn == SexEnum.FEMALE) {
                        surnameRField.setValue(surnameValue.substring(0,surnameValue.length()-1) + (char)1086 + (char)1081);
                        nameField.setValue(surnameValue);
                    } else {
                        surnameRField.setValue(surnameValue + (char)1072);
                        nameField.setValue(surnameValue);
                    }
                }
                if ("nameh".equals(property)) {
                    if (sexIn  == SexEnum.FEMALE) {
                        nameRField.setValue(namehValue.substring(0, namehValue.length() - 1) + (char) 1099);
                        nameField.setValue(surnameValue + " " + namehValue.substring(0,1) + ".");
                    } else {
                        nameRField.setValue(namehValue + (char) 1072);
                        nameField.setValue(surnameValue + " " + namehValue.substring(0,1) + ".");
                    }
                }
                if ("patronymic".equals(property)) {
                    if (sexIn  == SexEnum.FEMALE) {
                        patronymicRField.setValue(patronymicValue.substring(0, patronymicValue.length() - 1) + (char) 1099);
                        nameField.setValue(surnameValue + " " + namehValue.substring(0,1) + "." + patronymicValue.substring(0,1) + ".");
                    } else {
                        patronymicRField.setValue(patronymicValue + (char) 1072);
                        nameField.setValue(surnameValue + " " + namehValue.substring(0,1) + "." + patronymicValue.substring(0,1) + ".");
                    }
                }
            }

        });
    }
}
