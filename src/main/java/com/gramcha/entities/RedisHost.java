/**
 * @author gramcha
 * 11-Feb-2018 9:50:43 PM
 * 
 */
package com.gramcha.entities;

public class RedisHost {
	public RedisHost() {
		super();
	}
	/**
	 * @param host
	 * @param port
	 */
	public RedisHost(String host, String port) {
		super();
		this.host = host;
		this.port = port;
	}
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
	public int getPortNumber() {
		return Integer.parseInt(port);
	}
	public void setPort(String port) {
		this.port = port;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(host);
		builder.append(":");
		builder.append(port);
		return builder.toString();
	}
	
}
