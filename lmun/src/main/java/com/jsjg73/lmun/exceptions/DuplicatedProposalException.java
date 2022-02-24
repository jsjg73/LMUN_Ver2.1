package com.jsjg73.lmun.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class DuplicatedProposalException extends RuntimeException {
	public DuplicatedProposalException(String msg) {
		super(msg);
	}
}