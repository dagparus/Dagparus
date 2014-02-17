package com.haulmont.ext.core.entity;


import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.docflow.core.entity.Company;

import javax.persistence.*;

@Entity(name = "ext$Responsible")
@Table(name = "EXT_RESPONSIBLE")

@NamePattern("%s|area, village, responsible")

public class Responsible extends StandardEntity{

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "REPUBLIC_ID")
    private Republic republic;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "AREAD_ID")
    private Aread aread;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "VILLA_ID")
    private Villa villa;

    @Column(name = "RESPONSIBLE", length = 100)
    private String responsible;

    @Column(name = "NUMBER_TEL", length = 20)
    private String numberTel;

    @Column(name = "POSTAL_ADRESS", length = 200)
    private String postalAdress;

    @Column(name = "POST", length = 100)
    private String post;

    @Column(name = "FAXS", length = 20)
    private String faxs;

    @Column(name = "E_MAIL", length = 100)
    private String eMail;

    public String geteMail() {
        return eMail;
    }

    public void seteMail(String eMail) {
        this.eMail = eMail;
    }

    public String getPostalAdress() {
        return postalAdress;
    }


    public void setPostalAdress(String postalAdress) {
        this.postalAdress = postalAdress;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public String getFaxs() {
        return faxs;
    }

    public void setFaxs(String faxs) {
        this.faxs = faxs;
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

    public Villa getVilla() {
        return villa;
    }

    public void setVilla(Villa villa) {
        this.villa = villa;
    }

    public String getResponsible() {
        return responsible;
    }

    public void setResponsible(String responsible) {
        this.responsible = responsible;
    }

    public String getNumberTel() {
        return numberTel;
    }

    public void setNumberTel(String numberTel) {
        this.numberTel = numberTel;
    }
}
