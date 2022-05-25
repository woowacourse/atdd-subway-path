package wooteco.subway.dto.path;

import javax.validation.constraints.Positive;

public class PathRequest {
    private long source;

    private long target;

    @Positive
    private int age;

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
