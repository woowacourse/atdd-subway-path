package wooteco.subway.domain.path;

import java.util.Collections;
import java.util.List;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;
import wooteco.subway.domain.fare.Age;
import wooteco.subway.domain.fare.Fare;
import wooteco.subway.domain.line.Line;
import wooteco.subway.domain.section.Distance;
import wooteco.subway.domain.section.Section;
import wooteco.subway.domain.station.Station;

public class Path {

    private final List<Station> stations;
    private final Distance distance;
    private final Fare fare;

    public Path(List<Section> sections, Station departure, Station arrival, Age age) {
        validateDepartureAndArrivalAreDifferent(departure, arrival);
        final GraphPath<Station, SectionEdge> graphPath = generateGraphPath(sections, departure, arrival);

        this.stations = graphPath.getVertexList();
        this.distance = new Distance(graphPath.getWeight());
        this.fare = Fare.of(distance, age, generateExtraFare(graphPath));
    }

    private void validateDepartureAndArrivalAreDifferent(Station departure, Station arrival) {
        if (departure.equals(arrival)) {
            throw new IllegalArgumentException("경로의 출발역과 도착역이 같을 수 없습니다.");
        }
    }

    private GraphPath<Station, SectionEdge> generateGraphPath(List<Section> sections, Station departure,
                                                              Station arrival) {
        final WeightedMultigraph<Station, SectionEdge> graph = generateGraphFromSections(sections);
        final DijkstraShortestPath<Station, SectionEdge> shortestPath = new DijkstraShortestPath<>(graph);
        return shortestPath.getPath(departure, arrival);
    }

    private Fare generateExtraFare(GraphPath<Station, SectionEdge> graphPath) {
        List<SectionEdge> edgeList = graphPath.getEdgeList();
        int maxFare = edgeList.stream()
                .map(SectionEdge::getSection)
                .map(Section::getLine)
                .map(Line::getExtraFare)
                .mapToInt(Fare::getValue)
                .max()
                .orElse(0);

        return new Fare(maxFare);
    }

    private WeightedMultigraph<Station, SectionEdge> generateGraphFromSections(List<Section> sections) {
        final WeightedMultigraph<Station, SectionEdge> graph = new WeightedMultigraph<>(
                SectionEdge.class);

        sections.forEach(section -> addVertexAndEdgeIntoGraph(graph, section));
        return graph;
    }

    private void addVertexAndEdgeIntoGraph(WeightedMultigraph<Station, SectionEdge> graph, Section section) {
        final Station upStation = section.getUpStation();
        final Station downStation = section.getDownStation();

        graph.addVertex(upStation);
        graph.addVertex(downStation);

        SectionEdge edge = new SectionEdge(section);
        graph.addEdge(upStation, downStation, edge);
        graph.setEdgeWeight(edge, section.getDistance().getValue());
    }

    public List<Station> getStations() {
        return Collections.unmodifiableList(stations);
    }

    public Fare getFare() {
        return this.fare;
    }

    public Distance getDistance() {
        return distance;
    }
}
