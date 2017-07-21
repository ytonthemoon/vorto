package org.eclipse.vorto.repository.web.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
public class StreamCopyException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1231520000810039747L;

	public StreamCopyException(String message, Throwable cause) {
		super(message, cause);
	}

}