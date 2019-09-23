package com.training.spring.batch.exception;

public class CurruptFolderBusinessException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public CurruptFolderBusinessException() {
	}

	public CurruptFolderBusinessException(String errorMessage) {
		super(errorMessage);
	}

}
