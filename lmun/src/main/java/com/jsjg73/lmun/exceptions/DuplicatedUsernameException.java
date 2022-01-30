package com.jsjg73.lmun.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class DuplicatedUsernameException extends RuntimeException {
	public DuplicatedUsernameException(String msg) {
		super(msg);
	}
}