package com.tek.bb2.storage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;
import java.util.stream.Collectors;

import org.json.JSONArray;
import org.json.JSONObject;

import com.tek.bb2.addons.UserVoiceChannel;

public class Storage {
	
	private ArrayList<AuthCombo> panelAuth;
	private ArrayList<String> leftTimeouts;
	private ArrayList<CachedMessage> messageCache;
	private ArrayList<UserVoiceChannel> userChannels;
	
	@SuppressWarnings("unchecked")
	private Storage(JSONObject json) {
		if(json.has("leftTimeouts")) {
			leftTimeouts = new ArrayList<String>(json.getJSONArray("leftTimeouts").toList().stream().map(o -> (String)o).collect(Collectors.toList()));
		}else {
			leftTimeouts = new ArrayList<String>();
		}
		
		if(json.has("panelAuth")) {
			panelAuth = new ArrayList<AuthCombo>( json.getJSONArray("panelAuth").toList().stream().map(o -> {
					HashMap<String, Object> map = (HashMap<String,Object>)o;
					AuthCombo combo = new AuthCombo((String)map.get("id"), (String)map.get("token"), (byte)((Integer)map.get("perms")).intValue());
					return combo;
				}).collect(Collectors.toList()));
		}else {
			panelAuth = new ArrayList<AuthCombo>();
		}
		
		messageCache = new ArrayList<CachedMessage>();
		userChannels = new ArrayList<UserVoiceChannel>();
	}
	
	public JSONObject encode() {
		JSONObject encoded = new JSONObject();
		
		encoded.put("leftTimeouts", leftTimeouts);
		
		JSONArray panelAuthJson = new JSONArray();
		panelAuth.forEach(authCombo -> {
			JSONObject obj = new JSONObject();
			obj.put("id", authCombo.getId());
			obj.put("token", authCombo.getAccessToken());
			obj.put("perms", authCombo.getPermissions());
			panelAuthJson.put(obj);
		});
		
		encoded.put("panelAuth", panelAuthJson);
		
		return encoded;
	}
	
	public void save() {
		File file = new File("storage.json");
		
		try{
			FileWriter writer = new FileWriter(file);
			
			writer.write(encode().toString());
			
			writer.flush();
			writer.close();
		}catch(IOException e) { e.printStackTrace(); }
	}
	
	public void shutdown() {
		for(UserVoiceChannel channel : userChannels) {
			channel.destroy();
		}
	}
	
	public ArrayList<String> getLeftTimeouts() {
		return leftTimeouts;
	}
	
	public ArrayList<CachedMessage> getMessageCache() {
		return messageCache;
	}
	
	public ArrayList<UserVoiceChannel> getUserChannels() {
		return userChannels;
	}
	
	public ArrayList<AuthCombo> getPanelAuth() {
		return panelAuth;
	}
	
	public Optional<CachedMessage> getCachedMessage(String id) {
		Optional<CachedMessage> message = messageCache.stream().filter(cm -> cm.getId().equals(id)).findFirst();
		
		if(message.isPresent()) messageCache.remove(message.get());
		
		return message;
	}
	
	public Optional<UserVoiceChannel> getUserChannel(String id) {
		Optional<UserVoiceChannel> userChannel = userChannels.stream().filter(uvc -> uvc.getUserId().equals(id)).findFirst();
		
		return userChannel;
	}
	
	public Optional<UserVoiceChannel> getUserChannelById(String id) {
		Optional<UserVoiceChannel> userChannel = userChannels.stream().filter(uvc -> uvc.getVoiceChannelId().equals(id)).findFirst();
		
		return userChannel;
	}
	
	public Optional<AuthCombo> getAuthComboByToken(String token){
		Optional<AuthCombo> auth = panelAuth.stream().filter(combo -> combo.getAccessToken().equals(token)).findFirst();
		
		return auth;
	}
	
	public Optional<AuthCombo> getAuthComboById(String id){
		Optional<AuthCombo> auth = panelAuth.stream().filter(combo -> combo.getId().equals(id)).findFirst();
		
		return auth;
	}
	
	public static Storage load(String storagePath) {
		StringBuilder configContents = new StringBuilder();
		
		try{
			File storage = new File(storagePath);
			if(!storage.exists()) {
				storage.createNewFile();
				FileWriter writer = new FileWriter(storage);
				writer.write("{}");
				writer.flush();
				writer.close();
			}
			
			FileInputStream fis = new FileInputStream(new File(storagePath));
			BufferedReader br = new BufferedReader(new InputStreamReader(fis));
			String line;
			
			while((line = br.readLine()) != null) {
				configContents.append(line).append("\n");
			}
			
			br.close();
		}catch(IOException e) {
			e.printStackTrace();
			
			return new Storage(new JSONObject());
		}
		
		String stringContents = configContents.toString();
		
		JSONObject config = new JSONObject(stringContents);
		
		return new Storage(config);
	}
	
}
