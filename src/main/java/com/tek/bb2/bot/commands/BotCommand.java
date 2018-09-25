package com.tek.bb2.bot.commands;

import java.util.List;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

public abstract class BotCommand extends Command{
	
	private List<String> channels;
	
	public BotCommand(String name, String description, String arguments, Category category, List<String> channels, String... aliases) {
		this.guildOnly = true;
		this.name = name;
		this.help = description;
		this.arguments = arguments;
		this.channels = channels;
		this.aliases = aliases;
		this.category = category;
	}
	
	@Override
	protected void execute(CommandEvent event) {
		if(channels == null || channels.contains(event.getTextChannel().getId())) {
			commandExecute(event);
		}
	}
	
	public abstract void commandExecute(CommandEvent event);
	
}
