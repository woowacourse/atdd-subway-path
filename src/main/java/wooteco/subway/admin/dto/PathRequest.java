package wooteco.subway.admin.dto;

import javax.validation.constraints.NotBlank;

public class PathRequest {
    @NotBlank
    private String source;
    @NotBlank
    private String target;
    @NotBlank
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

