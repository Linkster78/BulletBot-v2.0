package com.tek.bb2.bot.commands;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.AbstractMap.SimpleEntry;

import javax.imageio.ImageIO;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.tek.bb2.util.CommandCategories;
import com.tek.bb2.util.ImageToUTF;

public class Image2UTFCommand extends BotCommand{

	public Image2UTFCommand() {
		super("image2utf", "Transforms and renders a text version of your image", "<image url or attach image>", CommandCategories.FUN, null, "img2utf");
	}

	private int CHAR_SIZE = 8;
	private int MAX_IMAGE_SIZE = 256;
	
	@Override
	public void commandExecute(CommandEvent event) {
		if(!event.getArgs().isEmpty() && event.getArgs().split(" ").length == 1) {
			String argument = event.getArgs().split(" ")[0];
			
			try {
				URL url = new URL(argument);
				BufferedImage image = ImageIO.read(url);
				
				if(image != null) {
					followImage(event, image);
				}else {
					event.reply("The URL doesn't point to an image");
				}
			}catch(Exception e) {
				event.reply("You must specify a URL");
			}
		}else if(event.getArgs().isEmpty() && event.getMessage().getAttachments().size() == 1 && event.getMessage().getAttachments().get(0).isImage()) {
			try{
				byte[] contents = readBytes(event.getMessage().getAttachments().get(0).getInputStream());
				BufferedImage image = ImageIO.read(new ByteArrayInputStream(contents));
				followImage(event, image);
			}catch(Exception e) {
				e.printStackTrace();
				event.reply("Internal Error");
			}
		}else {
			event.reply("Invalid Syntax");
		}
		
		event.getMessage().delete().queue();
	}
	
	public void followImage(CommandEvent event, BufferedImage image) {
		new Thread(() -> {
			SimpleEntry<Integer, Integer> newSize = redimension(image);
			String utf = ImageToUTF.toUtf(image, newSize.getKey(), newSize.getValue());
			BufferedImage rendered = ImageToUTF.renderImage(utf, CHAR_SIZE);
			event.getTextChannel().sendMessage("Here is your image's UTF counterpart").queue();
			event.getTextChannel().sendFile(readBytes(rendered), "utf_version.png").queue();
		}).start();
	}
	
	public SimpleEntry<Integer, Integer> redimension(BufferedImage image){
		if(image.getWidth() <= MAX_IMAGE_SIZE && image.getHeight() <= MAX_IMAGE_SIZE) return new SimpleEntry<Integer, Integer>(image.getWidth(), image.getHeight());
		float ratio;
		ratio = (float) (image.getWidth() > image.getHeight() ? (double)MAX_IMAGE_SIZE / (double)image.getWidth() : (double)MAX_IMAGE_SIZE / (double)image.getHeight());
		int nwidth = (int) ((double)image.getWidth() * ratio);
		int nheight = (int) ((double)image.getHeight() * ratio);
		return new SimpleEntry<Integer, Integer>(nwidth, nheight);
	}
	
	public byte[] readBytes(InputStream is) {
		try{
			byte[] byteContent = new byte[is.available()];
			
			is.read(byteContent);
			
			return byteContent;
		}catch(Exception e) {
			return new byte[0];
		}
	}

	public byte[] readBytes(BufferedImage image) {
		try{
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ImageIO.write(image, "png", bos);
			return bos.toByteArray();
		}catch(Exception e) { return new byte[0]; }
	}
	
}
