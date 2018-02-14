/**
 * @author gramcha
 * 13-Feb-2018 5:46:37 PM
 * 
 */
package com.gramcha.entities;

public class CacheResponse {
	private String key;
	private String value;
	private String status;
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
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("CacheResponse [key=");
		builder.append(key);
		builder.append(", value=");
		builder.append(value);
		builder.append(", status=");
		builder.append(status);
		builder.append("]");
		return builder.toString();
	}
	
}
