package wooteco.subway.admin.service;

import java.util.function.Function;

import wooteco.subway.admin.domain.LineStation;

public enum PathType {
    DISTANCE(LineStation::getDistance),
    DURATION(LineStation::getDuration);

    private Function<LineStation, Integer> pathType;

    PathType(Function<LineStation, Integer> pathType) {
        this.pathType = pathType;
    }

    public Integer getValue(LineStation lineStation) {
        return pathType.apply(lineStation);
    }
}
