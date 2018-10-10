package com.tek.bb2.storage;

import org.json.JSONObject;

public class AuthCombo {
	
	private final static short ACCESS_TOKEN_LENGTH = 32;
	private final static String ACCESS_TOKEN_CHARACTERS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ!@#$%^&*()";
	
	private String id;
	private String token;
	private byte perms;
	
	public AuthCombo(String id, String accessToken, byte permissions) {
		this.id = id;
		this.token = accessToken;
		this.perms = permissions;
	}

	public JSONObject generateReadablePermissions() {
		JSONObject readablePermissions = new JSONObject();
		
		readablePermissions.put("member", getBit(this.perms, 0));
		readablePermissions.put("supporter", getBit(this.perms, 1));
		readablePermissions.put("giveaway", getBit(this.perms, 2));
		readablePermissions.put("moderator", getBit(this.perms, 3));
		readablePermissions.put("admin", getBit(this.perms, 4));
		readablePermissions.put("headadmin", getBit(this.perms, 5));
		readablePermissions.put("owner", getBit(this.perms, 6));
		
		return readablePermissions;
	}
	
	public boolean getBit(byte val, int position)
	{
	   return (byte) ((val >> position) & 1) == 1;
	}
	
	public String getId() {
		return id;
	}
	
	public String getAccessToken() {
		return token;
	}
	
	public byte getPermissions() {
		return perms;
	}
	
	public static AuthCombo generateDefaultCombo(String id) {
		return new AuthCombo(id, generateAccessToken(), defaultPermissions());
	}
	
	public static byte defaultPermissions() {
		return (byte) 0b00000001;
	}
	
	public static String generateAccessToken() {
		String tok = "";
		
		for(int i = 0; i < ACCESS_TOKEN_LENGTH; i++) {
			tok += ACCESS_TOKEN_CHARACTERS.charAt((int) (Math.random() * ACCESS_TOKEN_CHARACTERS.length()));
		}
		
		return tok;
	}
	
}
