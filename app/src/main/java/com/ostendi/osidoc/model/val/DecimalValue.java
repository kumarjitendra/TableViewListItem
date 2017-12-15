package com.ostendi.osidoc.model.val;

public class DecimalValue extends Value {

	double value;

	public DecimalValue(double value) {
		this.value = value;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	@Override
	public ValueType getType() {
		return ValueType.DECIMAL;
	}

}