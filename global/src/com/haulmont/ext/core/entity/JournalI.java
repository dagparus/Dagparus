/*
 * Copyright (c) 2013 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.ext.core.entity;

import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.StandardEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

@Entity(name = "ext$JournalI")
@Table(name = "EXT_JOURNALI")
@NamePattern("%s|dateJI")
public class JournalI extends StandardEntity {

    @Column(name = "DATE_JI")
    private Date dateJI;

    @Column(name = "WHOM", length = 100)
    private String whom;

    @Column(name = "CONTENTI", length = 100)
    private String contentI;

    @Column(name = "CONTACT_JI", length = 50)
    private String contactJI;

    @Column(name = "STATUSI", length = 100)
    private String statusI;

    @Column(name = "NUMBER_JI", length = 20)
    private String numberJI;

    public Date getDateJI() {
        return dateJI;
    }

    public void setDateJI(Date dateJI) {
        this.dateJI = dateJI;
    }

    public String getWhom() {
        return whom;
    }

    public void setWhom(String whom) {
        this.whom = whom;
    }

    public String getContentI() {
        return contentI;
    }

    public void setContentI(String contentI) {
        this.contentI = contentI;
    }

    public String getContactJI() {
        return contactJI;
    }

    public void setContactJI(String contactJI) {
        this.contactJI = contactJI;
    }

    public String getStatusI() {
        return statusI;
    }

    public void setStatusI(String statusI) {
        this.statusI = statusI;
    }


    public String getNumberJI() {
        return numberJI;
    }

    public void setNumberJI(String numberJI) {
        this.numberJI = numberJI;
    }


}
