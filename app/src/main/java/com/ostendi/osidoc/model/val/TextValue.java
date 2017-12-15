package com.ostendi.osidoc.model.val;


public class TextValue extends Value {

	private String value;

	public TextValue(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public ValueType getType() {
		return ValueType.TEXT;
	}

}