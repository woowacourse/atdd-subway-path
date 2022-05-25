package wooteco.subway.dto;

import javax.validation.constraints.NotNull;

public class PathRequest {

    @NotNull
    private long source;
    @NotNull
    private long target;
    @NotNull
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
