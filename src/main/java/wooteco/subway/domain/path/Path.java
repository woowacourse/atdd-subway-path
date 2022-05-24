package wooteco.subway.domain.path;

import java.util.Collections;
import java.util.List;
import wooteco.subway.domain.Sections;
import wooteco.subway.exception.NotFoundPathException;

public class Path {

    private final List<Long> shortestStationIds;
    private final List<Long> usedLineIds;
    private final int totalDistance;


    private Path(final List<Long> shortestStationIds, final List<Long> usedLineIds, final int totalDistance) {
        this.shortestStationIds = shortestStationIds;
        this.usedLineIds = usedLineIds;
        this.totalDistance = totalDistance;
    }

    public static Path of(final Sections sections, final long sourceId, final long targetId,
                          final PathFindStrategy pathFindStrategy) {
        validateMovement(sourceId, targetId);
        final FindPathResult pathResult = pathFindStrategy.findPath(sections, sourceId, targetId);
        return new Path(pathResult.getStationIds(), pathResult.getUsedLineIds(), pathResult.getTotalDistance());
    }

    private static void validateMovement(final long sourceId, final long targetId) {
        if (sourceId == targetId) {
            throw new NotFoundPathException("같은 위치로는 경로를 찾을 수 없습니다.");
        }
    }

    public List<Long> getStationIds() {
        return Collections.unmodifiableList(shortestStationIds);
    }

    public List<Long> getUsedLineIds() {
        return Collections.unmodifiableList(usedLineIds);
    }

    public int getTotalDistance() {
        return totalDistance;
    }
}
