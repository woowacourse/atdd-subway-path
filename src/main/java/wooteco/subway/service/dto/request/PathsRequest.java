package wooteco.subway.service.dto.request;

public class PathsRequest {

    private final long source;
    private final long target;
    private final int age;

    public PathsRequest(long source, long target, int age) {
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
