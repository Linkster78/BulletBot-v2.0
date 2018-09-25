package com.tek.bb2.bot;

import javax.security.auth.login.LoginException;

import com.jagrosh.jdautilities.command.CommandClient;
import com.jagrosh.jdautilities.command.CommandClientBuilder;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import com.tek.bb2.bot.commands.AgreeCommand;
import com.tek.bb2.bot.commands.CommandErrorHandler;
import com.tek.bb2.bot.commands.HelpCommand;
import com.tek.bb2.bot.commands.IDontWantCommand;
import com.tek.bb2.bot.commands.IWantCommand;
import com.tek.bb2.bot.commands.InviteCommand;
import com.tek.bb2.bot.commands.PullCommand;
import com.tek.bb2.bot.commands.UserPickCommand;
import com.tek.bb2.bot.events.GlobalEventListener;
import com.tek.bb2.config.Config;
import com.tek.bb2.log.ClientLogger;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.entities.Guild;

public class BulletBot {
	
	private static BulletBot instance;
	private Config config;
	
	private JDA jda;
	private CommandClient commandClient;
	private EventWaiter eventWaiter;

	public BulletBot(Config config) {
		this.config = config;
		
		instance = this;
	}
	
	public void launch() {
		//COMMANDS
		CommandClientBuilder commandClientBuilder = new CommandClientBuilder();
		
		//CONFIGURE COMMAND HANDLER
		commandClientBuilder.setOwnerId(config.getOwner());
		commandClientBuilder.setPrefix(config.getPrefix());
		commandClientBuilder.setGame(Game.listening(config.getPresence()));
		
		//REGISTER COMMANDS
		commandClientBuilder.addCommand(new AgreeCommand());
		commandClientBuilder.addCommand(new IWantCommand());
		commandClientBuilder.addCommand(new IDontWantCommand());
		commandClientBuilder.addCommand(new InviteCommand());
		commandClientBuilder.addCommand(new UserPickCommand());
		commandClientBuilder.addCommand(new PullCommand());
		
		//FIX HELP MENU
		commandClientBuilder.setHelpConsumer(new HelpCommand());
		
		//ADD ERROR HANDLER FOR MORE USER FRIENDLY RESPONSES
		commandClientBuilder.setListener(new CommandErrorHandler());
		
		//BUILD COMMAND HANDLER
		commandClient = commandClientBuilder.build();
		
		//BUILD EVENT WAITER
		eventWaiter = new EventWaiter();
		
		//JDA
		JDABuilder jdaBuilder = new JDABuilder(config.getToken());
		
		//REGISTER EVENT HANDLERS
		jdaBuilder.addEventListener(commandClient, eventWaiter, new GlobalEventListener());
		
		//BUILD JDA
		try {
			jda = jdaBuilder.build();
		} catch (LoginException e) {
			e.printStackTrace();
			
			ClientLogger.log(BulletBot.class, "Failed to log in");
		}
	}
	
	public Guild getGuild() {
		return jda.getGuildById(config.getGuild());
	}
	
	public Config getConfig() {
		return config;
	}
	
	public JDA getJda() {
		return jda;
	}
	
	public CommandClient getCommandClient() {
		return commandClient;
	}
	
	public EventWaiter getEventWaiter() {
		return eventWaiter;
	}
	
	public static BulletBot getInstance() {
		return instance;
	}
	
}
