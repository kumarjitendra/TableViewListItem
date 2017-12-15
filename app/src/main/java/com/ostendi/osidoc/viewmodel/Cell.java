package com.ostendi.osidoc.viewmodel;

/**
 * Created by jitendra on 21/11/2017.
 */

public class Cell {

    private String id;
    private String data;

    public Cell(String id) {
        this(id, "");
    }

    public Cell(String strId, String strData) {
        this.id = strId;
        this.data = strData;
    }

    public String getId() {
        return id;
    }

    public String getData() {
        return data;
    }

    public void setData(String strData) {
        data = strData;
    }

}

