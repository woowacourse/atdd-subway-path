package wooteco.subway.admin.dto;

public class FindPathRequest {
    private String source;
    private String target;
    private String pathType;

    private FindPathRequest() {
    }

    public FindPathRequest(String source, String target, String pathType) {
        this.source = source;
        this.target = target;
        this.pathType = pathType;
    }

    public String getSource() {
        return source;
    }

    public String getTarget() {
        return target;
    }

    public String getPathType() {
        return pathType;
    }
}
