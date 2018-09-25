package com.tek.bb2;

import com.tek.bb2.bot.BulletBot;
import com.tek.bb2.config.Config;
import com.tek.bb2.log.ClientLogger;

public class Launcher {
	
	public static void main(String[] args) {
		Config config = Config.load("config.json");
		
		if(config.valid()) {
			BulletBot bulletBot = new BulletBot(config);
			bulletBot.launch();
		} else {
			ClientLogger.log(Launcher.class, "Error while loading configuration file, exiting");
		}
	}
	
}
