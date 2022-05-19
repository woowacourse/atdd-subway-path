package wooteco.subway.domain;

public interface Graph {

    Path search(Station source, Station target);
}
