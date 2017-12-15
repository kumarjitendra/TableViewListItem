package com.ostendi.osidoc.model;


import com.ostendi.osidoc.model.val.ValueType;

public class FieldDefinition {

	private String id;
	private String label;
	private ValueType type;

	public FieldDefinition(String id, String label, String type) {
		this.id = id;
		this.label = label;
		this.type = ValueType.fromText(type);
	}

	public String getId() {
		return id;
	}

	public String getLabel() {
		return label;
	}

	public ValueType getType() {
		return type;
	}

}