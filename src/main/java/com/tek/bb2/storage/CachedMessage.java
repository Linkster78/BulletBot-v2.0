package com.tek.bb2.storage;

import java.util.List;
import java.util.stream.Collectors;

import net.dv8tion.jda.core.entities.Message;

public class CachedMessage {
	
	private String id, authorId;
	private String message;
	private List<CachedAttachment> attachments;
	
	public CachedMessage(Message message) {
		this.id = message.getId();
		this.authorId = message.getAuthor().getId();
		this.message = message.getContentRaw();
		this.attachments = message.getAttachments().stream().map(attachment -> new CachedAttachment(attachment)).collect(Collectors.toList());
	}
	
	public String getId() {
		return id;
	}
	
	public String getAuthorId() {
		return authorId;
	}
	
	public String getMessage() {
		return message;
	}
	
	public List<CachedAttachment> getAttachments() {
		return attachments;
	}
	
}
