/*
 * Copyright (c) 2013 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.ext.core.entity.Enum;

import com.haulmont.chile.core.datatypes.impl.EnumClass;


public enum NameOrganisationEnum implements EnumClass<String> {

    PARUS("P"),
    INFORM("I");

    private String id;
    private NameOrganisationEnum(String id) {
        this.id = id;
    }
    public String getId() {
        return id;
    }

    public static NameOrganisationEnum fromId(String id){
        if ("P".equals(id))
            return PARUS;
        else if("I".equals(id))
            return INFORM;
        else
            return null;
    }
}
