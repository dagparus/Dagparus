/*
 * Copyright (c) 2014 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.ext.core.entity;

import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.entity.annotation.EnableRestore;

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
    private String nameContract;

    @Column (name = "TOTAL")
    private String total;

    @Column (name = "COUNT")
    private String count;

    @Column (name = "PRICE")
    private String price;

    @Column (name = "TOTAL_COST")
    private String totalCost;

    public String getNameContract() {
        return nameContract;
    }

    public void setNameContract(String nameContract) {
        this.nameContract = nameContract;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(String totalCost) {
        this.totalCost = totalCost;
    }
}
