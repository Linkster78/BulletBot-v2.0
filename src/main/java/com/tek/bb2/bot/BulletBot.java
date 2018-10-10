package com.tek.bb2.bot;

import java.awt.Color;

import javax.security.auth.login.LoginException;

import com.jagrosh.jdautilities.command.CommandClient;
import com.jagrosh.jdautilities.command.CommandClientBuilder;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import com.tek.bb2.api.ServerManager;
import com.tek.bb2.bot.commands.AgreeCommand;
import com.tek.bb2.bot.commands.AuthCommand;
import com.tek.bb2.bot.commands.ClearCommand;
import com.tek.bb2.bot.commands.CoinFlipCommand;
import com.tek.bb2.bot.commands.CommandMessageHandler;
import com.tek.bb2.bot.commands.HelpCommand;
import com.tek.bb2.bot.commands.IDontWantCommand;
import com.tek.bb2.bot.commands.IWantCommand;
import com.tek.bb2.bot.commands.Image2UTFCommand;
import com.tek.bb2.bot.commands.InviteCommand;
import com.tek.bb2.bot.commands.PullCommand;
import com.tek.bb2.bot.commands.ShutdownCommand;
import com.tek.bb2.bot.commands.UserInfoCommand;
import com.tek.bb2.bot.commands.UserPickCommand;
import com.tek.bb2.bot.commands.VoiceChannelCommand;
import com.tek.bb2.bot.events.GlobalEventListener;
import com.tek.bb2.config.Config;
import com.tek.bb2.log.ClientLogger;
import com.tek.bb2.storage.Storage;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.entities.Guild;

public class BulletBot {
	
	private static BulletBot instance;
	private Config config;
	
	private JDA jda;
	private CommandClient commandClient;
	private EventWaiter eventWaiter;
	private ServerManager serverManager;
	private Storage storage;

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
		//UTILITY
		commandClientBuilder.addCommand(new AgreeCommand());
		commandClientBuilder.addCommand(new UserPickCommand());
		commandClientBuilder.addCommand(new UserInfoCommand());
		commandClientBuilder.addCommand(new VoiceChannelCommand());
		commandClientBuilder.addCommand(new InviteCommand());
		//ROLES
		commandClientBuilder.addCommand(new IWantCommand());
		commandClientBuilder.addCommand(new IDontWantCommand());
		//FUN
		commandClientBuilder.addCommand(new Image2UTFCommand());
		commandClientBuilder.addCommand(new CoinFlipCommand());
		//WEB
		commandClientBuilder.addCommand(new AuthCommand());
		//GIVEAWAY
		commandClientBuilder.addCommand(new PullCommand());
		//MODERATION
		commandClientBuilder.addCommand(new ClearCommand());
		//OWNER
		commandClientBuilder.addCommand(new ShutdownCommand());
		
		//FIX HELP MENU
		commandClientBuilder.setHelpConsumer(new HelpCommand());
		
		//ADD ERROR HANDLER FOR MORE USER FRIENDLY RESPONSES
		commandClientBuilder.setListener(new CommandMessageHandler());
		
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
		
		//LOAD STORAGE
		storage = Storage.load("storage.json");
		
		//LOAD API
		serverManager = new ServerManager();
		serverManager.initialize();
	}
	
	public void shutdown() {
		storage.shutdown();
		jda.shutdown();
	}
	
	public Guild getGuild() {
		return jda.getGuildById(config.getGuild());
	}
	
	public Color getColor(OnlineStatus status) {
		switch(status){
			case INVISIBLE:
				return Color.gray;
			case OFFLINE:
				return Color.gray;
			case DO_NOT_DISTURB:
				return Color.red;
			case IDLE:
				return Color.yellow;
			case ONLINE:
				return Color.green;
			case UNKNOWN:
				return Color.magenta;
			default:
				return Color.magenta;
		}
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
	
	public ServerManager getServerManager() {
		return serverManager;
	}
	
	public EventWaiter getEventWaiter() {
		return eventWaiter;
	}
	
	public Storage getStorage() {
		return storage;
	}
	
	public static BulletBot getInstance() {
		return instance;
	}
	
}
