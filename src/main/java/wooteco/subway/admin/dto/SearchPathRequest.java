package wooteco.subway.admin.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import wooteco.subway.admin.domain.PathType;

public class SearchPathRequest {
    @NotBlank
    private String source;

    @NotBlank
    private String target;

    @NotNull
    private PathType pathType;

    private SearchPathRequest() {
    }

    public SearchPathRequest(String source, String target, PathType pathType) {
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

    public PathType getPathType() {
        return pathType;
    }
}
