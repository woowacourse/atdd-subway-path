package wooteco.subway.domain.path;

import wooteco.subway.domain.Section;
import wooteco.subway.domain.Sections;

import java.util.List;

public class Path {

    private final List<Long> shortestPath;
    private final int totalDistance;
    private final Sections sections;

    public Path(List<Long> shortestPath, int totalDistance, Sections sections) {
        this.shortestPath = shortestPath;
        this.totalDistance = totalDistance;
        this.sections = sections;
    }

    public List<Long> getShortestPath() {
        return shortestPath;
    }

    public int getTotalDistance() {
        return totalDistance;
    }

    public List<Section> getShortestSections() {
        return sections.getShortestSections(shortestPath);
    }
}
