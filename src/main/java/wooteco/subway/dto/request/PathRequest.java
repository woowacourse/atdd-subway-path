package wooteco.subway.dto.request;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class PathRequest {
    @NotNull(message = "출발역은 필수입니다.")
    private final Long source;
    @NotNull(message = "도착역은 필수입니다.")
    private final Long target;
    @NotNull(message = "나이는 필수입니다.")
    @Min(value = 0, message = "나이는 음수일 수 없습니다.")
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
