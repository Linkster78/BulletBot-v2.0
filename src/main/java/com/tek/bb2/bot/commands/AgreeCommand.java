package com.tek.bb2.bot.commands;

import java.time.OffsetDateTime;
import java.util.Arrays;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.tek.bb2.bot.BulletBot;
import com.tek.bb2.util.CommandCategories;

import net.dv8tion.jda.core.entities.Role;

public class AgreeCommand extends BotCommand {

	public AgreeCommand() {
		super("agree", "Agrees to the rules", "", CommandCategories.UTILITY, Arrays.asList(BulletBot.getInstance().getConfig().getWelcomeChannel()));
		
		this.hidden = true;
	}
	
	@Override
	public void commandExecute(CommandEvent event) {
		OffsetDateTime created = event.getAuthor().getCreationTime();
		OffsetDateTime earlierCommand = event.getMessage().getCreationTime().minusWeeks(1);
		
		if(earlierCommand.isAfter(created)) {
			event.getGuild().getController().addSingleRoleToMember(event.getMember(), event.getJDA().getRoleById(BulletBot.getInstance().getConfig().getMemberRole())).queue();
		} else {
			Role mod = event.getJDA().getRoleById(BulletBot.getInstance().getConfig().getModRole());
			Role admin = event.getJDA().getRoleById(BulletBot.getInstance().getConfig().getAdminRole());
			
			event.reply(event.getAuthor().getAsMention() + ", it seems your account is too recent. Please connect some form of social media such as Steam or Reddit to your discord account then wait for a " + mod.getAsMention() + " or " + admin.getAsMention() + " to allow you into the server.");
		}
		
		event.getMessage().delete().queue();
	}

}
