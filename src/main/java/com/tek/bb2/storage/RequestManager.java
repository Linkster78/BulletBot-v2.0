package com.tek.bb2.storage;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Optional;

import com.tek.bb2.addons.CustomMenu;
import com.tek.bb2.bot.BulletBot;

import net.dv8tion.jda.core.entities.User;

public class RequestManager {
	
	private String userId;
	
	private ArrayList<UserRequest> userRequests;
	
	private String EMOTE_REFRESH = "🔁";
	private String EMOTE_NEXT    = "⏭";
	private String EMOTE_CLEAR   = "🔴";
	private String EMOTE_EXIT    = "💥";
	
	public RequestManager() {
		userRequests = new ArrayList<UserRequest>();
	}
	
	public void openFor(String userId) {
		this.userId = userId;
		
		CustomMenu.Builder menuBuilder = new CustomMenu.Builder();
		menuBuilder.setColor(Color.cyan);
		menuBuilder.setEventWaiter(BulletBot.getInstance().getEventWaiter());
		menuBuilder.setText(buildText());
		menuBuilder.addChoice(EMOTE_REFRESH);
		menuBuilder.addChoice(EMOTE_NEXT);
		menuBuilder.addChoice(EMOTE_CLEAR);
		menuBuilder.addChoice(EMOTE_EXIT);
		menuBuilder.setAction((re, menu) -> {
			switch(re.getName()) {
				case "🔁":
					menu.setText(buildText());
					break;
				case "⏭":
					if(userRequests.size() > 0) userRequests.remove(0);
					menu.setText(buildText());
					break;
				case "🔴":
					userRequests.clear();
					menu.setText(buildText());
					break;
				case "💥":
					menu.terminate();
					BulletBot.getInstance().getStorage().getRequestManager().close();
					menu.setText("**Exited**");
					break;
				default:
					break;
			}
		});
		
		CustomMenu queuedMenu = menuBuilder.build();
		
		User user = BulletBot.getInstance().getJda().getUserById(userId);
		
		user.openPrivateChannel().queue(pc -> {
			queuedMenu.display(pc);
		});
	}
	
	public String buildText() {
		String text = "";
		
		Optional<UserRequest> firstRequest = getRequestAtPosition(0);
		text += ("**Current Request:**\n" + (firstRequest.isPresent() ? firstRequest.get().toString() : "None") + "\n\n**Queued Requests:**\n");
		
		for(int i = 0; i < 10; i++) {
			Optional<UserRequest> request = getRequestAtPosition(i + 1);
			String requestText = "**" + (i + 1) + ".** " + (request.isPresent() ? request.get().toString() : "None");
			text += (requestText + "\n");
		}
		
		text += ("\n" + buildHelp());
		
		return text;
	}
	
	public Optional<UserRequest> getRequestAtPosition(int pos) {
		return userRequests.size() - 1 < pos ?  Optional.empty() : Optional.of(userRequests.get(pos));
	}
	
	public Optional<UserRequest> getRequestById(String id) {
		return userRequests.stream().filter(ur -> ur.getUserId().equals(id)).findFirst();
	}
	
	public String buildHelp() {
		return EMOTE_REFRESH + " **Refresh requests**\n" + EMOTE_NEXT + " **Next request**\n" + EMOTE_CLEAR + " **Clear requests**\n" + EMOTE_EXIT + " **Exit menu**";
	}
	
	public void close() {
		this.userId = null;
	}
	
	public boolean isOpen() {
		return userId != null;
	}
	
	public String getUserId() {
		return userId;
	}
	
	public ArrayList<UserRequest> getUserRequests() {
		return userRequests;
	}
	
}
