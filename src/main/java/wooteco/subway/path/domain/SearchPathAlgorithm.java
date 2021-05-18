package wooteco.subway.path.domain;

import java.util.List;

public interface SearchPathAlgorithm<T> {
    void add(T vertex1, T vertex2, int weight);

    List<T> getShortestPath(T vertex1, T vertex2);

    int getShortedDistance(T vertex1, T vertex2);
}
