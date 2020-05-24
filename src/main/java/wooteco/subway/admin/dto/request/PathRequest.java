package wooteco.subway.admin.dto.request;

import wooteco.subway.admin.domain.vo.PathType;

public class PathRequest {

    private Long source;
    private Long target;
    private PathType type;

    public Long getSource() {
        return source;
    }

    public Long getTarget() {
        return target;
    }

    public PathType getType() {
        return type;
    }
}
