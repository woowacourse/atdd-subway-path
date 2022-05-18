package wooteco.subway.dto;

public class PathRequest {

    private final long source;
    private final long target;
    private final int age;

    public PathRequest(final long source, final long target, final int age) {
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

    public int getAge() {
        return age;
    }
}
