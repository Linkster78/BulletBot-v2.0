package com.tek.bb2.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;

import org.json.JSONObject;

public class Config {
	
	private String token, owner, guild, prefix, presence, welcomeChannel, optionalRolesChannel, deletedMessagesChannel, channelRequestsChannel, gamingCategory, memberRole, timeoutRole, modRole, adminRole;
	private int apiPort;
	private List<String> commandChannels, requestableRoles, giveawayHosts;
	
	private Config(JSONObject config) {
		try{
			if(config.has("token")) {
				token = config.getString("token");
				apiPort = config.getInt("apiPort");
				owner = config.getString("owner");
				guild = config.getString("guild");
				prefix = config.getString("prefix");
				presence = config.getString("presence");
				welcomeChannel = config.getString("welcomeChannel");
				optionalRolesChannel = config.getString("optionalRolesChannel");
				deletedMessagesChannel = config.getString("deletedMessagesChannel");
				channelRequestsChannel = config.getString("channelRequestsChannel");
				gamingCategory = config.getString("gamingCategory");
				memberRole = config.getString("memberRole");
				timeoutRole = config.getString("timeoutRole");
				modRole = config.getString("modRole");
				adminRole = config.getString("adminRole");
				commandChannels = config.getJSONArray("commandChannels").toList().stream().map((o) -> (String)o).collect(Collectors.toList());
				requestableRoles = config.getJSONArray("requestableRoles").toList().stream().map((o) -> (String)o).collect(Collectors.toList());
				giveawayHosts = config.getJSONArray("giveawayHosts").toList().stream().map((o) -> (String)o).collect(Collectors.toList());
			}
		}catch(Exception e) {
			token = null;
		}
	}
	
	public boolean valid() {
		return token != null;
	}
	
	public String getToken() {
		return token;
	}
	
	public int getApiPort() {
		return apiPort;
	}
	
	public String getOwner() {
		return owner;
	}
	
	public String getGuild() {
		return guild;
	}
	
	public String getPrefix() {
		return prefix;
	}
	
	public String getPresence() {
		return presence;
	}
	
	public String getWelcomeChannel() {
		return welcomeChannel;
	}
	
	public String getOptionalRolesChannel() {
		return optionalRolesChannel;
	}
	
	public String getDeletedMessagesChannel() {
		return deletedMessagesChannel;
	}
	
	public String getChannelRequestsChannel() {
		return channelRequestsChannel;
	}
	
	public String getGamingCategory() {
		return gamingCategory;
	}
	
	public String getMemberRole() {
		return memberRole;
	}
	
	public String getTimeoutRole() {
		return timeoutRole;
	}
	
	public String getModRole() {
		return modRole;
	}
	
	public String getAdminRole() {
		return adminRole;
	}
	
	public List<String> getCommandChannels() {
		return commandChannels;
	}
	
	public List<String> getRequestableRoles() {
		return requestableRoles;
	}
	
	public List<String> getGiveawayHosts() {
		return giveawayHosts;
	}
	
	public static Config load(String configPath) {
		StringBuilder configContents = new StringBuilder();
		
		try{
			FileInputStream fis = new FileInputStream(new File(configPath));
			BufferedReader br = new BufferedReader(new InputStreamReader(fis));
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
