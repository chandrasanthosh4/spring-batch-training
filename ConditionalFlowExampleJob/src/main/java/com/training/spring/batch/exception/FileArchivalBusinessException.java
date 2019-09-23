package com.training.spring.batch.exception;

public class FileArchivalBusinessException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public FileArchivalBusinessException() {
	}

	public FileArchivalBusinessException(String errorMessage) {
		super(errorMessage);
	}

}
