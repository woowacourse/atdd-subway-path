package wooteco.subway.infrastructure;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;
import wooteco.subway.domain.Path;
import wooteco.subway.domain.PathFinder;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Station;
import wooteco.subway.domain.fare.AgeDecorator;
import wooteco.subway.domain.fare.BaseFare;
import wooteco.subway.domain.fare.DistanceDecorator;
import wooteco.subway.domain.fare.Fare;
import wooteco.subway.exception.NoSuchPathException;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

public class SubwayGraph implements PathFinder {

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
    public Path getPath(final Station source, final Station target, final int extraFare, final int age) {
        validateExistsPath(source, target);
        List<Station> stations = graph.getPath(source, target).getVertexList();
        int distance = (int) graph.getPathWeight(source, target);
        Fare fare = new AgeDecorator(new DistanceDecorator(new BaseFare(extraFare), distance), age);
        double price = fare.calculateExtraFare();
        return new Path(stations, distance, price);
    }

    private void validateExistsPath(final Station source, final Station target) {
        if (Objects.isNull(this.graph.getPath(source, target))) {
            throw new NoSuchPathException(source.getId(), target.getId());
        }
    }

    @Override
    public Long getExpensiveLineId(Station source, Station target) {
        return graph.getPath(source, target).getEdgeList().stream()
                .mapToLong(DefaultWeightedEdgeCustom::getLineId)
                .max()
                .orElseThrow(NoSuchElementException::new);
    }
}
