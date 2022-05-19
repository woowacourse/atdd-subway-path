package wooteco.subway.dto.path;

public class PathFindRequest {

    private long source;
    private long target;
    private int age;

    private PathFindRequest() {
    }

    public PathFindRequest(final long source, final long target, final int age) {
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
