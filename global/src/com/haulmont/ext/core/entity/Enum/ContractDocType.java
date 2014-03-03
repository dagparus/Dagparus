/*
 * Copyright (c) 2014 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.ext.core.entity.Enum;

import com.haulmont.chile.core.datatypes.impl.EnumClass;

/**
 * Created by mahdi on 2/16/14.
 */
public enum ContractDocType implements EnumClass<String> {

    LICENSE_SERVICE ("LS"),
    PROVIDE_SERVICE ("PS"),
    PRODUCT_DELIVERY ("PD"),
    CONTRACT_ANTI ("CA"),
    CONTRACT_ASSIGINT ("CAS"),
    CONTRACT_LO_ANTI ("CLA");
    private String id;
    private ContractDocType(String id) {
        this.id = id;
    }

    @Override
    public String getId() {
        return id;
    }

    public static ContractDocType fromId(String id) {
        if ("LS".equals(id))
            return LICENSE_SERVICE;
        else if ("PS".equals(id))
            return PROVIDE_SERVICE;
        else if ("PD".equals(id))
            return PRODUCT_DELIVERY;
        else if ("CA".equals(id))
            return CONTRACT_ANTI;
        else if ("CLA".equals(id))
            return CONTRACT_LO_ANTI;
        else if ("CAS".equals(id))
            return CONTRACT_ASSIGINT;
        else return null;
    }
}
