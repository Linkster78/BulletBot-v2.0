package com.tek.bb2.bot.events;

import java.awt.Color;
import java.util.Optional;

import com.tek.bb2.addons.UserVoiceChannel;
import com.tek.bb2.bot.BulletBot;
import com.tek.bb2.storage.CachedMessage;
import com.tek.bb2.storage.Storage;
import com.tek.bb2.util.TextUtil;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.channel.voice.VoiceChannelDeleteEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberLeaveEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberRoleAddEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberRoleRemoveEvent;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.core.events.message.MessageDeleteEvent;
import net.dv8tion.jda.core.events.message.MessageUpdateEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class GlobalEventListener extends ListenerAdapter{
	
	@Override
	public void onGuildMemberRoleAdd(GuildMemberRoleAddEvent event) {
		Role memberRole = event.getGuild().getRoleById(BulletBot.getInstance().getConfig().getMemberRole());
		Role timeoutRole = event.getGuild().getRoleById(BulletBot.getInstance().getConfig().getTimeoutRole());
		
		if(event.getRoles().contains(timeoutRole)) {
		    event.getGuild().getController().removeSingleRoleFromMember(event.getMember(), memberRole).queue();
		}
	}
	
	@Override
	public void onGuildMemberRoleRemove(GuildMemberRoleRemoveEvent event) {
		Role memberRole = event.getGuild().getRoleById(BulletBot.getInstance().getConfig().getMemberRole());
		Role timeoutRole = event.getGuild().getRoleById(BulletBot.getInstance().getConfig().getTimeoutRole());
		
		if(event.getRoles().contains(timeoutRole)) {
		    event.getGuild().getController().addSingleRoleToMember(event.getMember(), memberRole).queue();
		}
	}
	
	@Override
	public void onGuildMemberJoin(GuildMemberJoinEvent event) {
		Role timeoutRole = event.getGuild().getRoleById(BulletBot.getInstance().getConfig().getTimeoutRole());
		
		if(BulletBot.getInstance().getStorage().getLeftTimeouts().contains(event.getMember().getUser().getId())) {
			event.getGuild().getController().addSingleRoleToMember(event.getMember(), timeoutRole).queue();
			BulletBot.getInstance().getStorage().getLeftTimeouts().remove(event.getMember().getUser().getId());
			BulletBot.getInstance().getStorage().save();
		}
	}
	
	@Override
	public void onGuildMemberLeave(GuildMemberLeaveEvent event) {
		Role timeoutRole = event.getGuild().getRoleById(BulletBot.getInstance().getConfig().getTimeoutRole());
		
		if(event.getMember().getRoles().contains(timeoutRole)) {
			BulletBot.getInstance().getStorage().getLeftTimeouts().add(event.getMember().getUser().getId());
			BulletBot.getInstance().getStorage().save();
		}else if(BulletBot.getInstance().getStorage().getLeftTimeouts().contains(event.getMember().getUser().getId())){
			BulletBot.getInstance().getStorage().getLeftTimeouts().remove(event.getMember().getUser().getId());
			BulletBot.getInstance().getStorage().save();
		}
	}
	
	@Override
	public void onMessageUpdate(MessageUpdateEvent event) {
		if(!event.getChannelType().equals(ChannelType.TEXT)) return;
		
		Optional<CachedMessage> message = BulletBot.getInstance().getStorage().getCachedMessage(event.getMessageId());
		
		if(message.isPresent()) {
			TextChannel deletedMessagesChannel = event.getGuild().getTextChannelById(BulletBot.getInstance().getConfig().getDeletedMessagesChannel());
			Member member = event.getGuild().getMemberById(message.get().getAuthorId());
			
			EmbedBuilder dmBuilder = new EmbedBuilder();
			dmBuilder.setColor(Color.red);
			dmBuilder.setAuthor("Edited Message", member.getUser().getAvatarUrl(), member.getUser().getAvatarUrl());
			dmBuilder.setThumbnail(member.getUser().getAvatarUrl());
			dmBuilder.addField("Message Author", member.getEffectiveName() + "#" + member.getUser().getDiscriminator(), true);
			dmBuilder.addField("Message Channel", "#" + event.getChannel().getName(), true);
			dmBuilder.addField("Message Author ID", member.getUser().getId(), true);
			dmBuilder.addField("Message ID", message.get().getId(), true);
			dmBuilder.addField("Time", TextUtil.getTimeStamp(), true);
			dmBuilder.addField("Old Message Text", message.get().getMessage().isEmpty() ? "None" : message.get().getMessage(), true);
			dmBuilder.addField("New Message Text", event.getMessage().getContentRaw().isEmpty() ? "None" : event.getMessage().getContentDisplay(), true);
			
			StringBuilder oldStringAttachments = new StringBuilder();
			message.get().getAttachments().forEach(attachment -> {
				String name = attachment.getFileName();
				oldStringAttachments.append("``" + name + "`` *Look further down*\n");
			});
			if(oldStringAttachments.length() == 0) oldStringAttachments.append("None");
			dmBuilder.addField("Old Message Attachments", oldStringAttachments.toString(), true);
			
			StringBuilder newStringAttachments = new StringBuilder();
			event.getMessage().getAttachments().forEach(attachment -> {
				String name = attachment.getFileName();
				newStringAttachments.append("``" + name + "`` *Look further down*\n");
			});
			if(newStringAttachments.length() == 0) newStringAttachments.append("None");
			dmBuilder.addField("New Message Attachments", newStringAttachments.toString(), true);
			
			deletedMessagesChannel.sendMessage(dmBuilder.build()).queue();
			
			CachedMessage newMessage = new CachedMessage(event.getMessage());
			
			Storage storage = BulletBot.getInstance().getStorage();
			
			if(storage.getMessageCache().size() == 250) storage.getMessageCache().remove(storage.getMessageCache().size() - 1);
			
			storage.getMessageCache().add(newMessage);
		}
	}
	
	@Override
	public void onMessageDelete(MessageDeleteEvent event) {
		if(!event.getChannelType().equals(ChannelType.TEXT)) return;
		
		Optional<CachedMessage> message = BulletBot.getInstance().getStorage().getCachedMessage(event.getMessageId());
		
		if(message.isPresent()) {
			TextChannel deletedMessagesChannel = event.getGuild().getTextChannelById(BulletBot.getInstance().getConfig().getDeletedMessagesChannel());
			Member member = event.getGuild().getMemberById(message.get().getAuthorId());
			
			EmbedBuilder dmBuilder = new EmbedBuilder();
			dmBuilder.setColor(Color.red);
			dmBuilder.setAuthor("Deleted Message", member.getUser().getAvatarUrl(), member.getUser().getAvatarUrl());
			dmBuilder.setThumbnail(member.getUser().getAvatarUrl());
			dmBuilder.addField("Message Author", member.getEffectiveName() + "#" + member.getUser().getDiscriminator(), true);
			dmBuilder.addField("Message Channel", "#" + event.getChannel().getName(), true);
			dmBuilder.addField("Message Author ID", member.getUser().getId(), true);
			dmBuilder.addField("Message ID", message.get().getId(), true);
			dmBuilder.addField("Time", TextUtil.getTimeStamp(), true);
			dmBuilder.addField("Message Text", message.get().getMessage().isEmpty() ? "None" : message.get().getMessage(), true);
			
			StringBuilder stringAttachments = new StringBuilder();
			message.get().getAttachments().forEach(attachment -> {
				String name = attachment.getFileName();
				stringAttachments.append("``" + name + "`` *Look further down*\n");
			});
			if(stringAttachments.length() == 0) stringAttachments.append("None");
			dmBuilder.addField("Message Attachments", stringAttachments.toString(), true);
			
			deletedMessagesChannel.sendMessage(dmBuilder.build()).queue();
			
			message.get().getAttachments().forEach(attachment -> {
				if(attachment.isTooBig()){
					deletedMessagesChannel.sendMessage("``" + attachment.getFileName() + "`` **Too Large! " + TextUtil.humanReadableByteCount(attachment.getSize()) + "**").queue();
				}else if(attachment.getByteContent() == null){
					deletedMessagesChannel.sendMessage("**Error while sending " + attachment.getFileName() + "**").queue();
				}else {
					deletedMessagesChannel.sendFile(attachment.getByteContent(), attachment.getFileName()).queue();
				}
				
			});
		}
	}
	
	@Override
	public void onVoiceChannelDelete(VoiceChannelDeleteEvent event) {
		Optional<UserVoiceChannel> channel = BulletBot.getInstance().getStorage().getUserChannelById(event.getChannel().getId());
		
		if(channel.isPresent()) {
			BulletBot.getInstance().getStorage().getUserChannels().remove(channel.get());
		}
	}
	
	@Override
	public void onGuildVoiceJoin(GuildVoiceJoinEvent event) {
		Optional<UserVoiceChannel> channel = BulletBot.getInstance().getStorage().getUserChannelById(event.getChannelJoined().getId());
		
		if(channel.isPresent()) {
			if(!channel.get().getTasks().isEmpty()) {
				channel.get().getTasks().forEach(sf -> sf.cancel(true));
				channel.get().getTasks().clear();
			}
		}
	}
	
	@Override
	public void onGuildVoiceLeave(GuildVoiceLeaveEvent event) {
		Optional<UserVoiceChannel> channel = BulletBot.getInstance().getStorage().getUserChannelById(event.getChannelLeft().getId());
		
		if(channel.isPresent()) {
			if(event.getChannelLeft().getMembers().size() == 0) {
				channel.get().destroy();
				BulletBot.getInstance().getStorage().getUserChannels().remove(channel.get());
			}
		}
	}
	
}
