package wooteco.subway.domain.path;

import java.util.List;

public interface Navigator<V, E> {

    List<E> calculateShortestPath(V source, V target);
}
