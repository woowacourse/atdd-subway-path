package wooteco.subway.admin.domain.path;

public interface Graph {
    SubwayPath findPath(Long sourceId, Long targetId);
}