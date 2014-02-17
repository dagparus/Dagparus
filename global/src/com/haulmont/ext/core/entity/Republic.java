package com.haulmont.ext.core.entity;

import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.StandardEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity(name = "ext$Republic")
@Table(name = "EXT_REPUBLIC")
@NamePattern("%s|regionname")
public class Republic extends StandardEntity{

    @Column(name = "REGIONCODE", length = 5)
    private String regioncode;

    @Column(name = "REGIONNAME", length = 50)
    private String regionname;

    public String getRegionname() {
        return regionname;
    }

    public void setRegionname(String regionname) {
        this.regionname = regionname;
    }

    public String getRegioncode() {
        return regioncode;
    }

    public void setRegioncode(String regioncode) {
        this.regioncode = regioncode;
    }

}
