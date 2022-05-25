package wooteco.subway.domain;

public interface Graph {

    ShortestPath getShortestPath(final Station source, final Station target);
}
