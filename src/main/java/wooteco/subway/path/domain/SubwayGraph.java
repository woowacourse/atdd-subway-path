package wooteco.subway.path.domain;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import wooteco.subway.line.domain.Line;
import wooteco.subway.line.domain.Section;
import wooteco.subway.station.domain.Station;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class SubwayGraph extends WeightedMultigraph<Station, DefaultWeightedEdge> {
    public SubwayGraph(Class edgeClass) {
        super(edgeClass);
    }

    public void addVertices(List<Line> lines) {
        lines.stream()
                .flatMap(line -> line.getStations().stream())
                .distinct()
                .collect(toList())
                .forEach(this::addVertex);
    }

    public void addEdges(List<Line> lines) {
        for (Line line : lines) {
            line.getSections()
                    .getSections()
                    .forEach(this::addEdge);
        }
    }

    private void addEdge(Section section) {
        setEdgeWeight(addEdge(section.getUpStation(), section.getDownStation()), section.getDistance());
    }
}
