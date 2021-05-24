package wooteco.subway.path.domain;

import java.util.List;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import wooteco.subway.line.domain.Section;
import wooteco.subway.station.domain.Station;

public class SubwayMap {

    private final WeightedMultigraph<Station, DefaultWeightedEdge> graph;
    private final ShortestPathFinder shortestPathFinder;

    public SubwayMap(List<Station> stations, List<Section> sections) {
        this.graph = new WeightedMultigraph(DefaultWeightedEdge.class);
        registerLines(stations);
        registerSections(sections);
        this.shortestPathFinder = new JgraphtPathFinder(graph);
    }

    private void registerLines(List<Station> stations) {
        for (Station station : stations) {
            graph.addVertex(station);
        }
    }

    private void registerSections(List<Section> sections) {
        for (Section section : sections) {
            graph.setEdgeWeight(
                graph.addEdge(section.getUpStation(), section.getDownStation()),
                section.getDistance()
            );
        }
    }

    public Path getShortestPath(Station source, Station target) {
        List<Station> shortestPath = shortestPathFinder.findShortestPath(source, target);
        int shortestDistance = shortestPathFinder.findShortestDistance(source, target);
        return new Path(shortestPath, shortestDistance);
    }
}
