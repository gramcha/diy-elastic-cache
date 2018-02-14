/**
 * @author gramcha
 * 11-Feb-2018 9:50:43 PM
 * 
 */
package com.gramcha.config;

public class RedisHost {
	private String host;
	private String port;
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = port;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("RedisHost [host=");
		builder.append(host);
		builder.append(", port=");
		builder.append(port);
		builder.append("]");
		return builder.toString();
	}
	
}
