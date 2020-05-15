package wooteco.subway.domain.path;

import java.util.Arrays;

public enum WeightType {
    DURATION("DURATION", StationWeightEdge::getDuration),
    DISTANCE("DISTANCE", StationWeightEdge::getDistance);

    private final String name;
    private final WeightStrategy strategy;

    WeightType(String name, WeightStrategy strategy) {
        this.name = name;
        this.strategy = strategy;
    }

    public static WeightStrategy findStrategy(String type) {
        return Arrays.stream(values())
            .filter(weightType -> weightType.name.equals(type))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("해당 경로 탐색 기준이 존재하지 않습니다."))
            .strategy;
    }
}
