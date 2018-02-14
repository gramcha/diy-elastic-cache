/**
 * @author gramcha
 * 13-Feb-2018 5:46:24 PM
 * 
 */
package com.gramcha.entities;

public class CacheRequest {
	private String key;
	private String value;
	private boolean isWrite;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public boolean isWrite() {
		return isWrite;
	}

	public void setWrite(boolean isWrite) {
		this.isWrite = isWrite;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("CacheRequest [key=");
		builder.append(key);
		builder.append(", value=");
		builder.append(value);
		builder.append(", isWrite=");
		builder.append(isWrite);
		builder.append("]");
		return builder.toString();
	}

}
