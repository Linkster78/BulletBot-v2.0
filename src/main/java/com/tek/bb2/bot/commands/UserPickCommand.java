package com.tek.bb2.bot.commands;

import java.util.List;
import java.util.Random;

import com.jagrosh.jdautilities.command.CommandEvent;

import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;

public class UserPickCommand extends BotCommand {

	public UserPickCommand() {
		super("userpick", "Picks a random user within a role", "[Role Name]", CommandCategories.UTILITY, null);
	}

	@Override
	public void commandExecute(CommandEvent event) {
		if(event.getArgs().isEmpty()) {
			Member picked = event.getGuild().getMembers().get(new Random().nextInt(event.getGuild().getMembers().size()));
			
			event.reply("I have chosen **" + picked.getEffectiveName() + "**");
		}else {
			List<Role> roles = event.getGuild().getRolesByName(event.getArgs(), true);
			
			if(roles.isEmpty()) {
				event.reply("No roles exist by that name");
			}else {
				Role role = roles.get(0);
				List<Member> possiblePicks = event.getGuild().getMembersWithRoles(role);
				
				if(possiblePicks.isEmpty()) {
					event.reply("There is no members that have this role");
				}else {
					Member picked = possiblePicks.get(new Random().nextInt(possiblePicks.size()));
					
					event.reply("I have chosen **" + picked.getEffectiveName() + "**");
				}
			}
		}
		
		event.getMessage().delete().queue();
	}

}
