package com.phr.ade.db.dto;


public class MCareTransmission {

    private long mCareRequestId;
    private long mCareResponseId;

    public long getMCareRequestId() {
        return mCareRequestId;
    }

    public void setMCareRequestId(long mCareRequestId) {
        this.mCareRequestId = mCareRequestId;
    }

    public long getMCareResponseId() {
        return mCareResponseId;
    }

    public void setMCareResponseId(long mCareResponseId) {
        this.mCareResponseId = mCareResponseId;
    }
}
