package com.tek.bb2.storage;

import java.io.IOException;
import java.io.InputStream;

import net.dv8tion.jda.core.entities.Message.Attachment;

public class CachedAttachment {
	
	private final static int MAX_BYTE_SIZE = 8388608;
	
	private String fileName;
	private byte[] byteContent;
	private int size;
	private boolean isTooBig;
	
	public CachedAttachment(Attachment attachment) {
		this.fileName = attachment.getFileName();
		
		try{
			InputStream is = attachment.getInputStream();
			
			this.size = is.available();
			
			if(this.size > MAX_BYTE_SIZE) {
				isTooBig = true;
				return;
			}
			
			isTooBig = false;
			
			this.byteContent = new byte[is.available()];
			
			is.read(this.byteContent);
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public String getFileName() {
		return fileName;
	}
	
	public byte[] getByteContent() {
		return byteContent;
	}
	
	public int getSize() {
		return size;
	}
	
	public boolean isTooBig() {
		return isTooBig;
	}
	
}
