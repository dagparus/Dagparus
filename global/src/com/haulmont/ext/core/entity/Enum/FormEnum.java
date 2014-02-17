/*
 * Copyright (c) 2013 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.ext.core.entity.Enum;

import com.haulmont.chile.core.datatypes.impl.EnumClass;


public enum FormEnum implements EnumClass<String> {

    COMMERCIAL("C"),
    COUNTRY("G");

    private String id;
    private FormEnum(String id) {
        this.id = id;
    }
    public String getId() {
        return id;
    }

    public static FormEnum fromId(String id){
        if ("C".equals(id))
            return COMMERCIAL;
        else if("G".equals(id))
            return COUNTRY;
        else
            return null;
    }
}
