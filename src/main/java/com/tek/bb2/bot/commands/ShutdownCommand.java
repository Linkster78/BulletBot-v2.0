package com.tek.bb2.bot.commands;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.tek.bb2.bot.BulletBot;
import com.tek.bb2.util.CommandCategories;

public class ShutdownCommand extends BotCommand{

	public ShutdownCommand() {
		super("shutdown", "Shuts down the bot", "", CommandCategories.OWNER, null);
		
		this.hidden = true;
		this.ownerCommand = true;
	}

	@Override
	public void commandExecute(CommandEvent event) {
		BulletBot.getInstance().shutdown();
	}

}
