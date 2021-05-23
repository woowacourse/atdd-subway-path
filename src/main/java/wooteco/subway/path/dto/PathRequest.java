package wooteco.subway.path.dto;

import javax.validation.constraints.NotNull;

public class PathRequest {
    @NotNull(message = "널일 수 없습니다.")
    private Long source;
    @NotNull(message = "널일 수 없습니다.")
    private Long target;

    public PathRequest() {
    }

    public PathRequest(Long source, Long target) {
        this.source = source;
        this.target = target;
    }


    public Long getSource() {
        return source;
    }

    public void setSource(Long source) {
        this.source = source;
    }

    public Long getTarget() {
        return target;
    }

    public void setTarget(Long target) {
        this.target = target;
    }
}
