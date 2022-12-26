package com.pavelkostal.api.constants;

public enum ResponseMessages {
	
	PHOTO_SAVED("Photo Saved"),
	INVALID_GPS("Invalid GPS coordination's"),
	INVALID_IMAGE("Invalid Image");
	
	private final String message;
	
	ResponseMessages(String message) {
		this.message = message;
	}
	
	@Override
	public String toString() {
		return message;
	}
}
