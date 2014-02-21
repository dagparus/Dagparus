package com.haulmont.ext.core.entity;

import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.docflow.core.entity.Company;
import com.haulmont.docflow.core.entity.Organization;
import com.haulmont.taskman.core.enums.SexEnum;

import javax.persistence.*;
import java.util.Date;

@Entity(name = "ext$Client")
@Table(name = "EXT_CLIENT")
@NamePattern("%s|name")
public class ExtClient extends StandardEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "COMPANY_ID")
    protected CompanyExt extCompany;

    @Column(name = "POST",length = 100)
    private String post;

    @Column(name = "LEGAL_ADDRESS", length = 200)
    private String legalAddress;

    @Column(name = "SURNAME_C", length = 100)
    private String surnameC;

    @Column(name = "NAME_C", length = 60)
    private String nameC;

    @Column(name = "PATRONYMIC_C", length = 100)
    private String patronymicC;

    @Column(name = "DATE_BIRTH")
    private Date dateBirth;

    @Column(name = "PLACE_BIRTH", length = 200)
    private String placeBirth;

    @Column(name = "NAME_CW", length = 100)
    private String nameCw;

    @Column(name = "KPP", length = 100)
    private String kpp;

    @Column(name = "COUNT_CHS", length = 20)
    private String countChs;

    @Column(name = "BANK", length = 200)
    private String bank;

    @Column(name = "INN", length = 100)
    private String inn;

    @Column(name = "NUMBER_TW", length = 20)
    private String numberTw;

    @Column(name = "NUMBER_TEL", length = 20)
    private String numberTel;

    @Column(name = "ADDRESS", length = 200)
    private String address;

    @Column(name = "LS", length = 100)
    private String ls;

    @Column(name = "BIK", length = 100)
    private String bik;

    @Column(name = "KS", length = 100)
    private String ks;

    @Column(name = "RS", length = 100)
    private String rs;

    @Column(name = "SURNAME_R", length = 100)
    private String surnameR;

    @Column(name = "NAME_R", length = 60)
    private String nameR;

    @Column(name = "PATRONYMIC_R", length = 100)
    private String patronymicR;

    @Column(name = "SEX", length=10)
    private String sex = SexEnum.MALE.getId();

    @Column(name = "NAME", length = 100)
    private String name;

    @Column(name = "TREASURYDEPARTMENT", length = 100)
    private String treasuryDepartment;

    @Column(name = "POST_R", length = 100)
    private String postR;

    public String getTreasuryDepartment() {
        return treasuryDepartment;
    }

    public void setTreasuryDepartment(String treasuryDepartment) {
        this.treasuryDepartment = treasuryDepartment;
    }

    public String getPostR() {
        return postR;
    }

    public void setPostR(String postR) {
        this.postR = postR;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SexEnum getSex() {
        return SexEnum.fromId(sex);
    }

    public void setSex(SexEnum sex) {
        this.sex = sex == null ? null : sex.getId();
    }

    public CompanyExt getExtCompany() {
        return extCompany;
    }

    public void setExtCompany(CompanyExt extCompany) {
        this.extCompany = extCompany;
    }

    public String getSurnameR() {
        return surnameR;
    }

    public void setSurnameR(String surnameR) {
        this.surnameR = surnameR;
    }

    public String getNameR() {
        return nameR;
    }

    public void setNameR(String nameR) {
        this.nameR = nameR;
    }

    public String getPatronymicR() {
        return patronymicR;
    }

    public void setPatronymicR(String patronymicR) {
        this.patronymicR = patronymicR;
    }

    public String getNumberTw() {
        return numberTw;
    }

    public void setNumberTw(String numberTw) {
        this.numberTw = numberTw;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public String getSurnameC() {
        return surnameC;
    }

    public void setSurnameC(String surnameC) {
        this.surnameC = surnameC;
    }

    public String getNameC() {
        return nameC;
    }

    public void setNameC(String nameC) {
        this.nameC = nameC;
    }

    public String getPatronymicC() {
        return patronymicC;
    }

    public void setPatronymicC(String patronymicC) {
        this.patronymicC = patronymicC;
    }

    public Date getDateBirth() {
        return dateBirth;
    }

    public void setDateBirth(Date dateBirth) {
        this.dateBirth = dateBirth;
    }

    public String getPlaceBirth() {
        return placeBirth;
    }

    public void setPlaceBirth(String placeBirth) {
        this.placeBirth = placeBirth;
    }

    public String getNameCw() {
        return nameCw;
    }

    public void setNameCw(String nameCw) {
        this.nameCw = nameCw;
    }

    public String getCountChs() {
        return countChs;
    }

    public void setCountChs(String countChs) {
        this.countChs = countChs;
    }

    public String getInn() {
        return inn;
    }

    public void setInn(String inn) {
        this.inn = inn;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getNumberTel() {
        return numberTel;
    }

    public void setNumberTel(String numberTel) {
        this.numberTel = numberTel;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLs() {
        return ls;
    }

    public void setLs(String ls) {
        this.ls = ls;
    }

    public String getBik() {
        return bik;
    }

    public void setBik(String bik) {
        this.bik = bik;
    }

    public String getKs() {
        return ks;
    }

    public void setKs(String ks) {
        this.ks = ks;
    }

    public String getRs() {
        return rs;
    }

    public void setRs(String rs) {
        this.rs = rs;
    }

    public String getKpp() {
        return kpp;
    }

    public void setKpp(String kpp) {
        this.kpp = kpp;
    }

    public String getLegalAddress() {
        return legalAddress;
    }

    public void setLegalAddress(String legalAddress) {
        this.legalAddress = legalAddress;
    }
}
