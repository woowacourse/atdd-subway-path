package wooteco.subway.dto;

import javax.validation.constraints.NotNull;

public class PathRequest {

    @NotNull
    private Long source;
    @NotNull
    private Long target;
    @NotNull
    private Integer age;

    private PathRequest() {
    }

    public PathRequest(Long source, Long target, Integer age) {
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

    public Integer getAge() {
        return age;
    }
}
