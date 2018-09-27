package com.tek.bb2.util;

public class TextUtil {
	
	public static String capitalize(String text) {
		return removeUnderscores(text.substring(0, 1).toUpperCase() + text.toLowerCase().substring(1, text.length()));
	}
	
	public static String removeUnderscores(String text) {
		return text.replaceAll("_", " ");
	}
	
	public static String humanReadableByteCount(long bytes) {
	    int unit = 1000;
	    if (bytes < unit) return bytes + " B";
	    int exp = (int) (Math.log(bytes) / Math.log(unit));
	    String pre = "kMGTPE".charAt(exp-1) + "i";
	    return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
	}
	
	public static boolean isNumber(String text) {
		try {
			Integer.parseInt(text);
			return true;
		}catch(Exception e) { 
			return false;
		}
	}
	
	public static int getNumber(String text) {
		try {
			return Integer.parseInt(text);
		}catch(Exception e) {
			return -1;
		}
	}
	
}
