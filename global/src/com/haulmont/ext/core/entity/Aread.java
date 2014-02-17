package com.haulmont.ext.core.entity;

import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.StandardEntity;


import javax.persistence.*;


@Entity(name = "ext$Aread")
@Table(name = "EXT_AREAD")
@NamePattern("%s|areadname")
public class Aread extends StandardEntity {


    @Column(name = "AREADNAME", length = 100)
    private String areadname;

    public String getAreadname() {
        return areadname;
    }

    public void setAreadname(String areadname) {
        this.areadname = areadname;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "REPUBLIC")
    protected Republic republic;


    public void setRepublic(Republic republic) {
        this.republic = republic;
    }

    public Republic getRepublic() {
        return republic;
    }
}
