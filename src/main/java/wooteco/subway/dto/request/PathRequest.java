package wooteco.subway.dto.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class PathRequest {

    @NotNull(message = "출발역을 선택해주세요.")
    private Long source;

    @NotNull(message = "도착역을 선택해주세요.")
    private Long target;

    @Positive(message = "나이는 1 이상이어야 합니다.")
    private int age;

    private PathRequest() {
    }

    public PathRequest(final Long source, final Long target, final int age) {
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
