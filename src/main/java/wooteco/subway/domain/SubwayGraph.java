package wooteco.subway.domain;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;
import wooteco.subway.exception.NotLinkPathException;

public class SubwayGraph {

    private final WeightedMultigraph<Station, ShortestPathEdge> subwayGraph;

    public SubwayGraph() {
        subwayGraph = new WeightedMultigraph<>(ShortestPathEdge.class);
    }

    public void init(final Sections sections) {
        addStationsToVertex(sections);
        addSectionsToEdge(sections);
    }

    private void addStationsToVertex(final Sections sections) {
        for (Station station : sections.getStations()) {
            subwayGraph.addVertex(station);
        }
    }

    private void addSectionsToEdge(final Sections sections) {
        for (Section section : sections.getSections()) {
            Station upStation = section.getUpStation();
            Station downStation = section.getDownStation();
            subwayGraph.addEdge(upStation, downStation,
                    new ShortestPathEdge(section.getLineId(), section.getDistance()));
        }
    }

    public GraphPath<Station, ShortestPathEdge> graphResult(final Station source, final Station target) {
        DijkstraShortestPath<Station, ShortestPathEdge> pathFinder = new DijkstraShortestPath<>(subwayGraph);
        GraphPath<Station, ShortestPathEdge> path = pathFinder.getPath(source, target);
        validateSourceToTargetLink(path);
        return path;
    }

    private void validateSourceToTargetLink(final GraphPath<Station, ShortestPathEdge> path) {
        if (path == null) {
            throw new NotLinkPathException("출발역과 도착역이 연결되어 있지 않습니다.");
        }
    }

}
