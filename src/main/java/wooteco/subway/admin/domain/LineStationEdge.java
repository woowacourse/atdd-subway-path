package wooteco.subway.admin.domain;

import org.jgrapht.graph.DefaultWeightedEdge;

import java.util.function.Function;

public class LineStationEdge extends DefaultWeightedEdge {
    private LineStation lineStation;
    private Function<LineStation, Integer> weightStrategy;

    public LineStationEdge(LineStation lineStation, Function<LineStation, Integer> weightStrategy) {
        this.lineStation = lineStation;
        this.weightStrategy = weightStrategy;
    }

    @Override
    public double getWeight() {
        return weightStrategy.apply(lineStation);
    }

    public double getDistance() {
        return lineStation.getDistance();
    }

    public double getDuration() {
        return lineStation.getDuration();
    }
}
