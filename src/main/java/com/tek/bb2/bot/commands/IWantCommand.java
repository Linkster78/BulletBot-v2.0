package com.tek.bb2.bot.commands;

import java.util.Arrays;
import java.util.List;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.tek.bb2.bot.BulletBot;
import com.tek.bb2.util.CommandCategories;

import net.dv8tion.jda.core.entities.Role;

public class IWantCommand extends BotCommand {

	public IWantCommand() {
		super("iwant", "Grants you the desired role", "<role>", CommandCategories.ROLES, Arrays.asList(BulletBot.getInstance().getConfig().getOptionalRolesChannel()));
	
		this.category = new Category("Roles");
	}

	@Override
	public void commandExecute(CommandEvent event) {
		if(BulletBot.getInstance().getConfig().getRequestableRoles().stream().anyMatch(s -> s.equalsIgnoreCase(event.getArgs()))) {
			List<Role> roles = event.getGuild().getRolesByName(event.getArgs(), true);
			
			if(!roles.isEmpty()) {
				event.getGuild().getController().addSingleRoleToMember(event.getMember(), roles.get(0)).queue();
			}
		}
		
		event.getMessage().delete().queue();
	}

}
