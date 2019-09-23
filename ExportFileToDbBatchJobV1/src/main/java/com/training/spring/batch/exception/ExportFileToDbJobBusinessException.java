package com.training.spring.batch.exception;

public class ExportFileToDbJobBusinessException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ExportFileToDbJobBusinessException() {
	}

	public ExportFileToDbJobBusinessException(String errorMessage) {
		super(errorMessage);
	}

}
