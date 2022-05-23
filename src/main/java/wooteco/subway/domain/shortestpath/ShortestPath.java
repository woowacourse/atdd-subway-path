package wooteco.subway.domain.shortestpath;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Sections;
import wooteco.subway.exception.NotFoundPathException;

public class ShortestPath {

    private final ShortestPathStrategy shortestPathStrategy;
    private final List<Section> path;

    public ShortestPath(ShortestPathStrategy shortestPathStrategy, Sections sections, Long sourceId, Long targetId) {
        this.shortestPathStrategy = shortestPathStrategy;
        this.path = calculateShortestPath(sections, sourceId, targetId);
    }

    private List<Section> calculateShortestPath(Sections sections, Long sourceId, Long targetId){
        validateMovement(sourceId, targetId);
        List<Long> shortestPath = shortestPathStrategy.findShortestPath(sections, sourceId, targetId);
        return toSections(sections, shortestPath);
    }

    private void validateMovement(long sourceId, long targetId) {
        if (sourceId == targetId) {
            throw new NotFoundPathException("같은 위치로는 경로를 찾을 수 없습니다.");
        }
    }

    private List<Section> toSections(Sections sections, List<Long> shortestPath) {
        List<Section> path = new LinkedList<>();

        for (int i = 0; i < shortestPath.size() - 1; i++) {
            path.add(sections.findSection(shortestPath.get(i), shortestPath.get(i + 1)));
        }
        return path;
    }

    public int getTotalDistance() {
        return path.stream()
                .mapToInt(Section::getDistance)
                .sum();
    }

    public List<Long> getStationIds(long sourceId, long targetId) {
        List<Long> stationIds = new LinkedList<>();
        for (Section section : path) {
            stationIds.add(sourceId);
            sourceId = section.getOppositeStation(sourceId);
        }
        stationIds.add(targetId);
        return stationIds;
    }

    public List<Long> getLineIds(){
        return path.stream()
                .map(Section::getLineId)
                .distinct()
                .collect(Collectors.toList());
    }
}
