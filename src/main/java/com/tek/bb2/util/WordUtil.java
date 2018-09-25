package com.tek.bb2.util;

public class WordUtil {
	
	public static String capitalize(String sentence) {
		return removeUnderscores(sentence.substring(0, 1).toUpperCase() + sentence.toLowerCase().substring(1, sentence.length()));
	}
	
	public static String removeUnderscores(String sentence) {
		return sentence.replaceAll("_", " ");
	}
	
}
