package wooteco.subway.domain;

import wooteco.subway.controller.dto.response.StationResponseDto;

import java.util.ArrayList;
import java.util.List;

public class Path {
    private final List<Section> allSections;
    private final Graph graph;

    public Path(List<Line> allLines, Station sourceStation, Station targetStation) {
        allSections = new ArrayList<>();
        for (Line line : allLines) {
            Sections sections = line.getSections();
            allSections.addAll(sections.getSections());
        }
        graph = new Graph(allSections, sourceStation, targetStation);
    }

    public List<Station> findShortestPath() {
        return graph.getShortestPath();
    }

    public int findTotalDistance() {
        return graph.getTotalDistance();
    }
}
