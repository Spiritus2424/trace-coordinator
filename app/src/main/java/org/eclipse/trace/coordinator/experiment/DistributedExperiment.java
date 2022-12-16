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
    private List<Experiment> experiments;

    public DistributedExperiment() {
        this.uuid = UUID.randomUUID();
        this.experiments = new ArrayList<>();
    }

    public DistributedExperiment(String name) {
        this.name = name;
        this.uuid = UUID.randomUUID();
        this.experiments = new ArrayList<>();
    }

    public DistributedExperiment(String name, List<Experiment> experiments) {
        this.name = name;
        this.experiments = experiments;
        this.uuid = UUID.fromString(experiments.stream().map(experiment -> experiment.getUuid()).reduce("",
                (String partialUuid, String experimentUuid) -> partialUuid.concat(experimentUuid)));

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

    public BigInteger getStart() {
        BigInteger start = experiments.get(0).getStart();

        for (Experiment experiment : experiments) {
            if (start.compareTo(experiment.getStart()) == 1) {
                start = experiment.getStart();
            }
        }
        return start;
    }

    public BigInteger getEnd() {
        BigInteger end = experiments.get(0).getStart();

        for (Experiment experiment : experiments) {
            if (end.compareTo(experiment.getStart()) == -1) {
                end = experiment.getStart();
            }
        }
        return end;
    }

    public int getNbEvents() {
        int nbEvents = 0;

        for (Experiment experiment : experiments) {
            nbEvents += experiment.getNbEvents();
        }
        return nbEvents;
    }

    public IndexingStatus getIndexingStatus() {
        IndexingStatus indexingStatus = null;
        for (Experiment experiment : experiments) {
            if (experiment.getIndexingStatus() == IndexingStatus.CLOSED) {
                indexingStatus = IndexingStatus.CLOSED;
                break;
            }
        }

        return indexingStatus;
    }

    public List<Trace> getTraces() {
        List<Trace> traces = new ArrayList<>();
        for (Experiment experiment : experiments) {
            traces.addAll(experiment.getTraces());
        }

        return traces;
    }

    public List<Experiment> getExperiments() {
        return experiments;
    }

    public boolean addExperiment(Experiment experiment) {
        return this.experiments.add(experiment);
    }

    public Experiment toExperiment() {
        return new Experiment(getUuid().toString(), getName(), getStart(), getEnd(), getNbEvents(), getIndexingStatus(),
                getTraces());
    }
}
