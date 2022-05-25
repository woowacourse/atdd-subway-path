package wooteco.subway.domain;

public class Path {

    private final Long source;
    private final Long target;

    private Path(final Long source, final Long target) {
        this.source = source;
        this.target = target;
    }

    public static Path of(final Long source, final Long target) {
        return new Path(source, target);
    }

    public Long getSource() {
        return source;
    }

    public Long getTarget() {
        return target;
    }
}
