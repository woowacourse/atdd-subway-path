package wooteco.subway.admin.dto;

public class PathRequest {
    private String source;
    private String target;
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
