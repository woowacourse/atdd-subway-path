package wooteco.subway.admin.dto.req;

import javax.validation.constraints.NotEmpty;

public class PathRequest {
    @NotEmpty(message = "출발역을 입력해주세요.")
    private String source;
    @NotEmpty(message = "도착역을 입력해주세요.")
    private String target;
    @NotEmpty(message = "거리, 혹은 시간 기준을 입력해주세요.")
    private String criteria;

    public PathRequest() {
    }

    public PathRequest(String source, String target, String criteria) {
        this.source = source;
        this.target = target;
        this.criteria = criteria;
    }

    public String getSource() {
        return source;
    }

    public String getTarget() {
        return target;
    }

    public String getCriteria() {
        return criteria;
    }
}
