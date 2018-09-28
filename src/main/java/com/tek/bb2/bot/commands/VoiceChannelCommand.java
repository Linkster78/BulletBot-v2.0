package com.tek.bb2.bot.commands;

import java.util.Arrays;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.tek.bb2.addons.UserVoiceChannel;
import com.tek.bb2.bot.BulletBot;
import com.tek.bb2.util.CommandCategories;
import com.tek.bb2.util.TextUtil;

public class VoiceChannelCommand extends BotCommand {

	public VoiceChannelCommand() {
		super("voicechannel", "Creates a temporary voice channel", "[capacity]", CommandCategories.UTILITY, Arrays.asList(BulletBot.getInstance().getConfig().getChannelRequestsChannel()), "vc");
	}

	@Override
	public void commandExecute(CommandEvent event) {
		if(event.getArgs().isEmpty()) {
			if(!BulletBot.getInstance().getStorage().getUserChannel(event.getAuthor().getId()).isPresent()) {
				UserVoiceChannel channel = new UserVoiceChannel(event.getAuthor().getId());
				channel.initialize();
				BulletBot.getInstance().getStorage().getUserChannels().add(channel);
			}
		}else if(TextUtil.isNumber(event.getArgs())){
			int number = TextUtil.getNumber(event.getArgs());
			
			if(!BulletBot.getInstance().getStorage().getUserChannel(event.getAuthor().getId()).isPresent()) {
				if(number > 0 && number < 100) {
					UserVoiceChannel channel = new UserVoiceChannel(event.getAuthor().getId(), number);
					channel.initialize();
					BulletBot.getInstance().getStorage().getUserChannels().add(channel);
				}
			}
		}
		
		event.getMessage().delete().queue();
	}

}
