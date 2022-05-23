package wooteco.subway.dto;

import javax.validation.constraints.Min;
import org.jetbrains.annotations.NotNull;

public class PathRequest {

    @NotNull
    private Long source;

    @NotNull
    private Long target;

    @Min(0)
    private int age;

    public PathRequest(@NotNull Long source, @NotNull Long target, int age) {
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
