package wooteco.subway.domain;

import java.util.Objects;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import wooteco.subway.exception.SectionNotFoundException;
import wooteco.subway.exception.SubwayException;

public class Path {

    private final DijkstraShortestPath<Station, DefaultWeightedEdge> graph;
    private final Station startStation;
    private final Station endStation;

    public Path(final DijkstraShortestPath<Station, DefaultWeightedEdge> graph,
                final Station startStation,
                final Station endStation) {
        validateDifferentStation(startStation, endStation);
        this.graph = Objects.requireNonNull(graph, "[ERROR] 경로 탐색용 경로가 존재하지 않습니다.");
        this.startStation = startStation;
        this.endStation = endStation;
    }

    private void validateDifferentStation(final Station startStation, final Station endStation) {
        if (startStation.equals(endStation)) {
            throw new SubwayException("[ERROR] 최소 경로 탐색이 불가합니다.");
        }
    }

    public int calculateMinDistance() {
        try {
            return (int) graph.getPath(startStation, endStation).getWeight();
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new SectionNotFoundException();
        }
    }
}
