package wooteco.subway.domain;

import java.util.List;

public class Path {

    private final Long source;
    private final Long target;
    private final List<Long> stationIds;
    private final Sections sections;

    public Path(Long source, Long target, List<Long> stationIds, Sections sections) {
        validateExistStationId(source, target, stationIds);
        this.source = source;
        this.target = target;
        this.stationIds = stationIds;
        this.sections = sections;
    }

    private static void validateExistStationId(Long source, Long target, List<Long> stationIds) {
        if (isNotContains(source, target, stationIds)) {
            throw new IllegalArgumentException("[ERROR] 역을 찾을 수 없습니다");
        }
    }

    private static boolean isNotContains(Long source, Long target, List<Long> stationIds) {
        return !(stationIds.contains(source) && stationIds.contains(target));
    }

    public Long getSource() {
        return source;
    }

    public Long getTarget() {
        return target;
    }

    public List<Long> getStationIds() {
        return stationIds;
    }

    public Sections getSections() {
        return sections;
    }

    public List<Section> getShortestPathSections(List<Long> shortestPath) {
        return sections.getSectionsFromShortestPath(shortestPath);
    }
}
