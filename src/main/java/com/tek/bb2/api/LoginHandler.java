package com.tek.bb2.api;

import java.util.Optional;

import org.json.JSONObject;

import com.tek.bb2.bot.BulletBot;
import com.tek.bb2.storage.AuthCombo;

import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;
import net.dv8tion.jda.core.entities.User;

public class LoginHandler implements HttpHandler {

	@SuppressWarnings("deprecation")
	@Override
	public void handleRequest(HttpServerExchange exchange) throws Exception {
		String postData = ServerManager.readPOST(exchange);
		
		if(ServerManager.isJSONValid(postData)) {
			JSONObject json = new JSONObject(postData);
			
			if(json.has("accessToken")) {
				String accessToken = json.getString("accessToken");
				
				if(!accessToken.isEmpty()) {
						Optional<AuthCombo> auth = BulletBot.getInstance().getStorage().getAuthComboByToken(accessToken);
						if(auth.isPresent()) {
							exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "application/json");
							
							JSONObject jsonResponse = new JSONObject();
							jsonResponse.put("id", auth.get().getId());
							jsonResponse.put("accessToken", auth.get().getAccessToken());
							jsonResponse.put("permissions", auth.get().generateReadablePermissions());
							
							User user = BulletBot.getInstance().getJda().getUserById(auth.get().getId());
							
							jsonResponse.put("username", user.getName() + "#" + user.getDiscriminator());
							
							exchange.getResponseSender().send(jsonResponse.toString());
						}else {
							exchange.setResponseCode(401);
							exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "application/json");
							exchange.getResponseSender().send(ServerManager.buildJSONError("Incorrect Access Token"));
						}
				}else {
					exchange.setResponseCode(400);
					exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "application/json");
					exchange.getResponseSender().send(ServerManager.buildJSONError("Access Token is empty"));
				}
			}else {
				exchange.setResponseCode(400);
				exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "application/json");
				exchange.getResponseSender().send(ServerManager.buildJSONError("Access Token missing"));
			}
		}else {
			exchange.setResponseCode(400);
			exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "application/json");
			exchange.getResponseSender().send(ServerManager.buildJSONError("Invalid JSON"));
		}
	}

}
