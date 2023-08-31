package org.eclipse.trace.coordinator.core.traceserver;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import org.eclipse.tsp.java.client.api.trace.Trace;
import org.eclipse.tsp.java.client.core.tspclient.TspClient;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import lombok.Getter;

public class TraceServer {
	private static int numberOfTraceServer = 0;
	@Getter
	private final int id;
	@Getter
	private URI uri;
	@Getter
	private String port;
	@Getter
	private List<Trace> traces;
	@Getter
	private TspClient tspClient;

	public TraceServer(String host, String port, List<Trace> traces) {
		this.id = TraceServer.numberOfTraceServer++;
		this.uri = URI.create(String.format("%s:%s", host, port));
		this.port = port;
		this.traces = traces == null ? new ArrayList<>() : traces;
		this.tspClient = new TspClient(this.uri.toString(), this.createExecutorService());
	}

	public String getHost() {
		return this.uri.getHost();
	}

	public Long encodeEntryId(Long entryId) throws ArithmeticException {
		final Integer step = (Integer.MAX_VALUE / TraceServer.numberOfTraceServer);
		if (entryId > step) {
			throw new ArithmeticException(
					String.format("The entry id is too big %d, it should be between [%d,%d[ for server %d", entryId,
							this.getLowerInterval(), this.getHigherInterval(), this.id));
		}
		return entryId + this.getLowerInterval();
	}

	public Long decodeEntryId(Long encodeEntryId) throws ArithmeticException {
		if (!isValidEncodeEntryId(encodeEntryId)) {
			throw new ArithmeticException(
					String.format("The encode entry id %d is not valid [%d,%d[ for server %d", encodeEntryId,
							this.getLowerInterval(), this.getHigherInterval(), this.id));
		}
		return encodeEntryId - this.getLowerInterval();
	}

	public boolean isValidEncodeEntryId(Long encodeEntryId) {
		return encodeEntryId >= this.getLowerInterval() && encodeEntryId < this.getHigherInterval();
	}

	private Long getLowerInterval() {
		final Long step = (Long.MAX_VALUE / TraceServer.numberOfTraceServer);
		return step * this.id;
	}

	private Long getHigherInterval() {
		final Long step = (Long.MAX_VALUE / TraceServer.numberOfTraceServer);
		return (step * (this.id + 1));
	}

	private ExecutorService createExecutorService() {
		ThreadFactory threadFactory = new ThreadFactoryBuilder()
				.setNameFormat(this.getHost().concat("-thread %d"))
				.setDaemon(false)
				.build();

		int threadPoolSize = System.getenv().containsKey("POOL_SIZE") ? Integer.parseInt(System.getenv("POOL_SIZE"))
				: 2;

		return Executors.newFixedThreadPool(threadPoolSize, threadFactory);
	}

}
