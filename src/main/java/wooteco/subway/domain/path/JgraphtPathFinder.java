package wooteco.subway.domain.path;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import wooteco.subway.domain.line.Line;
import wooteco.subway.domain.line.LineSeries;
import wooteco.subway.domain.property.Distance;
import wooteco.subway.domain.property.fare.Fare;
import wooteco.subway.domain.section.Section;
import wooteco.subway.domain.station.Station;
import wooteco.subway.exception.PathNotFoundException;

public class JgraphtPathFinder implements PathFinder {

    private final GraphPath<Station, SectionEdge> path;

    public JgraphtPathFinder(GraphPath<Station, SectionEdge> path) {
        this.path = path;
    }

    public static PathFinder of(LineSeries lineSeries, Station source, Station destination) {
        return new JgraphtPathFinder(findShortestPath(createGraph(lineSeries), source, destination));
    }

    private static WeightedMultigraph<Station, SectionEdge> createGraph(LineSeries lineSeries) {
        WeightedMultigraph<Station, SectionEdge> graph = new WeightedMultigraph<>(SectionEdge.class);
        for (Line line : lineSeries.getLines()) {
            addStationEdge(graph, line.getSectionSeries().getSections(), line.getExtraFare());
        }
        return graph;
    }

    private static void addStationEdge(
        WeightedMultigraph<Station, SectionEdge> graph,
        List<Section> sections,
        Fare fare
    ) {
        for (Section section : sections) {
            graph.addVertex(section.getUpStation());
            graph.addVertex(section.getDownStation());
            final SectionEdge sectionEdge = new SectionEdge(section, fare);
            graph.addEdge(section.getUpStation(), section.getDownStation(), sectionEdge);
            graph.setEdgeWeight(sectionEdge, section.getDistance().getValue());
        }
    }

    private static GraphPath<Station, SectionEdge> findShortestPath(
        WeightedMultigraph<Station, SectionEdge> graph,
        Station source, Station destination
    ) {
        validateDistinctStation(source, destination);
        return findPath(graph, source, destination);
    }

    private static void validateDistinctStation(Station source, Station destination) {
        if (source.equals(destination)) {
            throw new PathNotFoundException(
                String.format("출발지와 도착지(%s)는 동일할 수 없습니다.", source.getName())
            );
        }
    }

    private static GraphPath<Station, SectionEdge> findPath(
        WeightedMultigraph<Station, SectionEdge> graph,
        Station source,
        Station destination
    ) {
        try {
            final GraphPath<Station, SectionEdge> foundPath
                = new DijkstraShortestPath<>(graph).getPath(source, destination);
            return Objects.requireNonNull(foundPath);
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new PathNotFoundException(String.format(
                "(%s) 로부터 (%s) 까지의 경로가 존재하지 않습니다.",
                source.getName(),
                destination.getName()
            ));
        }
    }

    @Override
    public List<Section> getSections() {
        return mapEdgesTo(path.getEdgeList(), SectionEdge::getSection);
    }

    private static <T> List<T> mapEdgesTo(List<SectionEdge> edges, Function<SectionEdge, T> mapper) {
        return edges.stream()
            .map(mapper)
            .collect(Collectors.toList());
    }

    @Override
    public List<Fare> getPassingFares() {
        return mapEdgesTo(path.getEdgeList(), SectionEdge::getFare);
    }

    @Override
    public Distance getDistance() {
        return new Distance(Math.round(path.getWeight()));
    }

    private static class SectionEdge extends DefaultWeightedEdge {

        private final Section section;
        private final Fare fare;

        public SectionEdge(Section section, Fare fare) {
            this.section = section;
            this.fare = fare;
        }

        public Section getSection() {
            return section;
        }

        public Fare getFare() {
            return fare;
        }
    }
}
