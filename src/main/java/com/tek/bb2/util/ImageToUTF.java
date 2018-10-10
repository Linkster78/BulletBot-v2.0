package com.tek.bb2.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;

/*
 * ImageToUTF.class by Link
 * Utility class to perform a bunch
 * of different utf/image operations
 */

public class ImageToUTF {
	
	public static HashMap<Character, Integer> brightnessTable = new HashMap<Character, Integer>();
	
	public static String toUtf(BufferedImage image, int outX, int outY) {
		if(image == null || outX == 0 || outY == 0 || outX < 0 || outY < 0) return null;
		
		int inX = image.getWidth();
		int inY = image.getHeight();
		
		if(outX > inX || outY > inY) return null;
		
		int raX = inX / outX;
		int raY = inY / outY;
		
		StringBuilder builder = new StringBuilder();
		
		for(int y = 0; y < inY; y += raY) {
			for(int x = 0; x < inX; x += raX) {
				int areaBrightness = 0;
				int i = 0;
				
				for(int px = x - raX; px < x; px++) {
					for(int py = y - raY; py < y; py++) {
						int clr   = image.getRGB(x,y); 
						int red   = (clr & 0x00ff0000) >> 16;
						int green = (clr & 0x0000ff00) >> 8;
						int blue  = (clr & 0x000000ff);
						
						Color color = new Color(red, green, blue);
						
						areaBrightness += brightness(color);
						
						i++;
					}
				}
				
				areaBrightness /= i;
				
				char processed;
				
				processed = cbrightness(areaBrightness);
				
				if(processed == (char)999) {
					processed = ArtChar.charFrom(areaBrightness);
				}
				
				builder.append(processed);
			}
			
			builder.append(System.lineSeparator());
		}
		
		return builder.toString().substring(0, builder.toString().length() - System.lineSeparator().length());
	}
	
	public static BufferedImage renderImage(String utf, int charSize) {
		int width = utf.split("\n")[0].length();
		int height = utf.split("\n").length;
		
		BufferedImage rendered = new BufferedImage(width * charSize, height * charSize, BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics = (Graphics2D) rendered.getGraphics();
		
		graphics.setColor(Color.white);
		graphics.fillRect(0, 0, rendered.getWidth(), rendered.getHeight());
		
		graphics.setColor(Color.black);
		graphics.setFont(new Font(graphics.getFont().getFontName(), Font.PLAIN, charSize));
		
		int offX = 0;
		int offY = 0;
		
		for(String ch : utf.split("")) {
			if(ch.equals("\n")) {
				offX = 0;
				offY += charSize;
				continue;
			}
			
			graphics.drawString(ch, offX, offY);
			
			offX += charSize;
		}
		
		return rendered;
	}
	
	public static void loadBrightnessTable() {
		int highest = 0;
		
		for(int i = 33; i <= 254; i++) {
			char c = (char)i;
			
			BufferedImage bimage = new BufferedImage(16, 16,
				    BufferedImage.TYPE_BYTE_INDEXED);
			
			Graphics2D g2d = bimage.createGraphics();
			
			g2d.setColor(Color.WHITE);
			g2d.fillRect(0, 0, bimage.getWidth(), bimage.getHeight());
			
			g2d.setColor(Color.BLACK);
			g2d.drawString("" + c, 0, 16);
			
		    g2d.dispose();
		    
		    int brightnessArea = 0;
		    int iter = 0;
		    
		    for(int x = 0; x < bimage.getWidth(); x++) {
		    	for(int y = 0; y < bimage.getHeight(); y++) {
		    		int clr   = bimage.getRGB(x,y); 
					int red   = (clr & 0x00ff0000) >> 16;
					int green = (clr & 0x0000ff00) >> 8;
					int blue  = (clr & 0x000000ff);
					
					Color ci = new Color(red, green, blue);
					
					brightnessArea += brightness(ci);
		    		
		    		iter++;
		    	}
		    }
		    
		    brightnessArea /= iter;
		    
		    if(brightnessArea > highest) highest = brightnessArea;
		    
		    brightnessTable.put(c, brightnessArea);
		}
		
		for(char c : brightnessTable.keySet()) {
			double bn = brightnessTable.get(c);
			bn = 255 - (bn / highest * 255);
			brightnessTable.put(c, (int)bn);
		}
	}
	
	public static char cbrightness(int brightness) {
		for(char c : brightnessTable.keySet()) {
			if(brightnessTable.get(c).equals(brightness)) return c;
		}
		
		return (char)999;
	}
	
	public static int brightness(Color c)
	{
	   return (int)Math.sqrt(
	      c.getRed() * c.getRed() * .241 + 
	      c.getGreen() * c.getGreen() * .691 + 
	      c.getBlue() * c.getBlue() * .068);
	}
	
	public enum ArtChar{
		a(0, 40, '#'),
		b(41, 80, 'O'),
		c(81, 120, 'C'),
		d(121, 160, '/'),
		e(161, 200, '~'),
		f(201, 240, ','),
		g(241, 255, '.');
		
		public int from, to;
		public char ch;
		
		ArtChar(int from, int to, char ch){
			this.from = from;
			this.to = to;
			this.ch = ch;
		}
		
		public static char charFrom(int brightness) {
			for(ArtChar ac : ArtChar.values()) {
				if(brightness >= ac.from && brightness <= ac.to) return ac.ch;
			}
			
			return ' ';
		}
	}
	
}