package wooteco.subway.admin.domain;

public enum SearchType {
    DISTANCE,
    DURATION;

    public static SearchType of(final String typeName) {
        return valueOf(typeName.toUpperCase());
    }

    public boolean isDistance() {
        return this == DISTANCE;
    }
}
