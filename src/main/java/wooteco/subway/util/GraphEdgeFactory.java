package wooteco.subway.util;

import java.util.List;
import java.util.stream.Collectors;
import wooteco.subway.domain.Section;

public class GraphEdgeFactory {
    public static List<GraphEdgeRequest> from(List<Section> sections) {
        return sections.stream()
                .map(it -> {
                    var sourceVertex = it.getUpStationId();
                    var targetVertex = it.getDownStationId();
                    var weight = it.getDistance();
                    return new GraphEdgeRequest(sourceVertex, targetVertex, weight);
                })
                .collect(Collectors.toList());
    }
}
