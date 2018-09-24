package com.tek.bb2.log;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ClientLogger {
	
	public static void log(Class<?> clazz, String message) {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();
		
		System.out.println("[" + dtf.format(now) + "][" + clazz.getSimpleName() + "] " + message);
	}
	
}
