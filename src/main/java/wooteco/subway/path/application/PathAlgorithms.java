package wooteco.subway.path.application;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import wooteco.subway.path.dto.PathDto;
import wooteco.subway.path.dto.PathRequest;

public interface PathAlgorithms {

    PathDto findPath(WeightedMultigraph<Long, DefaultWeightedEdge> graph, PathRequest pathRequest);
}
