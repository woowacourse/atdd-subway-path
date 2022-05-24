package wooteco.subway.domain.path;

import java.util.List;
import wooteco.subway.domain.Sections;

public interface PathFindStrategy {

    List<Long> findPath(final Sections sections, final long sourceId, final long targetId);
}
