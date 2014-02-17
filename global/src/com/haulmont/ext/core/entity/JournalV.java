package com.haulmont.ext.core.entity;

import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.StandardEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

@Entity(name = "ext$JournalV")
@Table(name = "EXT_JOURNALV")
@NamePattern("%s|dateJV")

public class JournalV extends StandardEntity {

    @Column(name = "DATE_JV")
    private Date dateJV;

    @Column(name = "SOMEONE", length = 100)
    private String someone;

    @Column(name = "CONTENTV", length = 100)
    private String contentV;

    @Column(name = "CONTACT_JV", length = 50)
    private String contactJV;

    @Column(name = "STATUSV", length = 100)
    private String statusV;

    @Column(name = "NUMBER_JV", length = 20)
    private String numberJV;

    public String getNumberJV() {
        return numberJV;
    }

    public void setNumberJV(String numberJV) {
        this.numberJV = numberJV;
    }

    public Date getDateJV() {
        return dateJV;
    }

    public void setDateJV(Date dateJV) {
        this.dateJV = dateJV;
    }

    public String getSomeone() {
        return someone;
    }

    public void setSomeone(String someone) {
        this.someone = someone;
    }

    public String getContentV() {
        return contentV;
    }

    public void setContentV(String contentV) {
        this.contentV = contentV;
    }

    public String getContactJV() {
        return contactJV;
    }

    public void setContactJV(String contactJV) {
        this.contactJV = contactJV;
    }

    public String getStatusV() {
        return statusV;
    }

    public void setStatusV(String statusV) {
        this.statusV = statusV;
    }
}
