package com.tvd12.ezydata.example.common;

import java.time.LocalDateTime;

import java.time.*;
import java.util.*;

public final class DateConverter {
	
	private DateConverter() {}

	public static Date toDate(LocalDateTime dateTime) {
		return Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant());
	}
	
	public static LocalDate toLocalDate(Date date) {
	    return ZonedDateTime
	        .ofInstant(date.toInstant(), ZoneId.systemDefault())
	        .toLocalDate();
	}

	public static LocalDateTime toLocalDateTime(Date date) {
		return ZonedDateTime
	        .ofInstant(date.toInstant(), ZoneId.systemDefault())
	        .toLocalDateTime();
	}
}
