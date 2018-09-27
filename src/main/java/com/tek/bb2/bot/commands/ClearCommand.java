package com.tek.bb2.bot.commands;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.tek.bb2.util.CommandCategories;
import com.tek.bb2.util.TextUtil;

import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;

public class ClearCommand extends BotCommand{

	public ClearCommand() {
		super("clear", "Clears a set amount of messages", "<quantity> [@mention]", CommandCategories.MODERATION, null);
		
		this.userPermissions = new Permission[] {Permission.MESSAGE_MANAGE};
	}

	@Override
	public void commandExecute(CommandEvent event) {
		String[] args = event.getArgs().split(" ");
		
		if(args.length == 1) {
			if(TextUtil.isNumber(args[0])) {
				int quantity = TextUtil.getNumber(args[0]);
				
				if(quantity > 0) {
					getLatestMessages(event.getTextChannel(), event.getMessage().getId(), quantity, Optional.empty(), (Consumer<List<Message>>)((lm) -> {
						event.getTextChannel().deleteMessages(lm).queue();
					}));
				}else {
					event.reply("The quantity must be over 0");
				}
			}else {
				event.reply("This isn't a valid quantity");
			}
		}else if(args.length == 2) {
			if(TextUtil.isNumber(args[0])) {
				int quantity = TextUtil.getNumber(args[0]);
				
				if(quantity > 0) {
					if(event.getMessage().getMentionedMembers().size() == 1) {
						Member member = event.getMessage().getMentionedMembers().get(0);
						
						if(args[1].length() == member.getAsMention().length()) {
							getLatestMessages(event.getTextChannel(), event.getMessage().getId(), quantity, Optional.of(member.getUser()), (Consumer<List<Message>>)((lm) -> {
								event.getTextChannel().deleteMessages(lm).queue();
							}));
						}else {
							event.reply("The second argument is malformed");
						}
					}else {
						event.reply("You have not mentioned a user");
					}
				}else {
					event.reply("The quantity must be over 0");
				}
			}else {
				event.reply("This isn't a valid quantity");
			}
		}else {
			event.reply("Invalid arguments");
		}
		
		event.getMessage().delete().queue();
	}
	
	public void getLatestMessages(TextChannel channel, String excludeId, int quantity, Optional<User> user, Consumer<List<Message>> messageConsumer) {
		channel.getIterableHistory().cache(false).queue(lm -> {
			List<Message> messages = lm.stream().filter(message -> {
				return (user.isPresent() ? message.getAuthor().getId().equals(user.get().getId()) : true);
			}).filter(m -> !m.getId().equals(excludeId)).limit(quantity).collect(Collectors.toList());
			
			messageConsumer.accept(messages);
		});
	}

}
