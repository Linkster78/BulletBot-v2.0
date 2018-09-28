package com.tek.bb2.bot.commands;

import java.util.Optional;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.tek.bb2.bot.BulletBot;
import com.tek.bb2.storage.UserRequest;
import com.tek.bb2.util.CommandCategories;

public class RequestCommand extends BotCommand {

	public RequestCommand() {
		super("request", "Submits a giveaway action request", "<request>", CommandCategories.GIVEAWAY, BulletBot.getInstance().getConfig().getCommandChannels());
	}

	@Override
	public void commandExecute(CommandEvent event) {
		if(!event.getArgs().isEmpty()) {
			if(BulletBot.getInstance().getStorage().getRequestManager().isOpen()) {
				Optional<UserRequest> userRequest = BulletBot.getInstance().getStorage().getRequestManager().getRequestById(event.getAuthor().getId());
				
				if(!userRequest.isPresent()) {
					UserRequest newRequest = new UserRequest(event.getAuthor(), event.getArgs());
					
					BulletBot.getInstance().getStorage().getRequestManager().getUserRequests().add(newRequest);
					
					event.reply("Your request has been submitted!");
				}else {
					event.reply("You have already submitted a request");
				}
			}else {
				event.reply("Requests are not open");
			}
		}else {
			event.reply("You must include a request");
		}
		
		event.getMessage().delete().queue();
	}

}
