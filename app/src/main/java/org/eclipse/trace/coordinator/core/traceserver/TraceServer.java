package org.eclipse.trace.coordinator.core.traceserver;

import java.net.URI;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import org.eclipse.tsp.java.client.core.tspclient.TspClient;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

public class TraceServer {
	private static int numberOfTraceServer = 0;

	private final int id;
	private URI uri;
	private String port;
	private List<String> tracesPath;
	private TspClient tspClient;

	public TraceServer(String host, String port, List<String> tracesPath) {
		this.uri = URI.create(String.format("%s:%s", host, port));
		this.port = port;
		this.tracesPath = tracesPath;

		ThreadFactory threadFactory = new ThreadFactoryBuilder()
				.setNameFormat(this.getHost().concat("-thread %d"))
				.setDaemon(false)
				.build();

		int threadPoolSize = System.getenv().containsKey("POOL_SIZE") ? Integer.parseInt(System.getenv("POOL_SIZE"))
				: 2;

		this.tspClient = new TspClient(this.uri.toString(),
				Executors.newFixedThreadPool(threadPoolSize, threadFactory));
		this.id = TraceServer.numberOfTraceServer++;
	}

	public int getId() {
		return this.id;
	}

	public URI getUri() {
		return this.uri;
	}

	public String getHost() {
		return this.uri.getHost();
	}

	public String getPort() {
		return this.port;
	}

	public List<String> getTracesPath() {
		return this.tracesPath;
	}

	public TspClient getTspClient() {
		return tspClient;
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

}
