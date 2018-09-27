package com.tek.bb2.bot.commands;

import com.jagrosh.jdautilities.command.CommandListener;
import com.tek.bb2.bot.BulletBot;
import com.tek.bb2.storage.CachedMessage;
import com.tek.bb2.storage.Storage;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class CommandMessageHandler implements CommandListener{
	
	@Override
	public void onNonCommandMessage(MessageReceivedEvent event) {
		if(event.getMessage().getContentRaw().startsWith(BulletBot.getInstance().getCommandClient().getPrefix())) {
			event.getMessage().delete().queue();
		}else {
			CachedMessage messageContent = new CachedMessage(event.getMessage());
			
			Storage storage = BulletBot.getInstance().getStorage();
			
			if(storage.getMessageCache().size() == 250) storage.getMessageCache().remove(storage.getMessageCache().size() - 1);
			
			storage.getMessageCache().add(messageContent);
		}
	}
	
}
