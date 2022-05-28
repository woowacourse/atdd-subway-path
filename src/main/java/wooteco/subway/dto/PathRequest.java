package wooteco.subway.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class PathRequest {
    @NotNull(message = "출발점 id는 null 이면 안됩니다.")
    private final Long source;
    @NotNull(message = "도착점 id는 null 이면 안됩니다.")
    private final Long target;
    @Positive(message = "나이는 0이하이면 안됩니다.")
    private final Integer age;

    public PathRequest(final Long source, final Long target, final Integer age) {
        this.source = source;
        this.target = target;
        this.age = age;
    }

    public Long getSource() {
        return source;
    }

    public Long getTarget() {
        return target;
    }

    public Integer getAge() {
        return age;
    }
}
