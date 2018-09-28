package com.tek.bb2.addons;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import com.tek.bb2.bot.BulletBot;

import net.dv8tion.jda.core.entities.Category;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.entities.VoiceChannel;

public class UserVoiceChannel {
	
	private final Queue<ScheduledFuture<Void>> tasks = new LinkedList<>();
	
	private String userId;
	private String vcId;
	private int userCapacity;
	
	public UserVoiceChannel(String userId) {
		this(userId, -1);
	}
	
	public UserVoiceChannel(String userId, int userCapacity) {
		this.userId = userId;
		this.userCapacity = userCapacity;
	}
	
	public void initialize() {
		Category gamingCategory = BulletBot.getInstance().getGuild().getCategoryById(BulletBot.getInstance().getConfig().getGamingCategory());
		
		gamingCategory.createVoiceChannel(getOwner().getName() + "'s room").queue(channel -> {
			if(!(channel instanceof VoiceChannel)) return;
			
			VoiceChannel voiceChannel = (VoiceChannel)channel;
			this.vcId = voiceChannel.getId();
			
			if(this.userCapacity != -1) voiceChannel.getManager().setUserLimit(userCapacity).queue();
			
			ScheduledFuture<Void> deleteFuture = voiceChannel.delete().submitAfter(5, TimeUnit.MINUTES);
			tasks.add(deleteFuture);
		});
	}
	
	public void destroy() {
		getVoiceChannel().delete().complete();
	}
	
	public User getOwner() {
		return BulletBot.getInstance().getJda().getUserById(userId);
	}
	
	public VoiceChannel getVoiceChannel() {
		return BulletBot.getInstance().getJda().getVoiceChannelById(vcId);
	}
	
	public String getUserId() {
		return userId;
	}
	
	public String getVoiceChannelId() {
		return vcId;
	}
	
	public int getUserCapacity() {
		return userCapacity;
	}
	
	public Queue<ScheduledFuture<Void>> getTasks() {
		return tasks;
	}
	
}
