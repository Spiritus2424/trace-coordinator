package org.eclipse.trace.coordinator.experiment;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.eclipse.tsp.java.client.models.experiment.Experiment;
import org.eclipse.tsp.java.client.models.indexing.IndexingStatus;
import org.eclipse.tsp.java.client.models.trace.Trace;

public class DistributedExperiment {
    private UUID uuid;
    private String name;
    private List<String> tracesUuid;

    public DistributedExperiment() {
        this.name = "";
        this.uuid = UUID.randomUUID();
        this.tracesUuid = new ArrayList<>();
    }

    public DistributedExperiment(String name) {
        this.name = name;
        this.uuid = UUID.randomUUID();
        this.tracesUuid = new ArrayList<>();
    }

    public DistributedExperiment(String name, List<String> tracesUuid) {
        this.name = name;
        this.tracesUuid = tracesUuid;
        this.uuid = UUID.fromString(tracesUuid.stream()
                .map(traceUuid -> traceUuid.toString())
                .reduce("", (String partialUuid, String traceUuid) -> partialUuid.concat(traceUuid)));

    }

    public UUID getUuid() {
        return this.uuid;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getTracesUuid() {
        return tracesUuid;
    }

    public BigInteger getStart() {
        throw new UnsupportedOperationException();
        // BigInteger start = experiments.get(0).getStart();

        // for (Experiment experiment : experiments) {
        // if (start.compareTo(experiment.getStart()) == 1) {
        // start = experiment.getStart();
        // }
        // }
        // return start;
    }

    public BigInteger getEnd() {
        throw new UnsupportedOperationException();
        // BigInteger end = experiments.get(0).getStart();

        // for (Experiment experiment : experiments) {
        // if (end.compareTo(experiment.getStart()) == -1) {
        // end = experiment.getStart();
        // }
        // }
        // return end;
    }

    public int getNbEvents() {
        throw new UnsupportedOperationException();
        // int nbEvents = 0;

        // for (Experiment experiment : experiments) {
        // nbEvents += experiment.getNbEvents();
        // }
        // return nbEvents;
    }

    public IndexingStatus getIndexingStatus() {
        throw new UnsupportedOperationException();
        // IndexingStatus indexingStatus = null;
        // for (Experiment experiment : experiments) {
        // if (experiment.getIndexingStatus() == IndexingStatus.CLOSED) {
        // indexingStatus = IndexingStatus.CLOSED;
        // break;
        // }
        // }

        // return indexingStatus;
    }

    public List<Trace> getTraces() {
        throw new UnsupportedOperationException();
        // List<Trace> traces = new ArrayList<>();
        // for (Experiment experiment : experiments) {
        // traces.addAll(experiment.getTraces());
        // }

        // return traces;
    }

    public Experiment toExperiment() {
        return new Experiment(getUuid().toString(), getName(), getStart(), getEnd(), getNbEvents(), getIndexingStatus(),
                getTraces());
    }
}
