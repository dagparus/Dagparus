package com.haulmont.ext.core.entity;


import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.docflow.core.entity.Individual;
import com.haulmont.ext.core.entity.Enum.VillaEnum;

import javax.persistence.*;

@Entity(name = "ext$Villa")
@Table(name = "EXT_VILLA")
@NamePattern("%s|villaname")

public class Villa extends StandardEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "AREAD")
    protected Aread aread;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "REPUBLIC")
    protected Republic republic;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "INDIVIDUAL_ID")
    protected Individual individual;

    @Column(name = "VILLANAME", length = 100)
    private String villaname;

    @Column(name = "TYPE_VILLA", length = 1)
    protected String typeVilla = VillaEnum.VILLAGE.getId();

    @Column(name = "AREA_CENTER")
    private Boolean areaCenter;

    public VillaEnum getTypeVilla() {
        return VillaEnum.fromId(typeVilla);
    }

    public void setTypeVilla(VillaEnum typeVilla) {
        this.typeVilla = typeVilla == null ? null : typeVilla.getId();
    }

    public Boolean getAreaCenter() {
        return areaCenter;
    }

    public void setAreaCenter(Boolean areaCenter) {
        this.areaCenter = areaCenter;
    }

    public Individual getIndividual() {
        return individual;
    }

    public void setIndividual(Individual individual) {
        this.individual = individual;
    }

    public String getVillaname() {
        return villaname;
    }

    public void setVillaname(String villaname) {
        this.villaname = villaname;
    }

    public void setAread(Aread aread) {
        this.aread = aread;
    }

    public Aread getAread() {
        return aread;
    }

    public void setRepublic(Republic republic) {
        this.republic = republic;
    }

    public Republic getRepublic() {
        return republic;
    }


}