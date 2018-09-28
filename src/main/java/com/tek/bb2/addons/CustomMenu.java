package com.tek.bb2.addons;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import com.jagrosh.jdautilities.menu.Menu;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Emote;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.MessageReaction.ReactionEmote;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.core.requests.RestAction;
import net.dv8tion.jda.core.utils.Checks;

public class CustomMenu extends Menu {
	
	private final Color color;
	private final List<String> choices;
	private final BiConsumer<ReactionEmote, CustomMenu> action;
	private final Consumer<Message> finalAction;
	private String messageId, text;
	
	protected CustomMenu(EventWaiter waiter, Set<User> users, Set<Role> roles, long timeout, TimeUnit unit, Color color, List<String> choices, BiConsumer<ReactionEmote, CustomMenu> action, Consumer<Message> finalAction) {
		super(waiter, users, roles, timeout, unit);
		this.color = color;
		this.choices = choices;
		this.action = action;
		this.finalAction = finalAction;
		this.text = "1";
	}

	@Override
	public void display(MessageChannel channel) {
		initialize(channel.sendMessage(getMessage()));
	}

	@Override
	public void display(Message message) {
		initialize(message.editMessage(getMessage()));
	}
	
	public void edit(Message message) {
		message.editMessage(getMessage()).queue();
	}
	
	private void initialize(RestAction<Message> ra) {
		ra.queue(m -> {
			this.messageId = m.getId();
			
			for(int i = 0; i < choices.size(); i++) {
				Emote emote;
				try {
					emote = m.getJDA().getEmoteById(choices.get(i));
				}catch(Exception e) {
					emote = null;
				}
				
				RestAction<Void> r = emote == null ? m.addReaction(choices.get(i)) : m.addReaction(emote);
				if(i + 1 < choices.size()) {
					r.queue();
				}else {
					r.queue(v -> {
						queueReactionEvent(m);
					});
				}
			}
		});
	}
	
	private long lastEvent = System.currentTimeMillis();
	
	public void queueReactionEvent(Message m) {
		waiter.waitForEvent(MessageReactionAddEvent.class, event -> {
			MessageReactionAddEvent e = (MessageReactionAddEvent) event;
			
			if(!e.getMessageId().equals(m.getId())) return false;
			
			String re = e.getReaction().getReactionEmote().isEmote() ? e.getReaction().getReactionEmote().getId() : e.getReaction().getReactionEmote().getName();
			
			if(!choices.contains(re)) return false;
			
			if((System.currentTimeMillis() - lastEvent) < 500) return false;
			
			return isValidUser(e.getUser(), e.getGuild());
		}, (MessageReactionAddEvent event) -> {
			action.accept(event.getReaction().getReactionEmote(), this);
			
			display(m);
			
			this.lastEvent = System.currentTimeMillis();
			
			queueReactionEvent(m);
		}, timeout, unit, () -> finalAction.accept(m));
	}
	
	private Message getMessage()
    {
        MessageBuilder mbuilder = new MessageBuilder();
        mbuilder.setEmbed(new EmbedBuilder().setColor(color).setDescription("**NOTE: There is a 500ms cooldown between clicks**\n\n" + text).build());
        return mbuilder.build();
    }
	
	public void setText(String text) {
		this.text = text;
	}
	
	public Color getColor() {
		return color;
	}
	
	public List<String> getChoices() {
		return choices;
	}
	
	public String getMessageId() {
		return messageId;
	}
	
	public String getText() {
		return text;
	}
	
	public static class Builder extends Menu.Builder<Builder, CustomMenu> {

		private Color color;
		private List<String> choices = new ArrayList<String>();
		private BiConsumer<ReactionEmote, CustomMenu> action;
		private Consumer<Message> finalAction = (m) -> {};
		
		@Override
		public CustomMenu build() {
			Checks.check(waiter != null, "Must have an EventWaiter");
			
			return new CustomMenu(waiter, users, roles, timeout, unit, color, choices, action, finalAction);
		}
		
		public Builder setColor(Color color) {
			this.color = color;
			return this;
		}
		
		public Builder setAction(BiConsumer<ReactionEmote, CustomMenu> action) {
			this.action = action;
			return this;
		}
		
		public Builder setFinalAction(Consumer<Message> finalAction) {
			this.finalAction = finalAction;
			return this;
		}
		
		public Builder addChoice(String emoji)
        {
            this.choices.add(emoji);
            return this;
        }
	}

}
