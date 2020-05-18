package wooteco.subway.admin.domain;

public interface SubwayGraph {
    SubwayPath generatePath(Station source, Station target);
}
