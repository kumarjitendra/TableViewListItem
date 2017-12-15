package com.ostendi.osidoc.model.val;

public enum ValueType {
	TEXT(0, "text"),
	INT(1, "int"),
	DECIMAL(2, "decimal"),
	DATE(3, "date"),
	TIME(4, "time"),
	DATE_TIME(5, "datetime");

	int value;
	String text;

	ValueType(int value, String text) {
		this.value = value;
		this.text = text;
	}

	public static ValueType fromText(String text) {
		for (ValueType dt : values()) {
			if (dt.text.equals(text)) {
				return dt;
			}
		}
		return TEXT;
	}

	public static ValueType fromValue(int val) {
		for (ValueType dt : values()) {
			if (dt.value == val) {
				return dt;
			}
		}
		return TEXT;
	}

	public int getValue() {
		return value;
	}

	public String getText() {
		return text;
	}

}