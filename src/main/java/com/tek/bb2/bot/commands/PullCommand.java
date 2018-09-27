package com.tek.bb2.bot.commands;

import java.util.Random;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.tek.bb2.bot.BulletBot;
import com.tek.bb2.util.CommandCategories;

import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.VoiceChannel;

public class PullCommand extends BotCommand{

	public PullCommand() {
		super("pull", "Pulls a random user into your current voice channel", "<channel>", CommandCategories.GIVEAWAY, null);
		
		this.category = new Category("Giveaway");
	}

	@Override
	public void commandExecute(CommandEvent event) {
		if(BulletBot.getInstance().getConfig().getGiveawayHosts().contains(event.getAuthor().getId())){
			if(event.getMember().getVoiceState().inVoiceChannel()) {
				if(event.getGuild().getVoiceChannels().stream().anyMatch(vc -> vc.getName().equalsIgnoreCase(event.getArgs()))) {
					VoiceChannel vc = event.getGuild().getVoiceChannelsByName(event.getArgs(), true).get(0);
					
					if(!vc.getMembers().isEmpty()) {
						Member toPull = vc.getMembers().get(new Random().nextInt(vc.getMembers().size()));
							
						event.reply("I have chosen **" + toPull.getEffectiveName() + "**, he has been moved to your voice channel");
							
						event.getGuild().getController().moveVoiceMember(toPull, event.getMember().getVoiceState().getChannel()).queue();
					}else {
						event.reply("There is noone in this voice channel");
					}
				}else {
					event.reply("This voice channel does not exist");
				}
			}else {
				event.reply("You must be in a voice channel to use this");
			}
		}else {
			event.reply("You don't have the permissions to use this");
		}
		
		event.getMessage().delete().queue();
	}

}
