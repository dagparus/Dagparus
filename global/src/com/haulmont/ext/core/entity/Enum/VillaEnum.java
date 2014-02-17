/*
 * Copyright (c) 2014 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.ext.core.entity.Enum;

import com.haulmont.chile.core.datatypes.impl.EnumClass;

/**
 * Created by mahdi on 1/15/14.
 */
public enum VillaEnum implements EnumClass<String> {
    CITY("C"),
    VILLAGE("V");

    private String id;

    VillaEnum(String id) {
        this.id = id;
    }

    @Override
    public String getId() {
        return id;
    }

    public static VillaEnum fromId(String id){
        if("C".equals(id))
            return CITY;
        else if("V".equals(id))
            return VILLAGE;
        else
            return null;
    }
}
