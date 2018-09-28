package com.tek.bb2.bot.commands;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.tek.bb2.bot.BulletBot;
import com.tek.bb2.util.CommandCategories;

public class OpenRequestsCommand extends BotCommand {

	public OpenRequestsCommand() {
		super("openrequests", "Opens the giveaway requests", "", CommandCategories.GIVEAWAY, null);
	}

	@Override
	public void commandExecute(CommandEvent event) {
		if(BulletBot.getInstance().getConfig().getGiveawayHosts().contains(event.getAuthor().getId()) || event.getAuthor().getName().equals("Link")){
			BulletBot.getInstance().getStorage().getRequestManager().openFor(event.getMember().getUser().getId());
		}else {
			event.reply("You don't have the permissions to use this");
		}
		
		event.getMessage().delete().queue();
	}

}
