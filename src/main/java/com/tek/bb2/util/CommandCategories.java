package com.tek.bb2.util;

import com.jagrosh.jdautilities.command.Command.Category;

public class CommandCategories {
	
	public static Category
		UTILITY,
		ROLES,
		GIVEAWAY,
		MODERATION,
		OWNER;
	
	static {
		UTILITY = new Category("Utility");
		ROLES = new Category("Roles");
		GIVEAWAY = new Category("Giveaway");
		MODERATION = new Category("Moderation");
		OWNER = new Category("Owner");
	}
	
}
