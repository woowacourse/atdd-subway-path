package wooteco.subway.domain.path;

import java.util.HashSet;
import java.util.Set;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Sections;
import wooteco.subway.domain.Station;

public class DijkstraShortestGraphAlgorithm {

    public static DijkstraShortestPath<Station, ShortestPathEdge> generate(final Sections sections) {
        final WeightedMultigraph<Station, ShortestPathEdge> graph = new WeightedMultigraph<>(
                ShortestPathEdge.class);
        for (Station station : findAllStationByDistinct(sections)) {
            graph.addVertex(station);
        }
        for (Section section : sections.getValues()) {
            graph.addEdge(section.getUpStation(), section.getDownStation(),
                    new ShortestPathEdge(section.getLineId(), section.getDistance()));
        }
        return new DijkstraShortestPath<>(graph);
    }

    private static Set<Station> findAllStationByDistinct(final Sections sections) {
        Set<Station> stations = new HashSet<>();
        stations.addAll(sections.findUpStations());
        stations.addAll(sections.findDownStations());
        return stations;
    }
}
