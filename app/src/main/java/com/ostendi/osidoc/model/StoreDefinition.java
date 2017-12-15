package com.ostendi.osidoc.model;

import android.util.Log;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import com.ostendi.osidoc.model.val.ValueType;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class StoreDefinition {

    public static final DateTimeFormatter ISO_8601_DATE_FORMAT = DateTimeFormat.forPattern("yyyy-MM-dd");
    public static final DateTimeFormatter ISO_8601_TIME_FORMAT = DateTimeFormat.forPattern("HH:mm:ss");
    public static final DateTimeFormatter ISO_8601_DATE_TIME_FORMAT = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss");

    private String id;
    private String label;

    private LinkedHashMap<String, FieldDefinition> fieldDefinitions = Maps.newLinkedHashMap();

    private boolean isMultipleSelect;
    private int minSelect;
    private int maxSelect;
    private boolean isReadOnly;

    public void addFieldDefinition(String fieldId, String fieldLabel, String fieldType) {
        fieldDefinitions.put(fieldId, new FieldDefinition(fieldId, fieldLabel, fieldType));
    }

    public ValueType getFieldType(String fieldId) {
        return fieldDefinitions.get(fieldId).getType();
    }

    public List<FieldDefinition> getFieldDefinitions() {
        return ImmutableList.copyOf(fieldDefinitions.values());
    }

    public String getFieldId(int fieldNumber) {
    //return new ArrayList<>(fieldDefinitions.keySet()).get(fieldNumber);


      //  List<String> result = (List<String>) fieldDefinitions.values().toArray()[fieldNumber];
       // result.get(fieldNumber);



     //   List<Map.Entry<String, FieldDefinition>> indexedList = new ArrayList<Map.Entry<String, FieldDefinition>>(fieldDefinitions.entrySet());
     //   Map.Entry<String, FieldDefinition> entry = indexedList.get(fieldNumber);
      //  String str = entry.getValue().getId();


      /**
        Set<String> set = fieldDefinitions.keySet();
        List<FieldDefinition> list = new ArrayList<FieldDefinition>(set.hashCode());
        return String.valueOf(list.get(fieldNumber));
 **/

  return null;

    }

    public int getFieldCount() {
        return fieldDefinitions.size();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public boolean isMultipleSelect() {
        return isMultipleSelect;
    }

    public void setMultipleSelect(boolean isMultipleSelect) {
        this.isMultipleSelect = isMultipleSelect;
    }

    public int getMinSelect() {
        return minSelect;
    }

    public void setMinSelect(int minSelect) {
        this.minSelect = minSelect;
    }

    public int getMaxSelect() {
        return maxSelect;
    }

    public void setMaxSelect(int maxSelect) {
        this.maxSelect = maxSelect;
    }

    public boolean isReadOnly() {
        return isReadOnly;
    }

    public void setReadOnly(boolean isReadOnly) {
        this.isReadOnly = isReadOnly;
    }

}