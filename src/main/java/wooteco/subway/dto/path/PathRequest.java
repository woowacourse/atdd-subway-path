package wooteco.subway.dto.path;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class PathRequest {

    @NotNull
    private final Long source;

    @NotNull
    private final Long target;

    @Positive
    private final int age;

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
