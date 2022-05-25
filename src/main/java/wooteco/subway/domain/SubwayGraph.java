package wooteco.subway.domain;

public interface SubwayGraph {

    Path search(Station source, Station target);
}
