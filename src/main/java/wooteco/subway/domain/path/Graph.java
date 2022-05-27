package wooteco.subway.domain.path;

import java.util.List;
import wooteco.subway.domain.Station;

public interface Graph {

    List<Station> findPath(Long source, Long target);

    int findDistance(Long source, Long target);

    List<Long> findLineIdsRelatedPath(Long source, Long target);
}
