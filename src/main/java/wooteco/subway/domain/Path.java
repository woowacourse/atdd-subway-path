package wooteco.subway.domain;

import java.util.List;
import java.util.Map;

public class Path {
    private final Lines lines;
    private final PathFindingStrategy pathFindingStrategy;
    private final Station source;
    private final Station target;

    public Path(Lines lines, PathFindingStrategy pathFindingStrategy, Station source,
        Station target) {
        this.lines = lines;
        this.pathFindingStrategy = pathFindingStrategy;
        this.source = source;
        this.target = target;
    }

    public int getShortestDistance() {
        return (int)pathFindingStrategy.getShortestDistance(source, target, lines);
    }

    public List<Station> getShortestPath() {
        return pathFindingStrategy.getShortestPath(source, target, lines);
    }

    public int getExtraFare() {
        List<Long> lineIdsContainedInPath = pathFindingStrategy.getLineIds(source, target, lines);
        Map<Long, Integer> extraFareByIds = lines.getExtraFareByIds();
        int extraFare = 0;

        extraFare = findMaxExtraFare(lineIdsContainedInPath, extraFareByIds, extraFare);
        return extraFare;
    }

    private int findMaxExtraFare(List<Long> lineIdsContainedInPath, Map<Long, Integer> extraFareByIds, int extraFare) {
        for (Long lineId : lineIdsContainedInPath) {
            extraFare = checkBiggerFare(extraFareByIds, extraFare, lineId);
        }
        return extraFare;
    }

    private int checkBiggerFare(Map<Long, Integer> extraFareByIds, int extraFare, Long lineId) {
        if (extraFare < extraFareByIds.getOrDefault(lineId, 0)) {
            extraFare = extraFareByIds.getOrDefault(lineId, 0);
        }
        return extraFare;
    }
}
