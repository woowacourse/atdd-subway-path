package wooteco.subway.domain.path;

import java.util.List;

public interface Navigator<T> {

    List<T> calculateShortestPath();
}
