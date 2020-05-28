package wooteco.subway.admin.dto;

public class ShortestPathResponse {
    private String source;

    private String target;
    private String pathType;

    public ShortestPathResponse() {
    }

    public ShortestPathResponse(String source, String target, String pathType) {
        this.source = source;
        this.target = target;
        this.pathType = pathType;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getPathType() {
        return pathType;
    }

    public void setPathType(String pathType) {
        this.pathType = pathType;
    }
}


