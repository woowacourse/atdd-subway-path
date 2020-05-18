package wooteco.subway.admin.domain;

import java.util.function.Function;

public enum PathType {
    DISTANCE(LineStation::getDistance),
    DURATION(LineStation::getDuration);

    private Function<LineStation, Integer> pathType;

    PathType(Function<LineStation, Integer> pathType) {
        this.pathType = pathType;
    }

    public int getValue(LineStation lineStation) {
        return pathType.apply(lineStation);
    }
}
