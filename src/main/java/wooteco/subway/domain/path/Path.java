package wooteco.subway.domain.path;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import wooteco.subway.domain.path.fare.Fare;
import wooteco.subway.domain.section.Section;
import wooteco.subway.domain.station.Station;

public class Path {

    private static final String NOT_REGISTERED_STATION_EXCEPTION = "노선에 등록되지 않은 역을 입력하였습니다.";
    private static final String SELF_LOOP_EXCEPTION = "출발점과 도착점은 동일할 수 없습니다.";
    private static final String PATH_NOT_CONNECTED_EXCEPTION = "해당 역으로 이동하는 경로는 존재하지 않습니다.";

    private final GraphPath<Station, SectionEdge> value;

    private Path(GraphPath<Station, SectionEdge> value) {
        this.value = value;
    }

    public static Path of(Station source, Station target, List<Section> sections) {
        validateNonSelfLoop(source, target);
        validateRegisteredStations(source, target, sections);
        DijkstraShortestPath<Station, SectionEdge> shortestPath = new DijkstraShortestPath<>(toGraph(sections));
        return new Path(findPath(source, target, shortestPath));
    }

    private static void validateNonSelfLoop(Station source, Station target) {
        if (source.equals(target)) {
            throw new IllegalArgumentException(SELF_LOOP_EXCEPTION);
        }
    }

    private static void validateRegisteredStations(Station source, Station target, List<Section> sections) {
        boolean sourceNotRegistered = sections.stream()
                .noneMatch(station -> station.hasStationOf(source));
        boolean targetNotRegistered = sections.stream()
                .noneMatch(station -> station.hasStationOf(target));
        if (sourceNotRegistered || targetNotRegistered) {
            throw new IllegalArgumentException(NOT_REGISTERED_STATION_EXCEPTION);
        }
    }

    private static WeightedMultigraph<Station, SectionEdge> toGraph(List<Section> sections) {
        WeightedMultigraph<Station, SectionEdge> graph = new WeightedMultigraph<>(SectionEdge.class);
        for (Station vertex : toVertices(sections)) {
            graph.addVertex(vertex);
        }
        for (SectionEdge edge : toEdges(sections)) {
            graph.addEdge(edge.getSourceVertex(), edge.getTargetVertex(), edge);
        }
        return graph;
    }

    private static List<Station> toVertices(List<Section> sections) {
        List<Station> vertices = new ArrayList<>();
        for (Section section : sections) {
            vertices.addAll(section.toStations());
        }
        return vertices;
    }

    private static List<SectionEdge> toEdges(List<Section> sections) {
        return sections.stream()
                .map(SectionEdge::of)
                .collect(Collectors.toList());
    }

    private static GraphPath<Station, SectionEdge> findPath(Station source,
                                                            Station target,
                                                            DijkstraShortestPath<Station, SectionEdge> shortestPath) {
        GraphPath<Station, SectionEdge>  path = shortestPath.getPath(source, target);
        if (path == null) {
            throw new IllegalArgumentException(PATH_NOT_CONNECTED_EXCEPTION);
        }
        return path;
    }

    public List<Station> toStations() {
        return value.getVertexList();
    }

    public int calculateDistance() {
        return (int) value.getWeight();
    }

    public int calculateFare() {
        return Fare.of((int) value.getWeight()).getValue();
    }

    public List<Long> getRegisteredLineIds() {
        return value.getEdgeList().stream().map(SectionEdge::toDomain)
                .map(Section::getLineId)
                .distinct()
                .collect(Collectors.toList());
    }

    private static class SectionEdge extends DefaultWeightedEdge {

        final Section section;

        private SectionEdge(Section section) {
            this.section = section;
        }

        private static SectionEdge of(Section section) {
            return new SectionEdge(section);
        }

        Station getSourceVertex() {
            return section.getUpStation();
        }

        Station getTargetVertex() {
            return section.getDownStation();
        }

        @Override
        protected double getWeight() {
            return section.getDistance();
        }

        public Section toDomain() {
            return section;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            SectionEdge that = (SectionEdge) o;
            return Objects.equals(section, that.section);
        }

        @Override
        public int hashCode() {
            return Objects.hash(section);
        }
    }
}
