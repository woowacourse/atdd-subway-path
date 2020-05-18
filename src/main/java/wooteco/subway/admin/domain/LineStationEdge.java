package wooteco.subway.admin.domain;

import java.util.function.Function;
import org.jgrapht.graph.DefaultWeightedEdge;

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

    public int getDistance() {
        return lineStation.getDistance();
    }

    public int getDuration() {
        return lineStation.getDuration();
    }
}
