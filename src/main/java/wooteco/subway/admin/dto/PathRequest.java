package wooteco.subway.admin.dto;

import wooteco.subway.admin.domain.line.path.vo.PathSearchInfo;

public class PathRequest {
    private final String source;
    private final String target;

    public PathRequest(String source, String target) {
        this.source = source;
        this.target = target;
    }

    public String getSource() {
        return source;
    }

    public String getTarget() {
        return target;
    }

    public PathSearchInfo toPathInfo() {
        return new PathSearchInfo(source, target);
    }
}
