package wooteco.subway.path.infrastructure;

import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.stream.Stream;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import wooteco.subway.line.domain.Section;
import wooteco.subway.station.domain.Station;

public class SubwayMap {

    private final WeightedMultigraph<Station, DefaultWeightedEdge> graph;

    public SubwayMap() {
        this.graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
    }

    public WeightedMultigraph<Station, DefaultWeightedEdge> getSubwayMap(List<Section> sections) {
        setVertexes(sections);
        setEdges(sections);

        return graph;
    }

    private void setVertexes(List<Section> sections) {
        getStations(sections).forEach(graph::addVertex);
    }

    private List<Station> getStations(List<Section> sections) {
        return sections.stream()
            .flatMap(section -> Stream.of(section.getUpStation(), section.getDownStation()))
            .collect(toList());
    }

    private void setEdges(List<Section> sections) {
        sections.forEach(
            section -> graph.setEdgeWeight(
                graph.addEdge(
                    section.getUpStation(),
                    section.getDownStation()
                ),
                section.getDistance()
            )
        );
    }
}
