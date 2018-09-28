package com.tek.bb2.storage;

public class UserRequest {
	
	private String userId, request;
	
	public UserRequest(String userId, String request) {
		this.userId = userId;
		this.request = request;
	}

	public String getUserId() {
		return userId;
	}
	
	public String getRequest() {
		return request;
	}
	
}
