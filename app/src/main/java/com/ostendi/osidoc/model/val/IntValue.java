package com.ostendi.osidoc.model.val;

public class IntValue extends Value {

	int value;

	public IntValue(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	@Override
	public ValueType getType() {
		return ValueType.INT;
	}

    @Override
    public String toString() {
        return Integer.toString(value);
    }
}
