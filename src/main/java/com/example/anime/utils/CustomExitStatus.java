package com.example.anime.utils;

import lombok.Getter;

@Getter
public enum CustomExitStatus {
	STATUS_COMPLETED("STATUS_COMPLETED"), STATUS_FAILED("STATUS_FAILED"), PARTIALLY_COMPLETED("PARTIALLY_COMPLETED"),
	NO_CONTENT("NO_CONTENT");
	;

	private String status;

	CustomExitStatus(String status) {
		this.status = status;
	}
}
