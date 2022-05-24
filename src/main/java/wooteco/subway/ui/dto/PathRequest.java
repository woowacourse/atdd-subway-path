package wooteco.subway.ui.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class PathRequest {
    @NotNull(message = "출발역을 입력해주세요.")
    private final Long source;

    @NotNull(message = "도착역을 입력해주세요.")
    private final Long target;

    @NotNull(message = "나이를 입력해주세요.")
    @Positive(message = "나이에는 양수를 입력해주세요.")
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
