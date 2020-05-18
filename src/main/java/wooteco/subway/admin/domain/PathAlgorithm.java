package wooteco.subway.admin.domain;

public interface PathAlgorithm {

    PathResult findPath(Long sourceId, Long targetId, Graph graph);
}
