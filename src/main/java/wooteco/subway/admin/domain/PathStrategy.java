package wooteco.subway.admin.domain;

import wooteco.subway.admin.dto.PathResponse;

public interface PathStrategy {
    PathResponse getPath(final PathType type);
}
