package com.tek.bb2.bot.commands;

import java.util.Optional;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.tek.bb2.bot.BulletBot;
import com.tek.bb2.storage.AuthCombo;
import com.tek.bb2.util.CommandCategories;

public class AuthCommand extends BotCommand {

	public AuthCommand() {
		super("auth", "Generates/Regenerates an authentication token for the bot's web panel", "", CommandCategories.WEB, BulletBot.getInstance().getConfig().getCommandChannels());
	}

	@Override
	public void commandExecute(CommandEvent event) {
		Optional<AuthCombo> auth = BulletBot.getInstance().getStorage().getAuthComboById(event.getAuthor().getId());
		
		if(auth.isPresent()) {
			BulletBot.getInstance().getStorage().getPanelAuth().remove(auth.get());
			
			AuthCombo newCombo = AuthCombo.generateDefaultCombo(event.getAuthor().getId());
			
			event.replyInDm("Your new Access Token has been regenerated. Keep it safely and do **not** share it!\nAccess Token: **" + newCombo.getAccessToken() + "**");
		
			BulletBot.getInstance().getStorage().getPanelAuth().add(newCombo);
		}else {
			AuthCombo newCombo = AuthCombo.generateDefaultCombo(event.getAuthor().getId());
			
			event.replyInDm("Your new Access Token has been generated. Keep it safely and do **not** share it!\nAccess Token: **" + newCombo.getAccessToken() + "**");
			
			BulletBot.getInstance().getStorage().getPanelAuth().add(newCombo);
		}
		
		BulletBot.getInstance().getStorage().save();
		
		event.getMessage().delete().queue();
	}

}
