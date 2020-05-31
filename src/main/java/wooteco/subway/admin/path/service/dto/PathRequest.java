package wooteco.subway.admin.path.service.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class PathRequest {

    @NotNull(message = "출발역이 존재하지 않습니다.")
    private Long source;

    @NotNull(message = "도착역이 존재하지 않습니다.")
    private Long target;

    @NotBlank(message = "최단 경로를 검색할 타입이 존재하지 않습니다.")
    private String type;

    public PathRequest() {
    }

    public PathRequest(final Long source, final Long target, final String type) {
        this.source = source;
        this.target = target;
        this.type = type;
    }

    public Long getSource() {
        return source;
    }

    public void setSource(final Long source) {
        this.source = source;
    }

    public Long getTarget() {
        return target;
    }

    public void setTarget(final Long target) {
        this.target = target;
    }

    public String getType() {
        return type;
    }

    public void setType(final String type) {
        this.type = type;
    }

}
