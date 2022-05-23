package wooteco.subway.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class PathRequest {

    @NotNull
    private Long source;

    @NotNull
    private Long target;

    @NotNull
    @Min(0)
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

    public void setSource(Long source) {
        this.source = source;
    }

    public void setTarget(Long target) {
        this.target = target;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
