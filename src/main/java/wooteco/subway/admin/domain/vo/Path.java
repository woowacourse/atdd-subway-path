package wooteco.subway.admin.domain.vo;

import java.util.List;

import org.jgrapht.GraphPath;

import wooteco.subway.admin.domain.LineStationEdge;

public class Path {

    private final GraphPath<Long, LineStationEdge> path;

    private Path(GraphPath<Long, LineStationEdge> path) {
        this.path = path;
    }

    public static Path of(GraphPath<Long, LineStationEdge> path) {
        return new Path(path);
    }

    public List<Long> getVertexList() {
        return path.getVertexList();
    }

    public List<LineStationEdge> getEdgeList() {
        return path.getEdgeList();
    }
}
