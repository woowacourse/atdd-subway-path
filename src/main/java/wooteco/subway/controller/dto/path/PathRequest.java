package wooteco.subway.controller.dto.path;

import javax.validation.constraints.Positive;

public class PathRequest {

    @Positive
    private Long source;
    @Positive
    private Long target;
    @Positive
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
