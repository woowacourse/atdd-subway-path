package wooteco.subway.domain.line;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jgrapht.graph.WeightedMultigraph;

import wooteco.subway.domain.Section;
import wooteco.subway.domain.Sections;
import wooteco.subway.domain.Station;
import wooteco.subway.domain.path.ShortestPathEdge;

public class Lines {
    private final List<Line> lines;

    public Lines(List<Line> lines) {
        this.lines = lines;
    }

    public Set<Station> extractStations() {
        Set<Station> stations = new HashSet<>();
        for (Line line : lines) {
            Sections sections = line.getSections();
            addStations(stations, sections);
        }
        return stations;
    }

    private void addStations(Set<Station> stations, Sections sections) {
        for (Section section : sections.getSections()) {
            stations.add(section.getUpStation());
            stations.add(section.getDownStation());
        }
    }

    public void addEdge(WeightedMultigraph<Station, ShortestPathEdge> graph) {
        for (Line line : lines) {
            Sections sections = line.getSections();
            addEdges(graph, line, sections);
        }
    }

    private void addEdges(WeightedMultigraph<Station, ShortestPathEdge> graph, Line line, Sections sections) {
        for (Section section : sections.getSections()) {
            graph.addEdge(section.getUpStation(), section.getDownStation(), new ShortestPathEdge(line.getId(),
                    section.getDistance()));
        }
    }
}
