package wooteco.subway.path.dto;

import javax.validation.constraints.NotNull;

public class PathRequest {

    @NotNull
    private Long source;
    @NotNull
    private Long target;

    public Long getSource() {
        return source;
    }

    public Long getTarget() {
        return target;
    }

    public void setSource(Long source) {
        this.source = source;
    }

    public void setTarget(Long target) {
        this.target = target;
    }
}
