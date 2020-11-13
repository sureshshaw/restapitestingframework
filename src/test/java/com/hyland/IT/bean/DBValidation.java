package com.hyland.IT.bean;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class DBValidation implements Serializable {

    private String query;
    private List<DBQueryArgument> arguments;
    private Map<String, String> expectedColumnValues;

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public List<DBQueryArgument> getArguments() {
        return arguments;
    }

    public void setArguments(List<DBQueryArgument> arguments) {
        this.arguments = arguments;
    }

    public Map<String, String> getExpectedColumnValues() {
        return expectedColumnValues;
    }

    public void setExpectedColumnValues(Map<String, String> expectedColumnValues) {
        this.expectedColumnValues = expectedColumnValues;
    }

}
