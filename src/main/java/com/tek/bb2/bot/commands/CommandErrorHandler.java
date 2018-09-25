package com.tek.bb2.bot.commands;

import com.jagrosh.jdautilities.command.CommandListener;
import com.tek.bb2.bot.BulletBot;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class CommandErrorHandler implements CommandListener{
	
	@Override
	public void onNonCommandMessage(MessageReceivedEvent event) {
		if(event.getMessage().getContentRaw().startsWith(BulletBot.getInstance().getConfig().getPrefix())) {
			event.getMessage().delete().queue();
		}
	}
	
}
