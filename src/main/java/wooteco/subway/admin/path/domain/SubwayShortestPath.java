package wooteco.subway.admin.path.domain;

import java.util.List;

import org.jgrapht.GraphPath;

import wooteco.subway.admin.common.exception.InvalidSubwayPathException;
import wooteco.subway.admin.station.domain.Station;

public class SubwayShortestPath {

    private final GraphPath<Station, SubwayWeightedEdge> graphPath;

    public SubwayShortestPath(
        final GraphPath<Station, SubwayWeightedEdge> graphPath) {
        this.graphPath = graphPath;
    }

    public List<Station> getPathStations() {
        try {
            return graphPath.getVertexList();
        } catch (NullPointerException e) {
            throw new InvalidSubwayPathException("지하철 경로를 조회할 수 없습니다.");
        }
    }

    public int getWeight() {
        try {
            double pathWeight = graphPath.getWeight();
            return (int)pathWeight;
        } catch (NullPointerException e) {
            throw new InvalidSubwayPathException("지하철 경로를 조회할 수 없습니다.");
        }
    }

    public int getSubWeight() {
        try {
            return (int)graphPath.getEdgeList().stream()
                                 .mapToDouble(SubwayWeightedEdge::getSubWeight)
                                 .sum();
        } catch (NullPointerException e) {
            throw new InvalidSubwayPathException("지하철 경로를 조회할 수 없습니다.");
        }
    }

}
