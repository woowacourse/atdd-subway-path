package wooteco.subway.admin.domain.line.path;

import java.util.List;

import wooteco.subway.admin.domain.line.path.vo.PathInfo;

public interface Path {
    List<Long> getPath();

    PathInfo createPathInfo();
}
