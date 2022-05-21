package wooteco.subway.domain.path;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import wooteco.subway.domain.section.Section;
import wooteco.subway.domain.station.Station;

public class NavigatorJgraphtAdapter implements Navigator<Section> {

    private static final String PATH_NOT_CONNECTED_EXCEPTION = "해당 역으로 이동하는 경로는 존재하지 않습니다.";

    private final GraphPath<Station, SectionEdge> path;

    private NavigatorJgraphtAdapter(GraphPath<Station, SectionEdge> path) {
        this.path = path;
    }

    public static NavigatorJgraphtAdapter of(Station source, Station target, List<Section> sections) {
        DijkstraShortestPath<Station, SectionEdge> shortestPath = new DijkstraShortestPath<>(toGraph(sections));
        GraphPath<Station, SectionEdge>  path = shortestPath.getPath(source, target);
        if (path == null) {
            throw new IllegalArgumentException(PATH_NOT_CONNECTED_EXCEPTION);
        }
        return new NavigatorJgraphtAdapter(path);
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
        Set<Station> vertices = new HashSet<>();
        for (Section section : sections) {
            vertices.addAll(section.toStations());
        }
        return new ArrayList<>(vertices);
    }

    private static List<SectionEdge> toEdges(List<Section> sections) {
        return sections.stream()
                .map(SectionEdge::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<Section> calculateShortestPath() {
        return path.getEdgeList()
                .stream()
                .map(SectionEdge::toSection)
                .collect(Collectors.toList());
    }

    private static class SectionEdge extends DefaultWeightedEdge {

        final Section section;

        SectionEdge(Section section) {
            this.section = section;
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

        Section toSection() {
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
