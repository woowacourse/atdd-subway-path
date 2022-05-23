package wooteco.subway.dto;

import javax.validation.constraints.Positive;

public class PathRequest {

    private long source;
    private long target;
    @Positive(message = "나이는 양수여야 합니다.")
    private int age;

    public PathRequest(Long source, Long target, Integer age) {
        this.source = source;
        this.target = target;
        this.age = age;
    }

    public long getSource() {
        return source;
    }

    public long getTarget() {
        return target;
    }

    public int getAge() {
        return age;
    }
}
