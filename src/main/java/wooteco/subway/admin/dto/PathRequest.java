package wooteco.subway.admin.dto;

public class PathRequest {
    private String source;
    private String target;
    private String pathType;

    public PathRequest() {
    }

    public void setSource(String source) {
        this.source = source;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public void setPathType(String pathType) {
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

