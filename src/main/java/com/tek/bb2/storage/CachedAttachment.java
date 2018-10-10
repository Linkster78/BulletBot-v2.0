package com.tek.bb2.storage;

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
			
			this.byteContent = readBytes(is);
		}catch(Exception e) {
			this.size = 0;
			this.byteContent = new byte[0];
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
	
	public byte[] readBytes(InputStream is) {
		try{
			int size = is.available();
		
			if(size > MAX_BYTE_SIZE) {
				return new byte[0];
			}
			
			byte[] byteContent = new byte[is.available()];
			
			is.read(byteContent);
			
			return byteContent;
		}catch(Exception e) {
			return new byte[0];
		}
	}
	
}
