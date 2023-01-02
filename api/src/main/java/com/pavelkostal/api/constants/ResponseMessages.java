package com.pavelkostal.api.constants;

public enum ResponseMessages {
	
	PHOTO_SAVED("Photo Saved"),
	NO_GPS("No GPS data"),
	INVALID_GPS("Invalid GPS coordination's"),
	INVALID_GPS_AT_CITY("Invalid GPS coordination's at selected city"),
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
