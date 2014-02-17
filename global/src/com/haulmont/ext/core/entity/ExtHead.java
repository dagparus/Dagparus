package com.haulmont.ext.core.entity;


import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.docflow.core.entity.Company;
import com.haulmont.docflow.core.entity.Individual;
import com.haulmont.docflow.core.entity.Organization;
import com.haulmont.taskman.core.enums.SexEnum;

import javax.persistence.*;
import java.util.Date;

@Entity(name = "ext$Head")
@Table(name = "EXT_HEAD")
@NamePattern("%s|name")

public class ExtHead extends StandardEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "COMPANY_ID")
    private Company company;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "AREAD_ID")
    private Aread aread;

    @Column(name = "SURNAME", length = 100)
    private String surname;

    @Column(name = "NAMEH", length = 60)
    private String nameh;

    @Column(name = "PATRONYMIC", length = 100)
    private String patronymic;

    @Column(name = "NUMBER_TEL", length = 20)
    private String numberTel;

    @Column(name = "DATE_BIRTH")
    private Date dateBirth;

    @Column(name = "SURNAME_R", length = 100)
    private String surnameR;

    @Column(name = "NAME_R", length = 60)
    private String nameR;

    @Column(name = "PATRONYMIC_R", length = 100)
    private String patronymicR;


    @Column(name = "SEX", length=10)
    private String sex = SexEnum.MALE.getId();

    @Column(name = "POST", length = 100)
    private String post;

    @Column(name = "FAXS", length = 20)
    private String faxs;

    @Column(name = "EMAIL", length = 100)
    private String email;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name ="NAME", length = 100)
    private String name;

    public String getEmail() {
        return email;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public Aread getAread() {
        return aread;
    }

    public void setAread(Aread aread) {
        this.aread = aread;
    }
    public void setEmail(String email) {
        this.email = email;
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

    public SexEnum getSex() {
        return SexEnum.fromId(sex);
    }

    public void setSex(SexEnum sex) {
        this.sex = sex == null ? null : sex.getId();
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getNameh() {
        return nameh;
    }

    public void setNameh(String nameh) {
        this.nameh = nameh;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public String getNumberTel() {
        return numberTel;
    }

    public void setNumberTel(String numberTel) {
        this.numberTel = numberTel;
    }

    public Date getDateBirth() {
        return dateBirth;
    }

    public void setDateBirth(Date dateBirth) {
        this.dateBirth = dateBirth;
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
}
