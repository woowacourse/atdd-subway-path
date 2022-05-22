package wooteco.subway.domain.path;

import java.util.Collections;
import java.util.List;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import wooteco.subway.domain.section.Distance;
import wooteco.subway.domain.section.Section;
import wooteco.subway.domain.station.Station;

public class Path {

    private final List<Station> stations;
    private final Distance distance;
    private final Fare fare;

    public Path(List<Section> sections, Station departure, Station arrival) {
        validateDepartureAndArrivalAreDifferent(departure, arrival);
        final GraphPath<Station, DefaultWeightedEdge> graphPath = generateGraphPath(sections, departure, arrival);

        this.stations = graphPath.getVertexList();
        this.distance = new Distance(graphPath.getWeight());
        this.fare = Fare.from(distance);
    }

    private void validateDepartureAndArrivalAreDifferent(Station departure, Station arrival) {
        if (departure.equals(arrival)) {
            throw new IllegalArgumentException("경로의 출발역과 도착역이 같을 수 없습니다.");
        }
    }

    private GraphPath<Station, DefaultWeightedEdge> generateGraphPath(List<Section> sections, Station departure,
                                                                      Station arrival) {
        final WeightedMultigraph<Station, DefaultWeightedEdge> graph = generateGraphFromSections(sections);
        final DijkstraShortestPath<Station, DefaultWeightedEdge> shortestPath = new DijkstraShortestPath<>(graph);
        return shortestPath.getPath(departure, arrival);
    }

    private WeightedMultigraph<Station, DefaultWeightedEdge> generateGraphFromSections(List<Section> sections) {
        final WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(
                DefaultWeightedEdge.class);

        sections.forEach(section -> addVertexAndEdgeIntoGraph(graph, section));
        return graph;
    }

    private void addVertexAndEdgeIntoGraph(WeightedMultigraph<Station, DefaultWeightedEdge> graph, Section section) {
        final Station upStation = section.getUpStation();
        final Station downStation = section.getDownStation();

        graph.addVertex(upStation);
        graph.addVertex(downStation);
        graph.setEdgeWeight(graph.addEdge(upStation, downStation), section.getDistance().getValue());
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
