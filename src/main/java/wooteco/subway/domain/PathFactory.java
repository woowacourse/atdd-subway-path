package wooteco.subway.domain;

import java.util.List;
import java.util.Set;

public interface PathFactory {

    int findShortestDistance(Long source, Long target);

    List<Long> findShortestPath(Long source, Long target);

    Set<Long> findLines(Long source, Long target);
}
