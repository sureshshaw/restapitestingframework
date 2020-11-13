package com.hyland.IT.bean;

import java.io.Serializable;
import java.util.List;

public class TestModule implements Serializable
{
    private String baseUrl;

    private List<TestCases> testCases;

    public void setBaseUrl(String baseUrl){
        this.baseUrl = baseUrl;
    }
    public String getBaseUrl(){
        return this.baseUrl;
    }
    public void setTestCases(List<TestCases> testCases){
        this.testCases = testCases;
    }
    public List<TestCases> getTestCases(){
        return this.testCases;
    }
    
	@Override
	public String toString() {
		return "Root [baseUrl=" + baseUrl + ", testCases=" + testCases + "]";
	}   
}
