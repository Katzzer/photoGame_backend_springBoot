package com.pavelkostal.api.model;

public record ResponsePhotoSaved (
		Long id,
		String message
		) implements ResponsePhoto {}
