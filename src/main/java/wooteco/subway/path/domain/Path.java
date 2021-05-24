package wooteco.subway.path.domain;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import wooteco.subway.line.domain.Line;
import wooteco.subway.line.domain.Section;
import wooteco.subway.path.dto.PathResponse;
import wooteco.subway.station.domain.Station;
import wooteco.subway.station.dto.StationResponse;

import java.util.List;
import java.util.stream.Stream;

public class Path {
    private final List<Line> lines;

    public Path(List<Line> lines) {
        this.lines = lines;
    }

    public PathResponse findShortestPath(Long sourceId, Long targetId) {
        GraphPath<Station, DefaultWeightedEdge> path = makeShortestPath().getPath(findStationById(sourceId), findStationById(targetId));
       return new PathResponse(StationResponse.listOf(path.getVertexList()), (int) path.getWeight());
    }

    private Station findStationById(Long stationId) {
        return lines.stream()
                .map(Line::sections)
                .flatMap(it -> it.stream().flatMap(section -> Stream.of(
                        section.getUpStation(),
                        section.getDownStation()
                        )))
                .distinct()
                .filter(station -> station.sameId(stationId))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("찾을 수 없는 역입니다."));
    }


    private DijkstraShortestPath<Station, DefaultWeightedEdge> makeShortestPath() {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph =
                new WeightedMultigraph<>(DefaultWeightedEdge.class);
        makeGraph(graph);
        return new DijkstraShortestPath<>(graph);
    }

    private void makeGraph(final WeightedMultigraph<Station, DefaultWeightedEdge> graph) {
        for (Line line : lines) {
            addStationVertex(graph, line.getStations());
            setStationEdgeWeight(graph, line.sections());
        }
    }

    private void addStationVertex(WeightedMultigraph<Station, DefaultWeightedEdge> graph, List<Station> stations) {
        for (Station station : stations) {
            graph.addVertex(station);
        }
    }

    private void setStationEdgeWeight(final WeightedMultigraph<Station, DefaultWeightedEdge> graph, List<Section> sections) {
        for (Section section : sections) {
            graph.setEdgeWeight(graph.addEdge(section.getUpStation(), section.getDownStation()), section.getDistance());
        }
    }
}
