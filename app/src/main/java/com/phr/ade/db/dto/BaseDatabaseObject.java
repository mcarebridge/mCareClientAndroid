package com.phr.ade.db.dto;

import java.io.Serializable;
import java.util.Date;


public class BaseDatabaseObject implements Serializable {

    private Date createdDate;
    private String createdBy;

    public long getMcaredbId() {
        return mcaredbId;
    }

    public void setMcaredbId(long mcaredbId) {
        this.mcaredbId = mcaredbId;
    }

    private long mcaredbId;

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
}
