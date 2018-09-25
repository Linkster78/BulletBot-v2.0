package com.tek.bb2.bot.commands;

import com.jagrosh.jdautilities.command.Command.Category;

public class CommandCategories {
	
	public static Category
		UTILITY,
		GIVEAWAY,
		ROLES;
	
	static {
		UTILITY = new Category("Utility");
		GIVEAWAY = new Category("Giveaway");
		ROLES = new Category("Roles");
	}
	
}
