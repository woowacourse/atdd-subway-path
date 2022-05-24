package wooteco.subway.domain.subwaygraph;

import java.util.List;
import java.util.stream.Collectors;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;
import wooteco.subway.domain.Line;
import wooteco.subway.domain.Paths;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Sections;
import wooteco.subway.domain.Station;
import wooteco.subway.exception.CalculatePathsException;

public class SubwayGraph {

    private final WeightedMultigraph<Station, SubwayPathEdge> subwayGraph;

    public SubwayGraph(final Sections sections) {
        subwayGraph = new WeightedMultigraph<>(SubwayPathEdge.class);
        addStationsToSubwayVertex(sections);
        addSectionsToSubwayEdge(sections);
    }

    private void addStationsToSubwayVertex(final Sections sections) {
        for (Station station : sections.getStations()) {
            subwayGraph.addVertex(station);
        }
    }

    private void addSectionsToSubwayEdge(final Sections sections) {
        for (Section section : sections.getSections()) {
            subwayGraph.addEdge(section.getUpStation(), section.getDownStation()
                    , new SubwayPathEdge(section.getLineId(), section.getDistance()));
        }
    }

    public Paths createPathsResult(final Station source, final Station target) {
        GraphPath<Station, SubwayPathEdge> subwayGraphResult = createSubwayGraphResult(source, target);
        List<Station> stations = subwayGraphResult.getVertexList();
        List<Long> lineIds = getLineIds(subwayGraphResult);
        double distance = subwayGraphResult.getWeight();
        return new Paths(stations, lineIds, distance);
    }

    private GraphPath<Station, SubwayPathEdge> createSubwayGraphResult(Station source, Station target) {
        try {
            DijkstraShortestPath<Station, SubwayPathEdge> pathFinder = new DijkstraShortestPath<>(subwayGraph);
            return pathFinder.getPath(source, target);
        } catch (IllegalArgumentException e) {
            throw new CalculatePathsException("출발역과 도착역 중, 노선에 등록되지 않은 역이 있습니다.");
        }
    }

    private List<Long> getLineIds(GraphPath<Station, SubwayPathEdge> subwayGraphResult) {
        return subwayGraphResult.getEdgeList().stream()
                .map(SubwayPathEdge::getLineId)
                .collect(Collectors.toList());
    }
}
