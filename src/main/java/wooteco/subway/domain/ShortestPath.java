package wooteco.subway.domain;

public interface ShortestPath {

    Path getPath(final Station source, final Station target, final int extraFare);

}
