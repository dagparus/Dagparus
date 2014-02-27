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
    CONTRACT_ANTI ("CA");
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
        else return null;
    }
}
