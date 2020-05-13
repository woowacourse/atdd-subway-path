package wooteco.subway.admin.domain;

import java.util.List;

public class SubwayGraphs {
    private final List<SubwayGraph> subwayGraphs;

    public SubwayGraphs(final List<SubwayGraph> subwayGraphs) {
        this.subwayGraphs = subwayGraphs;
    }

    public SubwayGraph getShortestPath(final Long sourceStationId, final Long targetStationId) {
        return subwayGraphs.stream()
                .filter(subwayGraph -> subwayGraph.containAllVertexes(sourceStationId, targetStationId))
                .min(SubwayGraph.weightComparator(sourceStationId, targetStationId))
                .orElseThrow(() -> new IllegalArgumentException(String.format("%d - %d : 존재하지 않는 경로입니다.", sourceStationId, targetStationId)));
    }
}
