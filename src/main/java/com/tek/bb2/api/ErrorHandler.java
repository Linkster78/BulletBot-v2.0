package com.tek.bb2.api;

import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;

public class ErrorHandler implements HttpHandler {

	private int code;
	
	public ErrorHandler(int code) {
		this.code = code;
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void handleRequest(HttpServerExchange exchange) throws Exception {
		exchange.setResponseCode(code);
	}

}
