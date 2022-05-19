package wooteco.subway.dto;

import org.jetbrains.annotations.NotNull;

public class PathRequest {

    @NotNull
    private Long source;

    @NotNull
    private Long target;

    @NotNull
    private int age;

    public PathRequest(@NotNull Long source, @NotNull Long target, @NotNull int age) {
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
