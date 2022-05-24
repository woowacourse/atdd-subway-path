package wooteco.subway.dto;

import javax.validation.constraints.Min;

public class PathRequest {

    private final long source;
    private final long target;

    @Min(value = 0, message = "연령은 양수이어야 합니다.")
    private final int age;

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
