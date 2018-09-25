package com.tek.bb2.bot.commands;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.tek.bb2.util.CommandCategories;

public class InviteCommand extends BotCommand {

	public InviteCommand() {
		super("invite", "Displays the server invite", "", CommandCategories.UTILITY, null);
	
		this.category = new Category("Utility");
	}

	@Override
	public void commandExecute(CommandEvent event) {
		event.reply("https://discord.gg/bulletbarry");
		
		event.getMessage().delete().queue();
	}

}
