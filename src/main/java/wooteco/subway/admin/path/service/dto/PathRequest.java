package wooteco.subway.admin.path.service.dto;

import javax.validation.constraints.NotBlank;

public class PathRequest {

    @NotBlank(message = "출발역이 존재하지 않습니다.")
    private Long source;

    @NotBlank(message = "도착역이 존재하지 않습니다.")
    private Long target;

    @NotBlank(message = "최단 경로를 검색할 형식이 존재하지 않습니다.")
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

    public Long getTarget() {
        return target;
    }

    public String getType() {
        return type;
    }

}
