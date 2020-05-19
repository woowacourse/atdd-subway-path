package wooteco.subway.admin.dto.req;

import javax.validation.constraints.NotEmpty;

import wooteco.subway.admin.domain.CriteriaType;

public class PathRequest {
    @NotEmpty(message = "출발역을 입력해주세요.")
    private String source;
    @NotEmpty(message = "도착역을 입력해주세요.")
    private String target;
    private CriteriaType criteria;

    public PathRequest() {
    }

    public PathRequest(String source, String target, String criteria) {
        this.source = source;
        this.target = target;
        this.criteria = CriteriaType.of(criteria);
    }

    public String getSource() {
        return source;
    }

    public String getTarget() {
        return target;
    }

    public CriteriaType getCriteria() {
        return criteria;
    }
}
