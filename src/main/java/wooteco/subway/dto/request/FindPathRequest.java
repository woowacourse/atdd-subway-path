package wooteco.subway.dto.request;

import javax.validation.constraints.Positive;

public class FindPathRequest {
    private Long source;
    private Long target;
    @Positive(message = "나이는 0보다 커야합니다.")
    private int age;

    private FindPathRequest() {
    }

    public FindPathRequest(Long source, Long target, int age) {
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
