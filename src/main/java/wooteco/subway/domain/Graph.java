package wooteco.subway.domain;

import java.util.List;

public interface Graph {

    List<Station> findPath(Long source, Long target);

    int findDistance(Long source, Long target);
}
