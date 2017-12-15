package com.ostendi.osidoc.model;

import com.google.common.collect.Lists;

import java.util.Collections;
import java.util.List;

public class Page {

	private List<Record> records = Lists.newArrayList();
	private boolean hasMore;

	public void addRow(Record record) {
		records.add(record);
	}

	public List<Record> getRecords() {
		return Collections.unmodifiableList(records);
	}

	public boolean hasMore() {
		return hasMore;
	}

	public void setMore(boolean hasMore) {
		this.hasMore = hasMore;
	}

}
