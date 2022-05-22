package wooteco.subway.ui.dto;

import javax.validation.constraints.Positive;

public class PathRequest {

    @Positive
    private final long source;
    @Positive
    private final long target;
    @Positive
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
