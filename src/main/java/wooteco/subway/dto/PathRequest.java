package wooteco.subway.dto;

import javax.validation.constraints.Min;

public class PathRequest {

    @Min(value = 1, message = "출발역은 1 이상이어야 합니다.")
    private final long source;

    @Min(value = 1, message = "도착역은 1 이상이어야 합니다")
    private final long target;

    @Min(value = 0, message = "나이는 0이상의 상수여야 합니다.")
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
