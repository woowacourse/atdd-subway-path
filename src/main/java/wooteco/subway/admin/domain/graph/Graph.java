package wooteco.subway.admin.domain.graph;

public interface Graph {

    SubwayPath getPath(Long sourceStationId, Long targetStationId);

    boolean containAllVertexes(final Long sourceStationId, final Long targetStationId);
}
