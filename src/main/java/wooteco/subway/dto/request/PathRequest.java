package wooteco.subway.dto.request;

public class PathRequest {
    private final long source;
    private final long target;
    private final int age;

    public PathRequest(long source, long target, int age) {
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
