package com.nagarro.ticketapp.server.exceptions;

public class ApiRequestException extends RuntimeException {

	private static final long serialVersionUID = 2002083367010601217L;

	public ApiRequestException(String message) {
		super(message);
	}

}
