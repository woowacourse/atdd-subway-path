package wooteco.subway.domain.path;

import java.util.List;
import wooteco.subway.domain.Sections;

public interface PathFindStrategy {

    FindPathResult findPath(final Sections sections, final long sourceId, final long targetId);
}
