package wooteco.subway.controller.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class PathRequest {

    @NotNull(message = "출발 역의 id 가 비었습니다.")
    private final Long source;

    @NotNull(message = "도착 역의 id 가 비었습니다.")
    private final Long target;

    @Positive(message = "연령 값은 음수일 수 없습니다.")
    private final int age;

    public PathRequest(Long source, Long target, int age) {
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

    public int getAge() {
        return age;
    }
}
