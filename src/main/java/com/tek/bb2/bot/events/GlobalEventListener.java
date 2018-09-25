package com.tek.bb2.bot.events;

import com.tek.bb2.bot.BulletBot;

import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.events.guild.member.GuildMemberRoleAddEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberRoleRemoveEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class GlobalEventListener extends ListenerAdapter{
	
	@Override
	public void onGuildMemberRoleAdd(GuildMemberRoleAddEvent event) {
		Role memberRole = event.getGuild().getRoleById(BulletBot.getInstance().getConfig().getMemberRole());
		Role timeoutRole = event.getGuild().getRoleById(BulletBot.getInstance().getConfig().getTimeoutRole());
		Member member = event.getGuild().getMember(event.getUser());
		
		if(event.getRoles().contains(timeoutRole)) {
		    event.getGuild().getController().removeSingleRoleFromMember(member, memberRole).queue();
		}
	}
	
	@Override
	public void onGuildMemberRoleRemove(GuildMemberRoleRemoveEvent event) {
		Role memberRole = event.getGuild().getRoleById(BulletBot.getInstance().getConfig().getMemberRole());
		Role timeoutRole = event.getGuild().getRoleById(BulletBot.getInstance().getConfig().getTimeoutRole());
		Member member = event.getGuild().getMember(event.getUser());
		
		if(event.getRoles().contains(timeoutRole)) {
		    event.getGuild().getController().addSingleRoleToMember(member, memberRole).queue();
		}
	}
	
}
