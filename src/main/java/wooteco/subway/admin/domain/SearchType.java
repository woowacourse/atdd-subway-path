package wooteco.subway.admin.domain;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Objects;

public enum SearchType {
    DISTANCE,
    DURATION;

    public static SearchType of(final String typeName) {
        return Arrays.stream(values())
                .filter(it -> Objects.equals(it.name(), typeName.toUpperCase()))
                .findFirst()
                .orElseThrow(NoSuchElementException::new);
    }

    public boolean isDistance() {
        return this == DISTANCE;
    }
}
