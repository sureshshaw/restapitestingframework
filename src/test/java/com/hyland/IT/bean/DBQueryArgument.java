package com.hyland.IT.bean;

import java.io.Serializable;

public class DBQueryArgument implements Serializable {

    private String type;
    private String gpath;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getGpath() {
        return gpath;
    }

    public void setGpath(String gpath) {
        this.gpath = gpath;
    }

}
