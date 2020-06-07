package wooteco.subway.admin.service;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;

public interface ShortestPathService {
    List<Long> getPath(Long source, Long target, WeightedMultigraph<Long, DefaultWeightedEdge> graph);
}
