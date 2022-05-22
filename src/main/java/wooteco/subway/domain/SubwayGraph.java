package wooteco.subway.domain;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;
import wooteco.subway.exception.NoSuchPathException;
import wooteco.subway.utils.DefaultWeightedEdgeCustom;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

public class SubwayGraph implements ShortestPath {

    private final DijkstraShortestPath<Station, DefaultWeightedEdgeCustom> graph;

    public SubwayGraph(final List<Section> sections) {
        this.graph = initGraph(sections);
    }

    private DijkstraShortestPath<Station, DefaultWeightedEdgeCustom> initGraph(List<Section> sections) {
        WeightedMultigraph<Station, DefaultWeightedEdgeCustom> graph = new WeightedMultigraph<>(DefaultWeightedEdgeCustom.class);
        for (Section section : sections) {
            Station upStation = section.getUpStation();
            Station downStation = section.getDownStation();
            graph.addVertex(upStation);
            graph.addVertex(downStation);
            graph.addEdge(upStation, downStation, new DefaultWeightedEdgeCustom(section.getLineId(), section.getDistance()));
        }
        return new DijkstraShortestPath<>(graph);
    }

    @Override
    public Path getPath(final Station source, final Station target, final Fare fare, final int age) {
        validateExistsPath(source, target);
        List<Station> stations = graph.getPath(source, target).getVertexList();
        int distance = (int) graph.getPathWeight(source, target);
        double price = fare.calculateFare(distance, age);
        return new Path(stations, distance, price);
    }

    private void validateExistsPath(final Station source, final Station target) {
        if (Objects.isNull(this.graph.getPath(source, target))) {
            throw new NoSuchPathException(source.getId(), target.getId());
        }
    }

    public Long getExpensiveLineId(Station source, Station target) {
        return graph.getPath(source, target).getEdgeList().stream()
                .mapToLong(DefaultWeightedEdgeCustom::getLineId)
                .max()
                .orElseThrow(NoSuchElementException::new);
    }
}
