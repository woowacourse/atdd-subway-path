package wooteco.subway.path.domain;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import wooteco.subway.exception.NotExistingPathException;
import wooteco.subway.line.domain.Section;
import wooteco.subway.station.domain.Station;

import java.util.List;

public final class DijkstraSubwayPathStrategy extends GraphSubwayPathStrategy {

    public DijkstraSubwayPathStrategy(List<Station> stations, List<Section> sections) {
        super(stations, sections, new WeightedMultigraph<>(DefaultWeightedEdge.class));
    }

    @Override
    public List<Station> shortestPath(Station source, Station target) {
        try {
            return shortestGraphPath(source, target).getVertexList();
        } catch (NullPointerException exception) {
            throw new NotExistingPathException();
        }
    }

    @Override
    public int shortestDistance(Station source, Station target) {
        return (int) shortestGraphPath(source, target).getWeight();
    }

    private GraphPath<Station, DefaultWeightedEdge> shortestGraphPath(Station source, Station target) {
        DijkstraShortestPath<Station, DefaultWeightedEdge> path = new DijkstraShortestPath<>(graph);
        return path.getPath(source, target);
    }
}
