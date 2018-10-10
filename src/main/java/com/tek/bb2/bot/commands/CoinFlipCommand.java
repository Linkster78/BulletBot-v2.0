package com.tek.bb2.bot.commands;

import java.util.Random;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.tek.bb2.util.CommandCategories;
import com.tek.bb2.util.TextUtil;

public class CoinFlipCommand extends BotCommand {

	public CoinFlipCommand() {
		super("coinflip", "Flips a coin", "[Random Seed]", CommandCategories.FUN, null);
	}

	@Override
	public void commandExecute(CommandEvent event) {
		if(!event.getArgs().isEmpty()) {
			if(TextUtil.isNumber(event.getArgs())) {
				int seed = TextUtil.getNumber(event.getArgs());
				
				Random rand = new Random(seed);
				
				String message = rand.nextInt(2) == 0 ? "Heads" : "Tails";
				
				event.reply("Flip! The coin flies through the air, lands and ends up on... **" + message + "**");
			}else {
				event.reply("A seed must be a number");
			}
		}else {
			Random rand = new Random();
			
			String message = rand.nextInt(2) == 0 ? "Heads" : "Tails";
			
			event.reply("Flip! The coin flies through the air, lands and ends up on... **" + message + "**");
		}
		
		event.getMessage().delete().queue();
	}
	
}
