
package com.haulmont.ext.web.ui.Villa;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.impl.DsListenerAdapter;
import com.haulmont.ext.core.entity.Aread;
import com.haulmont.ext.core.entity.Enum.VillaEnum;
import org.apache.commons.lang.ObjectUtils;

import javax.inject.Inject;
import java.util.Map;


public class VillaEditor extends AbstractEditor {

    @Inject
    Datasource villaDs;

    ActionsField aread;
    OptionsGroup typeVilla;
    CheckBox areaCenter;

    transient Aread newAread;

    public VillaEditor(IFrame frame) {
        super(frame);
    }

    @Override
    public void setItem(Entity item) {
        super.setItem(item);
    }

    @Override
    public void init(Map<String, Object> params) {
        super.init(params);

        villaDs = getDsContext().get("villaDs");
        aread = getComponent("aread");
        typeVilla = getComponent("typeVilla");
        areaCenter = getComponent("areaCenter");

        villaDs.addListener(new DsListenerAdapter() {
            public void valueChanged(Object source, String property, Object prevValue, Object value) {
                if ("typeVilla".equals(property)) {
                    if (value == VillaEnum.CITY) {
                        aread.setRequired(false);
                        aread.setEnabled(false);
                        aread.setValue(null);
                        areaCenter.setEnabled(false);
                        areaCenter.setValue(false);
                    } else {
                        aread.setRequired(true);
                        aread.setEnabled(true);
                        areaCenter.setEnabled(true);
                    }
                }
            }
        });
    }
}
