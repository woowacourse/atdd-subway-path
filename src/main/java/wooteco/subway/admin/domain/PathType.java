package wooteco.subway.admin.domain;

import java.util.Arrays;

import wooteco.subway.admin.exception.NoExistPathTypeException;

public enum PathType {
    DISTANCE("DISTANCE"),
    DURATION("DURATION");

    String name;

    PathType(String name) {
        this.name = name;
    }

    public static PathType findPathType(String name) {
        return Arrays.stream(values())
            .filter(pathType -> pathType.name.equals(name))
            .findFirst()
            .orElseThrow(() -> new NoExistPathTypeException("존재하지 않는 경로 타입입니다."));
    }

    public boolean isDistance() {
        return this.equals(DISTANCE);
    }

    public boolean isDuration() {
        return this.equals(DURATION);
    }
}
