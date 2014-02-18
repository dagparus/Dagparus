package com.haulmont.ext.core.entity;


import com.haulmont.chile.core.annotations.Aggregation;
import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.annotation.Listeners;
import com.haulmont.cuba.core.entity.annotation.TrackEditScreenHistory;
import com.haulmont.cuba.security.entity.UserRole;
import com.haulmont.docflow.core.entity.Company;
import com.haulmont.docflow.core.entity.Doc;

import javax.persistence.*;
import java.util.Date;
import java.util.LinkedHashSet;

@Entity(name = "ext$ContractPP")
@Table(name = "EXT_CONTRACTPP")
@DiscriminatorValue("112")
@PrimaryKeyJoinColumn(name = "CARD_ID", referencedColumnName = "CARD_ID")
@Listeners("com.haulmont.docflow.core.listeners.DocEntityListener")
@NamePattern("%s|number")
@TrackEditScreenHistory
public class ContractPP extends Doc {
    private static final long serialVersionUID = -5772794459098915083L;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "COMPANY_ID")
    protected CompanyExt extCompany;

    public CompanyExt getExtCompany() {
        return extCompany;
    }

    public void setExtCompany(CompanyExt extCompany) {
        this.extCompany = extCompany;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CLIENT_ID")
    protected ExtClient client;

    public ExtClient getClient() {
        return client;
    }

    public void setClient(ExtClient client) {
        this.client = client;
    }

    @OneToMany(mappedBy = "contractPP")
    @Aggregation
    protected LinkedHashSet<ContractppModull> modull;

    public LinkedHashSet<ContractppModull> getModull() {
        return modull;
    }

    public void setModull(LinkedHashSet<ContractppModull> modull) {
        this.modull = modull;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "HEAD_ID")
    protected ExtHead head;

    public ExtHead getHead() {
        return head;
    }

    public void setHead(ExtHead head) {
        this.head = head;
    }

    @Column (name ="NUMBER")
    private String number;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    @Column (name = "DATE_CONTR")
    private Date dateContr;

    public Date getDateContr() {
        return dateContr;
    }

    public void setDateContr(Date dateContr) {
        this.dateContr = dateContr;
    }

    @Column (name = "DATECLOSE")
    private Date dateClose;

    public Date getDateClose() {
        return dateClose;
    }

    public void setDateClose(Date dateClose) {
        this.dateClose = dateClose;
    }

    @Column (name = "AMOUNT")
    private String amount;

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    @Column (name = "TEXTAMOUNT")
    private String textAmount;

    public String getTextAmount() {
        return textAmount;
    }

    public void setTextAmount(String textAmount) {
        this.textAmount = textAmount;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "VILLA_ID")
    protected Villa villa;

    public Villa getVilla() {
        return villa;
    }

    public void setVilla(Villa villa) {
        this.villa = villa;
    }

    @Column (name = "NAMECONTRACT")
    private String nameContract;

    public String getNameContract() {
        return nameContract;
    }

    public void setNameContract(String nameContract) {
        this.nameContract = nameContract;
    }
}