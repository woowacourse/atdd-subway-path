package wooteco.subway.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class PathRequest {

    @NotNull(message = "출발역 ID가 필요합니다.")
    private Long source;
    @NotNull(message = "도착역 ID가 필요합니다.")
    private Long target;
    @Min(value = 1, message = "나이는 1살 이상이여야 합니다.")
    private int age;

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
