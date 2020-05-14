package wooteco.subway.admin.domain;

import org.jgrapht.graph.DefaultWeightedEdge;

import java.util.function.Function;

public class LineStationEdge extends DefaultWeightedEdge {
    private LineStation lineStation;
    private Function<LineStation, Integer> edgeStrategy;

    public LineStationEdge(LineStation lineStation, Function<LineStation, Integer> edgeStrategy) {
        this.lineStation = lineStation;
        this.edgeStrategy = edgeStrategy;
    }

    @Override
    public double getWeight() {
        return edgeStrategy.apply(lineStation);
    }

    public double getDistance() {
        return lineStation.getDistance();
    }

    public double getDuration() {
        return lineStation.getDuration();
    }
}
