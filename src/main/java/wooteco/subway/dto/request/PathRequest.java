package wooteco.subway.dto.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

public class PathRequest {

    @NotNull
    @Positive
    private final Long source;
    @NotNull
    @Positive
    private final Long target;
    @PositiveOrZero(message = "나이는 0 이상의 수만 가능합니다.")
    private final Integer age;

    public PathRequest(Long source, Long target, Integer age) {
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
