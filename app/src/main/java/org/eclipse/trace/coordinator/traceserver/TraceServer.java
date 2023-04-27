package org.eclipse.trace.coordinator.traceserver;

import java.net.URI;
import java.util.List;

import org.eclipse.tsp.java.client.core.tspclient.TspClient;

public class TraceServer {
	private static int NUMBER_OF_TRACE_SERVER = 0;

	private final int id;
	private URI uri;
	private String port;
	private List<String> tracesPath;
	private TspClient tspClient;

	public TraceServer(String host, String port, List<String> tracesPath) {
		this.uri = URI.create(String.format("%s:%s", host, port));
		this.port = port;
		this.tracesPath = tracesPath;

		this.tspClient = new TspClient(this.uri.toString());

		this.id = TraceServer.NUMBER_OF_TRACE_SERVER++;
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

	public int encodeEntryId(int entryId) throws ArithmeticException {
		final int step = (Integer.MAX_VALUE / TraceServer.NUMBER_OF_TRACE_SERVER);
		if (entryId > step) {
			throw new ArithmeticException(
					String.format("The entry id is too big %d, it should be between [%d,%d[ for server %d", entryId,
							this.getLowerInterval(), this.getHigherInterval(), this.id));
		}
		return entryId + step * this.id;
	}

	public int decodeEntryId(int encodeEntryId) throws ArithmeticException {
		if (!isValidEncodeEntryId(encodeEntryId)) {
			throw new ArithmeticException(
					String.format("The encode entry id %d is not valid [%d,%d[ for server %d", encodeEntryId,
							this.getLowerInterval(), this.getHigherInterval(), this.id));
		}
		return encodeEntryId - this.getLowerInterval();
	}

	public boolean isValidEncodeEntryId(int encodeEntryId) {
		return encodeEntryId >= this.getLowerInterval() && encodeEntryId < this.getHigherInterval();
	}

	private int getLowerInterval() {
		final int step = (Integer.MAX_VALUE / TraceServer.NUMBER_OF_TRACE_SERVER);
		return step * this.id;
	}

	private int getHigherInterval() {
		final int step = (Integer.MAX_VALUE / TraceServer.NUMBER_OF_TRACE_SERVER);
		return (step * (this.id + 1));
	}

}
