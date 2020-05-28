package wooteco.subway.admin.domain.path;

public interface Graph {
    Path findPath(Long sourceId, Long targetId);
}