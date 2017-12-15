package com.ostendi.osidoc.model.val;

import org.joda.time.LocalTime;

public class TimeValue extends Value {

	private LocalTime value;

	public TimeValue(LocalTime value) {
		this.value = value;
	}

	public LocalTime getValue() {
		return value;
	}

	public void setValue(LocalTime value) {
		this.value = value;
	}

	@Override
	public ValueType getType() {
		return ValueType.TIME;
	}

}