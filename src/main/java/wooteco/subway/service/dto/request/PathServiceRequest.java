package wooteco.subway.service.dto.request;

public class PathServiceRequest {

    private final long source;
    private final long target;
    private final long age;

    public PathServiceRequest(long source, long target, long age) {
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
