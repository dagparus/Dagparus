package com.haulmont.ext.core.entity;

import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.docflow.core.entity.Company;

import javax.persistence.*;

@Entity(name="ext$CompanyExt")
@Table(name="EXT_COMPANY")
@PrimaryKeyJoinColumn(name = "COMPANY_ID",  referencedColumnName = "CONTRACTOR_ID")
@DiscriminatorValue("M")
@NamePattern("%s|name")
public class CompanyExt extends Company{

    @ManyToOne(fetch = FetchType.LAZY)
    @Column(name = "PARENT_COMPANY_ID")
    protected CompanyExt parentCompanyExt;

    @ManyToOne(fetch = FetchType.LAZY)
    @Column (name = "REPUBLIC_ID")
    protected Republic republic;

    @ManyToOne(fetch = FetchType.LAZY)
    @Column (name = "AREAD_ID")
    protected Aread aread;

    @ManyToOne(fetch = FetchType.LAZY)
    @Column(name = "VILLA_ID")
    protected Villa villa;

    public Villa getVilla() {
        return villa;
    }

    public void setVilla(Villa villa) {
        this.villa = villa;
    }

    public Republic getRepublic() {
        return republic;
    }

    public void setRepublic(Republic republic) {
        this.republic = republic;
    }

    public Aread getAread() {
        return aread;
    }

    public void setAread(Aread aread) {
        this.aread = aread;
    }

    public CompanyExt getParentCompanyExt() {
        return parentCompanyExt;
    }

    public void setParentCompanyExt(CompanyExt parentCompanyExt) {
        this.parentCompanyExt = parentCompanyExt;
    }
}
