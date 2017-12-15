package com.ostendi.osidoc.server;

import com.ostendi.osidoc.model.Page;
import com.ostendi.osidoc.model.StoreDefinition;
import com.ostendi.osidoc.model.val.ValueType;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

public class FakeHttpServer {

    private int rowCount;
    private LinkedHashMap<String, ValueType> columnDataTypes = new LinkedHashMap<String, ValueType>();

    public FakeHttpServer(int rowCount) {
        this.rowCount = rowCount;

        int numberOrColumns = RandomUtils.nextInt(8, 20);
        for (int i = 0; i < numberOrColumns; i++) {
            ValueType dataType = ValueType.fromValue(RandomUtils.nextInt(0, 6));
            String id = RandomStringUtils.randomAlphabetic(5).toLowerCase();
            columnDataTypes.put(id, dataType);
        }
    }

    public String fetchStoreDefinition(String tableId) {
        JSONArray columns = new JSONArray();
        try {
            for (Entry<String, ValueType> fieldDateType : columnDataTypes.entrySet()) {
                columns.put(new JSONObject()
                    .put("id", fieldDateType.getKey())
                    .put("label", StringUtils.capitalize(fieldDateType.getKey()))
                    .put("type", fieldDateType.getValue().getText())
                );
            }
            return new JSONObject()
                .put("id", tableId)
                .put("label", "My table label")
                .put("columns", columns)
                .put("multipleSelect", false)
                .put("readOnly", false)
                .toString();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public String fetchPage(String tableId, int start, int limit) {
        boolean hasMore = true;
        int last = start + limit - 1;
        if (last >= rowCount - 1) {
            hasMore = false;
            last = rowCount - 1;
        }

        JSONArray records = new JSONArray();
        try {
            for (int i = start; i <= last; i++) {
                JSONObject row = new JSONObject();

                for (Entry<String, ValueType> columnDataType : columnDataTypes.entrySet()) {
                    String id = columnDataType.getKey();
                    switch (columnDataType.getValue()) {
                        case INT:
                            row.put(id, getRandomIntValue());
                            break;
                        case DECIMAL:
                            row.put(id, getRandomDecimalValue());
                            break;
                        case DATE:
                            row.put(id, getRandomIso8601DateValue());
                            break;
                        case TIME:
                            row.put(id, getRandomIso8601TimeValue());
                            break;
                        case DATE_TIME:
                            row.put(id, getRandomIso8601DateTimeValue());
                            break;
                        default:
                            row.put(id, getRandomText());
                    }
                }
                records.put(row);
            }

            return new JSONObject()
                .put("rows", records)
                .put("more", hasMore)
                .toString();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    private String getRandomText() {
        return RandomStringUtils.randomAlphabetic(2, 20);
    }

    private int getRandomIntValue() {
        return RandomUtils.nextInt(0, 5000);
    }

    private BigDecimal getRandomDecimalValue() {
        return new BigDecimal(RandomUtils.nextDouble(0, 5000)).setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    private String getRandomIso8601DateValue() {
        return StoreDefinition.ISO_8601_DATE_FORMAT.print(new LocalDate(getRandomDate()));
    }

    private String getRandomIso8601TimeValue() {
        return StoreDefinition.ISO_8601_TIME_FORMAT.print(new LocalTime(getRandomDate()));
    }

    private String getRandomIso8601DateTimeValue() {
        return StoreDefinition.ISO_8601_DATE_TIME_FORMAT.print(new LocalDateTime(getRandomDate()));
    }

    private Date getRandomDate() {
        return new Date(Math.abs(System.currentTimeMillis() - RandomUtils.nextLong(0, 4509960660777L)));
    }

    public static void main(String[] args) {
        // Mocks the server calls
        FakeHttpServer tableDataProvider = new FakeHttpServer(500);

        String tableJson = tableDataProvider.fetchStoreDefinition("");

        StoreDefinition tableStructure = ServerDataDeserializer.deserializeStoreDefinition(tableJson);
        int pageSize = 50;

        int start = 0;
        while (true) {
            String pageJson = tableDataProvider.fetchPage(tableStructure.getId(), start, pageSize);
            System.out.println(pageJson);
            Page page = ServerDataDeserializer.deserializePage(tableStructure, pageJson);

            if (!page.hasMore()) {
                break;
            }
            start += pageSize;
        }

    }

}