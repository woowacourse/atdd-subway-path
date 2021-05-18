package wooteco.subway.domain;

import java.util.ArrayList;
import java.util.List;

public class Path {
    private final Graph graph;

    public Path(List<Line> allLines) {
        List<Section> allSections = new ArrayList<>();
        for (Line line : allLines) {
            Sections sections = line.getSections();
            allSections.addAll(sections.getSections());
        }
        graph = new Graph(allSections);
    }

    public List<Station> findShortestPath(Station sourceStation, Station targetStation) {
        return graph.getShortestPath(sourceStation, targetStation);
    }

    public int findTotalDistance(Station sourceStation, Station targetStation) {
        return graph.getTotalDistance(sourceStation, targetStation);
    }
}
