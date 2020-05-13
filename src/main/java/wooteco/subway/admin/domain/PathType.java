package wooteco.subway.admin.domain;

import java.util.Arrays;

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
            .orElseThrow(RuntimeException::new);
    }

    public boolean isDistance() {
        return this.equals(DISTANCE);
    }

    public boolean isDuration() {
        return this.equals(DURATION);
    }
}
