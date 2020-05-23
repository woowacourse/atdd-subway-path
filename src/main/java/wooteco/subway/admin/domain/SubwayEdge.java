package wooteco.subway.admin.domain;

import java.util.Objects;

import org.jgrapht.graph.DefaultWeightedEdge;

public class SubwayEdge extends DefaultWeightedEdge {
    private final LineStation lineStation;
    private final PathType pathType;

    public SubwayEdge(LineStation lineStation, PathType pathType) {
        this.lineStation = lineStation;
        this.pathType = pathType;
    }

    public int getDuration() {
        if (Objects.isNull(lineStation)) {
            return -1;
        }
        return lineStation.getDuration();
    }

    public int getDistance() {
        if (Objects.isNull(lineStation)) {
            return -1;
        }
        return lineStation.getDistance();
    }

    @Override
    protected double getWeight() {
        return pathType.getWeight(lineStation);
    }
}
