package com.tek.bb2.config;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.json.JSONObject;

public class Config {
	
	private String token, owner, prefix, presence;
	
	private Config(JSONObject config) {
		if(config.has("token")) {
			token = config.getString("token");
			owner = config.getString("owner");
			prefix = config.getString("prefix");
			presence = config.getString("presence");
		}
	}
	
	public boolean valid() {
		return token != null;
	}
	
	public String getToken() {
		return token;
	}
	
	public String getOwner() {
		return owner;
	}
	
	public String getPrefix() {
		return prefix;
	}
	
	public String getPresence() {
		return presence;
	}
	
	public static Config load(String configPath) {
		InputStream is = Config.class.getResourceAsStream(configPath);
		
		StringBuilder configContents = new StringBuilder();
		
		try{
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			String line;
			
			while((line = br.readLine()) != null) {
				configContents.append(line).append("\n");
			}
			
			br.close();
		}catch(IOException e) {
			e.printStackTrace();
			
			return new Config(new JSONObject());
		}
		
		String stringContents = configContents.toString();
		
		JSONObject config = new JSONObject(stringContents);
		
		return new Config(config);
	}
	
}
