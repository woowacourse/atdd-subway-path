package wooteco.subway.admin.domain;

import java.util.Arrays;

public enum PathSearchType {
    DISTANCE("DISTANCE"),
    DURATION("DURATION");

    private String value;

    PathSearchType(String value) {
        this.value = value;
    }

    public static PathSearchType of(String type) {
        return Arrays.stream(PathSearchType.values())
                .filter(pathSearchType -> pathSearchType.is(type))
                .findFirst()
                .orElseThrow(IllegalPathSearchTypeException::new);
    }

    public boolean is(String value) {
        return this.value.equals(value);
    }
}
