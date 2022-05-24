package wooteco.subway.ui.dto;

import wooteco.subway.service.dto.PathServiceRequest;

public class PathRequest {

    private Long source;
    private Long target;
    private Integer age;

    public PathRequest(final Long source, final Long target, final Integer age) {
        this.source = source;
        this.target = target;
        this.age = age;
    }

    public PathServiceRequest toServiceRequest() {
        return new PathServiceRequest(source, target, age);
    }
}
