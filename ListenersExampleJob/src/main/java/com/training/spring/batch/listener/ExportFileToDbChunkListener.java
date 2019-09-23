package com.training.spring.batch.listener;

import org.springframework.batch.core.ChunkListener;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.stereotype.Component;

@Component
public class ExportFileToDbChunkListener implements ChunkListener {

	@Override
	public void beforeChunk(ChunkContext context) {
		System.out.println("------------Start of chunk------------");
	}

	@Override
	public void afterChunk(ChunkContext context) {
		System.out.println("------------End of chunk--------------");
	}

	@Override
	public void afterChunkError(ChunkContext context) {
		System.err.println("Error during chunk processing");
	}

}
