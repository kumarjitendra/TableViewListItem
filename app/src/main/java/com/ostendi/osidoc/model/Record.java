package com.ostendi.osidoc.model;

import android.util.Log;

import com.google.common.collect.Maps;
import com.ostendi.osidoc.model.val.Value;

import java.util.Collections;
import java.util.Map;

public class Record {
    private static final String LOG_TAG = Record.class.getSimpleName();
    private long id;
    private Map<String, Value> fieldValues = Maps.newHashMap();

    public Record() {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void addFieldValue(String fieldId, Value fieldValue) {
        fieldValues.put(fieldId, fieldValue);
    }

    public Value getFieldValue(String fieldId) {
        return fieldValues.get(fieldId);
    }

    public Map<String, Value> getFieldValues() {
        return Collections.unmodifiableMap(fieldValues);
    }


}
