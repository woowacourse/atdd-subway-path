package wooteco.subway.admin.domain.jgrapht;


import java.util.function.Function;
import org.jgrapht.graph.DefaultWeightedEdge;
import wooteco.subway.admin.domain.Edge;
import wooteco.subway.admin.domain.LineStation;

public class SubwayEdge extends DefaultWeightedEdge implements Edge {
    private final LineStation lineStation;
    private final Function<LineStation, Integer> weightStrategy;

    public SubwayEdge(LineStation lineStation,
            Function<LineStation, Integer> weightStrategy) {
        this.lineStation = lineStation;
        this.weightStrategy = weightStrategy;
    }

    @Override
    public double getWeight() {
        return weightStrategy.apply(lineStation);
    }

    @Override
    public int getDistance() {
        return lineStation.getDistance();
    }

    @Override
    public int getDuration() {
        return lineStation.getDuration();
    }
}