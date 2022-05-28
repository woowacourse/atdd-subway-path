package wooteco.subway.ui.dto;

import javax.validation.constraints.NotNull;
import wooteco.subway.service.dto.PathServiceRequest;

public class PathRequest {

    @NotNull
    private Long source;

    @NotNull
    private Long target;

    @NotNull
    private Integer age;

    public PathRequest(Long source, Long target, Integer age) {
        this.source = source;
        this.target = target;
        this.age = age;
    }

    public PathServiceRequest toServiceRequest() {
        return new PathServiceRequest(source, target, age);
    }
}
