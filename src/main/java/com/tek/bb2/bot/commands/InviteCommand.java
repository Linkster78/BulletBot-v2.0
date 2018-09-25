package com.tek.bb2.bot.commands;

import com.jagrosh.jdautilities.command.CommandEvent;

public class InviteCommand extends BotCommand {

	public InviteCommand() {
		super("invite", "Displays the server invite", "", null);
	
		this.category = new Category("Utility");
	}

	@Override
	public void commandExecute(CommandEvent event) {
		event.reply("https://discord.gg/bulletbarry");
		
		event.getMessage().delete().queue();
	}

}
