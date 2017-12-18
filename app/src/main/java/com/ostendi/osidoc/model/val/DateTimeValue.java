package com.ostendi.osidoc.model.val;

import org.joda.time.LocalDateTime;

public class DateTimeValue extends Value {

    LocalDateTime value;

    public DateTimeValue(LocalDateTime value) {
        this.value = value;
    }

    public LocalDateTime getValue() {
        return value;
    }

    public void setValue(LocalDateTime value) {
        this.value = value;
    }

    @Override
    public ValueType getType() {
        return ValueType.DATE_TIME;
    }

    @Override
    public String toString() {
        return value.toString();
    }

}
