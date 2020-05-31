package wooteco.subway.admin.dto;

public class PathRequest {
    private String source;
    private String target;
    private String type;

    private PathRequest() {
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

    public void setSource(String source) {
        this.source = source;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public void setType(String type) {
        this.type = type;
    }
}
