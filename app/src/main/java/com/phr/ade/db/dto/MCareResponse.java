package com.phr.ade.db.dto;

import java.io.Serializable;


public class MCareResponse extends BaseDatabaseObject {

    private String responseBody;

    public String getResponseBody() {
        return responseBody;
    }

    public void setResponseBody(String responseBody) {
        this.responseBody = responseBody;
    }
}
