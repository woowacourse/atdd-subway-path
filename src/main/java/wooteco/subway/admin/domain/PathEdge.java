package wooteco.subway.admin.domain;

import org.jgrapht.graph.DefaultWeightedEdge;

public class PathEdge extends DefaultWeightedEdge {
    private LineStation lineStation;
    private PathType pathType;

    private PathEdge(LineStation lineStation, PathType pathType) {
        this.lineStation = lineStation;
        this.pathType = pathType;
    }

    public static PathEdge of(LineStation lineStation, PathType pathType) {
        return new PathEdge(lineStation, pathType);
    }

    @Override
    protected double getWeight() {
        return pathType.getWeight(lineStation);
    }

    public LineStation getLineStation() {
        return lineStation;
    }

    public PathType getPathType() {
        return pathType;
    }
}
