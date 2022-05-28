package wooteco.subway.dto.path;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class PathRequest {

    @NotNull
    private final Long source;

    @NotNull
    private final Long target;

    @NotNull
    @Positive
    private final Integer age;

    public PathRequest(Long source, Long target, Integer age) {
        this.source = source;
        this.target = target;
        this.age = age;
    }

    public long getSourceStationId() {
        return source;
    }

    public long getTargetStationId() {
        return target;
    }

    public int getAge() {
        return age;
    }
}
