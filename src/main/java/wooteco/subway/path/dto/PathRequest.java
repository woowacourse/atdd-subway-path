package wooteco.subway.path.dto;

import javax.validation.constraints.NotBlank;

public class PathRequest {

    @NotBlank(message = "출발역을 입력하세요.")
    private Long source;
    @NotBlank(message = "도착역을 입력하세요.")
    private Long target;

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
