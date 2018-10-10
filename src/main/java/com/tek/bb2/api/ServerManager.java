package com.tek.bb2.api;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.tek.bb2.bot.BulletBot;

import io.undertow.Handlers;
import io.undertow.Undertow;
import io.undertow.predicate.Predicate;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.handlers.BlockingHandler;
import io.undertow.util.Headers;

public class ServerManager {
	
	private Undertow server;
	
	public void initialize() {
		server = Undertow.builder()
				.addHttpListener(BulletBot.getInstance().getConfig().getApiPort(), "localhost")
				.setHandler(getRouting())
				.build();
		server.start();
	}
	
	public HttpHandler getRouting() {
		return Handlers.predicate((Predicate)pr -> { return pr.getRequestHeaders().contains(Headers.CONTENT_TYPE) && 
				pr.getRequestHeaders().get(Headers.CONTENT_TYPE).getFirst().equals("application/json"); },
				
				Handlers.routing()
				.add("POST", "/api/login", new BlockingHandler(new LoginHandler()))
				.setFallbackHandler(new ErrorHandler(404)),
				
				new ErrorHandler(400));
	}
	
	public static String readPOST(HttpServerExchange exchange) {
		exchange.startBlocking();
		BufferedReader br = new BufferedReader(new InputStreamReader(exchange.getInputStream(), StandardCharsets.UTF_8));
	    return br.lines().collect(Collectors.joining(System.lineSeparator()));
	}
	
	public static boolean isJSONValid(String test) {
	    try {
	        new JSONObject(test);
	    } catch (JSONException ex) {
	        try {
	            new JSONArray(test);
	        } catch (JSONException ex1) {
	            return false;
	        }
	    }
	    return true;
	}
	
	public static String buildJSONError(String error) {
		return new JSONObject().put("error", error).toString();
	}
	
}
