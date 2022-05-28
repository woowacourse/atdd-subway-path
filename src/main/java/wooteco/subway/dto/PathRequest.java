package wooteco.subway.dto;

import javax.validation.constraints.NotNull;

public class PathRequest {

    @NotNull(message = "출발역을 입력해주세요.")
    private Long source;
    @NotNull(message = "도착역을 입력해주세요.")
    private Long target;
    @NotNull(message = "나이를 입력해주세요.")
    private Long age;

    public PathRequest(Long source, Long target, Long age) {
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

    public Long getAge() {
        return age;
    }
}
