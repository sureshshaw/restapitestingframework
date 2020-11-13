package com.hyland.IT.bean;

import java.io.Serializable;
import java.util.Map;

public class ExpectedResult implements Serializable {
    private int statusCode;
    private Long responseTime;
    private String responseJsonSchemaFileName;
	private Map<String, String> responseHeaders;
    private Map<String, String> responseBody;
    private DBValidation dbValidation;

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public Long getResponseTime() {
 		return responseTime;
 	}

 	public void setResponseTime(Long responseTime) {
 		this.responseTime = responseTime;
 	}
 	
 	
    
    public String getResponseJsonSchemaFileName() {
		return responseJsonSchemaFileName;
	}

	public void setResponseJsonSchemaFileName(String responseJsonSchemaFileName) {
		this.responseJsonSchemaFileName = responseJsonSchemaFileName;
	}

	public Map<String, String> getResponseHeaders() {
        return responseHeaders;
    }

    public void setResponseHeaders(Map<String, String> responseHeaders) {
        this.responseHeaders = responseHeaders;
    }

    public Map<String, String> getResponseBody() {
        return responseBody;
    }

    public void setResponseBody(Map<String, String> responseBody) {
        this.responseBody = responseBody;
    }

    public DBValidation getDbValidation() {
        return dbValidation;
    }

    public void setDbValidation(DBValidation dbValidation) {
        this.dbValidation = dbValidation;
    }
}