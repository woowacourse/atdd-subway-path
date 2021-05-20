package wooteco.subway.path.ui.dto;

import wooteco.subway.path.ui.dto.valid.NumberValidation;

import java.beans.ConstructorProperties;

public class PathRequest {

    @NumberValidation
    private final Long source;
    @NumberValidation
    private final Long target;

    @ConstructorProperties({"source", "target"})
    public PathRequest(Long source, Long target) {
        this.source = source;
        this.target = target;
    }

    public Long getSource() {
        return source;
    }

    public Long getTarget() {
        return target;
    }

}
