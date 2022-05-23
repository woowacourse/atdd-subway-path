package wooteco.subway.domain.path.implement;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;
import wooteco.subway.domain.Path;
import wooteco.subway.domain.PathFindSpecification;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Station;
import wooteco.subway.domain.path.PathFindStrategy;

public class MinimumDistanceFindStrategy implements PathFindStrategy {

    private static final String UNCONNECTED_STATION_EXCEPTION = "연결되지 않은 두 역입니다.";
    private static final MinimumDistanceFindStrategy STRATEGY = new MinimumDistanceFindStrategy();

    private MinimumDistanceFindStrategy() {}

    public static MinimumDistanceFindStrategy of() {
        return STRATEGY;
    }

    @Override
    public Path findPath(PathFindSpecification specification) {
        GraphPath<Station, Section> graphPath = getGraphPath(specification.getStations(), specification.getSections(),
                specification.getFrom(), specification.getTo());
        if (Objects.isNull(graphPath)) {
            throw new IllegalArgumentException(UNCONNECTED_STATION_EXCEPTION);
        }
        return new Path(graphPath.getVertexList(), graphPath.getEdgeList(), (int) graphPath.getWeight());
    }

    private GraphPath<Station, Section> getGraphPath(List<Station> stations,
                                                       List<Section> sections, Station from, Station to) {
        WeightedMultigraph<Station, Section> graph = new WeightedMultigraph<>(Section.class);
        stations.forEach(graph::addVertex);

        Map<Long, Station> sectionMap = stations.stream()
                .collect(Collectors.toMap(Station::getId, it -> it));

        for (Section section : sections) {
            Station downStation = sectionMap.get(section.getDownStationId());
            Station upStation = sectionMap.get(section.getUpStationId());
            graph.addEdge(downStation, upStation, section);
            graph.setEdgeWeight(section, section.getDistance());
        }
        return new DijkstraShortestPath<>(graph).getPath(from, to);
    }
}
