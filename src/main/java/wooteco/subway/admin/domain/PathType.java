package wooteco.subway.admin.domain;

public enum PathType {
    DURATION,
    DISTANCE;

    public boolean isDuration() {
        return this == PathType.DURATION;
    }

    public boolean isDistance() {
        return this == PathType.DISTANCE;
    }
}
