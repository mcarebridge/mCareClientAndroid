package com.phr.ade.db.dto;

import java.io.Serializable;


public class MCareRequest extends BaseDatabaseObject {

    private String requestBody;


    public String getRequestBody() {
        return requestBody;
    }

    public void setRequestBody(String requestBody) {
        this.requestBody = requestBody;
    }
}
