package wooteco.subway.dto.request;

public class PathRequest {

    private long source;
    private long target;
    private int age;

    private PathRequest() {
    }

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
