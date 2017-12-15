package com.ostendi.osidoc.server;

import com.ostendi.osidoc.model.Page;
import com.ostendi.osidoc.model.Record;
import com.ostendi.osidoc.model.StoreDefinition;
import com.ostendi.osidoc.model.val.DateTimeValue;
import com.ostendi.osidoc.model.val.DateValue;
import com.ostendi.osidoc.model.val.DecimalValue;
import com.ostendi.osidoc.model.val.IntValue;
import com.ostendi.osidoc.model.val.TextValue;
import com.ostendi.osidoc.model.val.TimeValue;
import com.ostendi.osidoc.model.val.Value;
import com.ostendi.osidoc.model.val.ValueType;

import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;


public final class ServerDataDeserializer {

    private ServerDataDeserializer() {
    }

    public static StoreDefinition deserializeStoreDefinition(String tableJsonText) {
        StoreDefinition storeDefinition = new StoreDefinition();
        try {
            JSONObject tableJson = new JSONObject(tableJsonText);

            storeDefinition.setId(tableJson.getString("id"));
            storeDefinition.setLabel(tableJson.getString("label"));

            boolean isMultipleSelect = tableJson.getBoolean("multipleSelect");
            if (isMultipleSelect) {
                storeDefinition.setMultipleSelect(true);

                if (tableJson.has("minSelect")) {
                    storeDefinition.setMinSelect(tableJson.getInt("minSelect"));
                }
                if (tableJson.has("maxSelect")) {
                    storeDefinition.setMaxSelect(tableJson.getInt("maxSelect"));
                }
                if (tableJson.has("readOnly")) {
                    storeDefinition.setReadOnly(tableJson.getBoolean("readOnly"));
                }
            }

            JSONArray columnsJson = tableJson.getJSONArray("columns");
            for (int i = 0; i < columnsJson.length(); i++) {
                JSONObject columnJson = columnsJson.getJSONObject(i);
                storeDefinition.addFieldDefinition(
                        columnJson.getString("id"),
                        columnJson.getString("label"),
                        columnJson.getString("type")
                );
            }

            return storeDefinition;
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
//deserializePage :converting back the stream into an object
    public static Page deserializePage(StoreDefinition table, String pageJsonText) {
        Page page = new Page();
        try {
            JSONObject pageJson = new JSONObject(pageJsonText);

            page.setMore(pageJson.getBoolean("more"));

            JSONArray rowsJson = pageJson.getJSONArray("rows");
            for (int i = 0; i < rowsJson.length(); i++) {
                Record record = new Record();
                JSONObject rowJson = rowsJson.getJSONObject(i);

                Iterator<String> keys = rowJson.keys();
                while (keys.hasNext()) {
                    String columnId = (String) keys.next(); // First key in your json object
                    // for (String columnId : key) {
                    ValueType columnType = table.getFieldType(columnId);

                    Value value;
                    switch (columnType) {
                        case INT:
                            value = new IntValue(rowJson.getInt(columnId));
                            break;
                        case DECIMAL:
                            value = new DecimalValue(rowJson.getDouble(columnId));
                            break;
                        case DATE:
                            String v = rowJson.getString(columnId);
                            value = new DateValue(getLocalDate(v)); //rowJson.getString(columnId)));
                            break;
                        case TIME:
                            String v1 = rowJson.getString(columnId);
                            value = new TimeValue(getLocalTime(v1)); //rowJson.getString(columnId)));
                            break;
                        case DATE_TIME:
                            String v2 = rowJson.getString(columnId);
                            value = new DateTimeValue(getLocalDateTime(v2)); //rowJson.getString(columnId)));
                            break;
                        default: //case TEXT:
                            value = new TextValue(rowJson.getString(columnId));
                            break;
                    }

                    record.addFieldValue(columnId, value);

                    page.addRow(record);
                }
            }

            return page;
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }


    private static LocalDate getLocalDate(String text) {
        return new LocalDate(StoreDefinition.ISO_8601_DATE_FORMAT.parseLocalDate(text));
    }

    private static LocalTime getLocalTime(String text) {
        return new LocalTime(StoreDefinition.ISO_8601_TIME_FORMAT.parseLocalTime(text));
    }

    private static LocalDateTime getLocalDateTime(String text) {
        return new LocalDateTime(StoreDefinition.ISO_8601_DATE_TIME_FORMAT.parseLocalDateTime(text));
    }

}
