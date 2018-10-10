package com.tek.bb2.util;

import com.jagrosh.jdautilities.command.Command.Category;

public class CommandCategories {
	
	public static Category
		UTILITY,
		FUN,
		ROLES,
		WEB,
		GIVEAWAY,
		MODERATION,
		OWNER;
	
	static {
		UTILITY = new Category("Utility");
		FUN = new Category("Fun");
		ROLES = new Category("Roles");
		WEB = new Category("Web");
		GIVEAWAY = new Category("Giveaway");
		MODERATION = new Category("Moderation");
		OWNER = new Category("Owner");
	}
	
}
