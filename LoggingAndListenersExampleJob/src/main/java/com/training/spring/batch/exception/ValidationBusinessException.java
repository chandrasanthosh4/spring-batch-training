package com.training.spring.batch.exception;

public class ValidationBusinessException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ValidationBusinessException() {
	}

	public ValidationBusinessException(String errorMessage) {
		super(errorMessage);
	}

}
