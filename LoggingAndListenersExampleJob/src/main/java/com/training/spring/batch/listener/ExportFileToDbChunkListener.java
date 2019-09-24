package com.training.spring.batch.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ChunkListener;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.stereotype.Component;

@Component
public class ExportFileToDbChunkListener implements ChunkListener {
	
	public static final Logger LOGGER = LoggerFactory.getLogger(ExportFileToDbChunkListener.class);

	@Override
	public void beforeChunk(ChunkContext context) {
		LOGGER.info("------------Start of chunk------------");
	}

	@Override
	public void afterChunk(ChunkContext context) {
		LOGGER.info("------------End of chunk--------------");
	}

	@Override
	public void afterChunkError(ChunkContext context) {
		LOGGER.error("Error during chunk processing");
	}

}
