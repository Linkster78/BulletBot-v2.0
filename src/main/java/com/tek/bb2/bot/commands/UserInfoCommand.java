package com.tek.bb2.bot.commands;

import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.tek.bb2.bot.BulletBot;
import com.tek.bb2.util.CommandCategories;
import com.tek.bb2.util.WordUtil;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.Role;

public class UserInfoCommand extends BotCommand {

	public UserInfoCommand() {
		super("userinfo", "Displays information about the user", "[@Mention]", CommandCategories.UTILITY, BulletBot.getInstance().getConfig().getCommandChannels(), "ui");
	}

	@Override
	public void commandExecute(CommandEvent event) {
		if(event.getArgs().isEmpty()) {
			Member member = event.getMember();
			
			event.reply(this.makeEmbedUserInfo(member));
		}else {
			List<Member> members = event.getMessage().getMentionedMembers();
			
			if(members.size() == 1) {
				if(members.get(0).getAsMention().length() == event.getArgs().length()) {
					Member mentioned = members.get(0);
					
					event.reply(this.makeEmbedUserInfo(mentioned));
				}else {
					event.reply("Incorrect Syntax");
				}
			}else if(members.size() > 1) {
				event.reply("Please only mention one user");
			}else if(members.size() == 0) {
				event.reply("You have not mentioned a user");
			}
		}
		
		event.getMessage().delete().queue();
	}
	
	public MessageEmbed makeEmbedUserInfo(Member member) {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		
		EmbedBuilder profileBuilder = new EmbedBuilder();
		profileBuilder.setAuthor("User Info", member.getUser().getAvatarUrl(), member.getUser().getAvatarUrl());
		profileBuilder.setThumbnail(member.getUser().getAvatarUrl());
		profileBuilder.setColor(BulletBot.getInstance().getColor(member.getOnlineStatus()));
		profileBuilder.addField("Real Name", member.getEffectiveName(), true);
		profileBuilder.addField("Nickname", member.getNickname() == null ? "None" : member.getEffectiveName(), true);
		profileBuilder.addField("Account Creation Time", member.getUser().getCreationTime().format(dtf), true);
		profileBuilder.addField("Join Time", member.getJoinDate().format(dtf), true);
		profileBuilder.addField("Status", WordUtil.capitalize(member.getOnlineStatus().name()), true);
		Optional<Role> highestRole = member.getRoles().stream().max(Comparator.comparing(Role::getPosition));
		profileBuilder.addField("Highest Role", highestRole.isPresent() ? highestRole.get().getName() : "None", true);
		
		return profileBuilder.build();
	}

}
