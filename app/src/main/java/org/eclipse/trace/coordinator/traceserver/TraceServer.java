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
            throw new ArithmeticException("The entry id is too big");
        }
        return entryId + step * this.id;
    }

    public int decodeEntryId(int encodeEntryId) throws ArithmeticException {
        if (!isValidEncodeEntryId(encodeEntryId)) {
            throw new ArithmeticException("The encode entry id is not valid");
        }
        final int step = (Integer.MAX_VALUE / TraceServer.NUMBER_OF_TRACE_SERVER);
        return encodeEntryId - step * this.id;
    }

    private boolean isValidEncodeEntryId(int encodeEntryId) {
        final int step = (Integer.MAX_VALUE / TraceServer.NUMBER_OF_TRACE_SERVER);
        return encodeEntryId >= step * this.id && encodeEntryId < step * (this.id + 1);
    }

}
