package com.tek.bb2.bot.commands;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.function.Consumer;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.tek.bb2.bot.BulletBot;

import net.dv8tion.jda.core.EmbedBuilder;

public class HelpCommand implements Consumer<CommandEvent> {

	@Override
	public void accept(CommandEvent event) {
		if(!BulletBot.getInstance().getConfig().getCommandChannels().contains(event.getTextChannel().getId())) return;
		
		String profileURL = BulletBot.getInstance().getJda().getSelfUser().getAvatarUrl();
		String prefix = BulletBot.getInstance().getCommandClient().getPrefix();
		
		EmbedBuilder embedBuilder = new EmbedBuilder();
		embedBuilder.setColor(Color.CYAN);
		embedBuilder.setAuthor("BulletBot", profileURL, profileURL);
		embedBuilder.setThumbnail(profileURL);
		embedBuilder.setTitle("Command List");
		embedBuilder.setDescription("All of the currently available commands of BulletBot");
		
		HashMap<String, ArrayList<Command>> sorted = new HashMap<String, ArrayList<Command>>();
		BulletBot.getInstance().getCommandClient().getCommands().stream().filter(command -> !command.isHidden()).forEach(command -> {
			String category = command.getCategory() == null ? "Unclassed" : command.getCategory().getName();
			
			if(sorted.keySet().contains(category)) {
				sorted.get(category).add(command);
			} else {
				sorted.put(category, new ArrayList<Command>(Arrays.asList(command)));
			}
		});
		
		for(String category : sorted.keySet()) {
			StringBuilder commands = new StringBuilder();
			
			for(Command command : sorted.get(category)) {
				commands.append("**" + prefix + command.getName() + " " + command.getArguments() + "** - " + command.getHelp() + "\n");
			}
			
			embedBuilder.addField(category, commands.toString(), false);
		}
		
		event.replyInDm(embedBuilder.build());
		
		event.reply("Sent the help menu in your DMs");
		
		event.getMessage().delete().queue();
	}
	
}
