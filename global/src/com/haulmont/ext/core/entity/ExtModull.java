/*
 * Copyright (c) 2014 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.ext.core.entity;

import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.entity.annotation.EnableRestore;
import com.haulmont.ext.core.entity.Enum.ContractDocType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity(name = "ext$Modull")
@Table(name = "EXT_MODULL")
@NamePattern("%s|name")
@EnableRestore
public class ExtModull extends StandardEntity{

    private static final long serialVersionUID = -4789116218059626402L;

    @Column (name = "NAME")
    private String name;

    @Column (name = "NAMECONTRACT")
    private String nameContract = ContractDocType.LICENSE_SERVICE.getId();

    @Column (name = "PRICE")
    private String price;

    public ContractDocType getNameContract() {
        return ContractDocType.fromId(nameContract);
    }

    public void setNameContract(ContractDocType nameContract) {
        this.nameContract = nameContract == null ? null : nameContract.getId();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
