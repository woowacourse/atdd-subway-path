package wooteco.subway.domain;

import java.util.List;

public interface PathFactory {

    int findShortestDistance(Long source, Long target);

    List<Long> findShortestPath(Long source, Long target);
}
