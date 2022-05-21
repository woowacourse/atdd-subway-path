package wooteco.subway.domain.path;

import java.util.List;

public interface Navigator2<V, E> {

    List<E> calculateShortestPath(V source, V target);
}
