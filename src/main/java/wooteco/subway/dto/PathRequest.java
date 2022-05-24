package wooteco.subway.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class PathRequest {
    @NotNull(message = "출발역을 입력해야 합니다.")
    private Long source;

    @NotNull(message = "도착역을 입력해야 합니다.")
    private Long target;

    @Min(value = 0, message = "올바른 나이를 입력해야 합니다.")
    private int age;

    private PathRequest() {
    }

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
