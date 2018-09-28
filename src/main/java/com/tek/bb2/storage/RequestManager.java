package com.tek.bb2.storage;

import java.awt.Color;
import java.util.ArrayList;

import com.tek.bb2.addons.CustomMenu;
import com.tek.bb2.bot.BulletBot;
import com.tek.bb2.util.TextUtil;

import net.dv8tion.jda.core.entities.User;

public class RequestManager {
	
	private String userId;
	
	private ArrayList<UserRequest> userRequests;
	
	public void openFor(String userId) {
		this.userId = userId;
		
		CustomMenu.Builder menuBuilder = new CustomMenu.Builder();
		menuBuilder.setColor(Color.cyan);
		menuBuilder.setEventWaiter(BulletBot.getInstance().getEventWaiter());
		menuBuilder.addChoice("⏭");
		menuBuilder.addChoice("🔴");
		menuBuilder.setAction((re, menu) -> {
			int num = TextUtil.getNumber(menu.getText());
			
			if(re.getName().equals("⏭")) {
				num++;
			}else if(re.getName().equals("🔴")){
				
			}
			
			menu.setText(num + "");
		});
		
		CustomMenu queuedMenu = menuBuilder.build();
		
		User user = BulletBot.getInstance().getJda().getUserById(userId);
		
		user.openPrivateChannel().queue(pc -> {
			queuedMenu.display(pc);
		});
	}
	
	public void close() {
		this.userId = null;
	}
	
	public String getUserId() {
		return userId;
	}
	
	public ArrayList<UserRequest> getUserRequests() {
		return userRequests;
	}
	
}
