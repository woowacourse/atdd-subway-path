package wooteco.subway.admin.domain;

import java.util.Objects;

import org.jgrapht.graph.DefaultWeightedEdge;

public class Edge extends DefaultWeightedEdge {
    private final LineStation lineStation;
    private final PathType pathType;

    public Edge(LineStation lineStation, PathType pathType) {
        validateLineStation(lineStation);
        validatePathType(pathType);
        this.lineStation = lineStation;
        this.pathType = pathType;
    }

    private void validatePathType(PathType pathType) {
        if (Objects.isNull(pathType)) {
            throw new IllegalArgumentException("PathType이 존재하지 않습니다.");
        }
    }

    private void validateLineStation(LineStation lineStation) {
        if (Objects.isNull(lineStation)) {
            throw new IllegalArgumentException("LineStation이 존재하지 않습니다.");
        }
    }

    public int getDuration() {
        return lineStation.getDuration();
    }

    public int getDistance() {
        return lineStation.getDistance();
    }

    @Override
    protected double getWeight() {
        return pathType.getWeight(lineStation);
    }
}
