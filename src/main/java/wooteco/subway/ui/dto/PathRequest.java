package wooteco.subway.ui.dto;

public class PathRequest {

    private final long source;
    private final long target;
    private final long age;

    public PathRequest(long source, long target, long age) {
        this.source = source;
        this.target = target;
        this.age = age;
    }

    public long getSource() {
        return source;
    }

    public long getTarget() {
        return target;
    }

    public long getAge() {
        return age;
    }
}
