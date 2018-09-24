package com.tek.bb2.bot;

import javax.security.auth.login.LoginException;

import com.jagrosh.jdautilities.command.CommandClient;
import com.jagrosh.jdautilities.command.CommandClientBuilder;
import com.tek.bb2.config.Config;
import com.tek.bb2.log.ClientLogger;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Game;

public class BulletBot {
	
	private static BulletBot instance;
	private Config config;
	
	private JDA jda;

	public BulletBot(Config config) {
		this.config = config;
		
		instance = this;
	}
	
	public void launch() {
		//COMMANDS
		CommandClientBuilder commandClientBuilder = new CommandClientBuilder();
		
		commandClientBuilder.setOwnerId(config.getOwner());
		commandClientBuilder.setGame(Game.listening(config.getPresence()));
		commandClientBuilder.setPrefix(config.getPrefix());
		
		CommandClient commandClient = commandClientBuilder.build();
		
		//JDA
		JDABuilder jdaBuilder = new JDABuilder(config.getToken());
		
		jdaBuilder.addEventListener(commandClient);
		
		try {
			jda = jdaBuilder.build();
		} catch (LoginException e) {
			e.printStackTrace();
			
			ClientLogger.log(BulletBot.class, "Failed to log in");
		}
	}
	
	public Config getConfig() {
		return config;
	}
	
	public JDA getJda() {
		return jda;
	}
	
	public static BulletBot getInstance() {
		return instance;
	}
	
}
