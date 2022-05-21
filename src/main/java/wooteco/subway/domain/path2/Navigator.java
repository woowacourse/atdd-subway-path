package wooteco.subway.domain.path2;

import java.util.List;

public interface Navigator<T> {

    List<T> calculateShortestPath();
}
