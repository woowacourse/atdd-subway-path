package wooteco.subway.domain.path;

import wooteco.subway.domain.Section;
import wooteco.subway.domain.Sections;

import java.util.List;

public class Path {

    private final List<Long> shortestPathByStationId;
    private final int totalDistance;
    private final Sections shortestSections;

    public Path(List<Long> shortestPathByStationId, int totalDistance, Sections shortestSections) {
        this.shortestPathByStationId = shortestPathByStationId;
        this.totalDistance = totalDistance;
        this.shortestSections = shortestSections;
    }

    public List<Long> getShortestPathByStationId() {
        return shortestPathByStationId;
    }

    public int getTotalDistance() {
        return totalDistance;
    }

    public List<Section> getShortestSections() {
        return shortestSections.getSections();
    }
}
