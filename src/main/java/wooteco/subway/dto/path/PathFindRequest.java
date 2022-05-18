package wooteco.subway.dto.path;

import javax.validation.constraints.Positive;

public class PathFindRequest {

    @Positive(message = "출발지의 id는 양수 값만 들어올 수 있습니다.")
    private long source;

    @Positive(message = "도착지의 id는 양수 값만 들어올 수 있습니다.")
    private long target;

    @Positive(message = "나이는 양수 값만 들어올 수 있습니다.")
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
