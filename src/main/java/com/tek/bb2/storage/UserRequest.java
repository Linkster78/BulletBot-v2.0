package com.tek.bb2.storage;

import net.dv8tion.jda.core.entities.User;

public class UserRequest {
	
	private String userId, username, request;
	
	public UserRequest(User user, String request) {
		this.userId = user.getId();
		this.username = user.getName() + "#" + user.getDiscriminator();
		this.request = request;
	}

	public String getUserId() {
		return userId;
	}
	
	public String getUsername() {
		return username;
	}
	
	public String getRequest() {
		return request;
	}
	
	@Override
	public String toString() {
		return "\"**" + request + "**\" from **" + username + "**";
	}
	
}
