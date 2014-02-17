package com.haulmont.ext.core.entity;

import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.annotation.Listeners;

import com.haulmont.taskman.core.entity.Task;

import javax.persistence.*;

/**
 * Created with IntelliJ IDEA.
 * User: mahdi
 * Date: 11/1/13
 * Time: 3:22 PM
 * To change this template use File | Settings | File Templates.
 */

@Entity(name = "ext$TaskRequest")
@Table(name = "EXT_TASKREQUEST")
@PrimaryKeyJoinColumn(name="TASK_ID", referencedColumnName = "CARD_ID")
@DiscriminatorValue("44")
@Listeners("com.haulmont.taskman.core.listener.TaskEntityListener")
@NamePattern("%s|description")
public class TaskRequest extends Task {
    private static final long serialVersionUID = -5744655750586043224L;

    @Column(name = "NUMBER")
    protected String number;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CLIENT_ID")
    protected ExtClient extClient;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public ExtClient getExtClient() {
        return extClient;
    }

    public void setExtClient(ExtClient extClient) {
        this.extClient = extClient;
    }
}
