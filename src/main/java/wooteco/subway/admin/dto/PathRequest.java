package wooteco.subway.admin.dto;

import javax.validation.constraints.NotEmpty;

public class PathRequest {
    @NotEmpty(message = "출발역이 비어있습니다")
    private String source;

    @NotEmpty(message = "도착역이 비어있습니다")
    private String target;

    @NotEmpty(message = "경로 타입이 비어있습니다")
    private String type;

    public PathRequest(String source, String target, String type) {
        this.source = source;
        this.target = target;
        this.type = type;
    }

    public String getSource() {
        return source;
    }

    public String getTarget() {
        return target;
    }

    public String getType() {
        return type;
    }
}
