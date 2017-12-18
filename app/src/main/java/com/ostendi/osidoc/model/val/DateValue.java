package com.ostendi.osidoc.model.val;

import org.joda.time.LocalDate;

public class DateValue extends Value {

    LocalDate value;

    public DateValue(LocalDate value) {
        this.value = value;
    }

    public LocalDate getValue() {
        return value;
    }

    public void setValue(LocalDate value) {
        this.value = value;
    }

    @Override
    public ValueType getType() {
        return ValueType.DATE;
    }

    @Override
    public String toString() {
        return value.toString();
    }

}