/*
 * Copyright (c) 2013 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.ext.core.entity;

import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.annotation.Listeners;
import com.haulmont.docflow.core.entity.Doc;
import com.haulmont.taskman.core.entity.Priority;
import com.haulmont.workflow.core.entity.Card;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by mahdi on 12/11/13.
 * FREEEZE PROJECT!!!
 */

@Entity(name = "ext$Call")
@Table(name = "EXT_CALL")
@DiscriminatorValue("150")
@PrimaryKeyJoinColumn(name = "CARD_ID", referencedColumnName = "CARD_ID")
@Listeners("com.haulmont.docflow.core.listeners.DocEntityListener")
@NamePattern("%s|name")
public class Call extends Doc {
    private static final long serialVersionUID = -688305569098915083L;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CLIENT_ID")
    protected ExtClient extClient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PRIORITY_ID")
    protected Priority priority;

    @Column (name ="TELEPHONE_NUMBER")
    private String telephoneNumber;

    @Column (name = "NAME")
    private String name;

    @Column (name = "DESCRIPTION")
    private String callDescription;

    @Column (name = "FINISH_DATE_PLAN")
    private Date finishDatePlan;

    @Column(name = "REFUSE_ENABLED")
    private Boolean refuseEnabled;

    @Column(name = "REASSIGN_ENABLED")
    private Boolean reassignEnabled;

    @Column(name = "CONFIRM_REQUIRED")
    private Boolean confirmRequired;

    public ExtClient getExtClient() {
        return extClient;
    }

    public void setExtClient(ExtClient extClient) {
        this.extClient = extClient;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public String getTelephoneNumber() {
        return telephoneNumber;
    }

    public void setTelephoneNumber(String telephoneNumber) {
        this.telephoneNumber = telephoneNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCallDescription() {
        return callDescription;
    }

    public void setCallDescription(String callDescription) {
        this.callDescription = callDescription;
    }

    public Date getFinishDatePlan() {
        return finishDatePlan;
    }

    public void setFinishDatePlan(Date finishDatePlan) {
        this.finishDatePlan = finishDatePlan;
    }

    public Boolean getRefuseEnabled() {
        return refuseEnabled;
    }

    public void setRefuseEnabled(Boolean refuseEnabled) {
        this.refuseEnabled = refuseEnabled;
    }

    public Boolean getReassignEnabled() {
        return reassignEnabled;
    }

    public void setReassignEnabled(Boolean reassignEnabled) {
        this.reassignEnabled = reassignEnabled;
    }

    public Boolean getConfirmRequired() {
        return confirmRequired;
    }

    public void setConfirmRequired(Boolean confirmRequired) {
        this.confirmRequired = confirmRequired;
    }
}
