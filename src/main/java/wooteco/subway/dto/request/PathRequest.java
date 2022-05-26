package wooteco.subway.dto.request;

import javax.validation.constraints.Positive;

public class PathRequest {

    @Positive
    private final long source;

    @Positive
    private final long target;

    @Positive
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

    @Override
    public String toString() {
        return "PathRequest{" +
                "source=" + source +
                ", target=" + target +
                ", age=" + age +
                '}';
    }
}
