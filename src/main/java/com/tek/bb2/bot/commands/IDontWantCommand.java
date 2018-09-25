package com.tek.bb2.bot.commands;

import java.util.Arrays;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.tek.bb2.bot.BulletBot;

import net.dv8tion.jda.core.entities.Role;

public class IDontWantCommand extends BotCommand {

	public IDontWantCommand() {
		super("idontwant", "Revokes the desired role from you", "<role>", CommandCategories.ROLES, Arrays.asList(BulletBot.getInstance().getConfig().getOptionalRolesChannel()));
	}

	@Override
	public void commandExecute(CommandEvent event) {
		if(BulletBot.getInstance().getConfig().getRequestableRoles().stream().anyMatch(s -> s.equalsIgnoreCase(event.getArgs()))) {
			Role role = event.getGuild().getRolesByName(event.getArgs(), true).get(0);
			
			event.getGuild().getController().removeSingleRoleFromMember(event.getMember(), role).queue();
		}
		
		event.getMessage().delete().queue();
	}

}
