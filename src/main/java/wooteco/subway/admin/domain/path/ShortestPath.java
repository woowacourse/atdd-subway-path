package wooteco.subway.admin.domain.path;

import java.util.List;
import org.jgrapht.alg.interfaces.ShortestPathAlgorithm;

public class ShortestPath {

    private final ShortestPathAlgorithm<Long, WeightedEdge> path;

    private ShortestPath(ShortestPathAlgorithm<Long, WeightedEdge> path) {
        this.path = path;
    }

    public static ShortestPath of(ShortestPathAlgorithm<Long, WeightedEdge> path) {
        return new ShortestPath(path);
    }

    public ShortestPathAlgorithm<Long, WeightedEdge> getPath() {
        return path;
    }

    public List<Long> getVertexList(Long source, Long target) {
        try {
            return path.getPath(source, target).getVertexList();
        } catch (NullPointerException e) {
            throw new IllegalArgumentException("존재하지 않는 경로입니다.");
        }
    }

    public int getWeight(Long source, Long target) {
        return (int) path.getPath(source, target).getWeight();
    }

    public int getSubWeight(Long source, Long target) {
        return path.getPath(source, target)
            .getEdgeList()
            .stream()
            .mapToInt(WeightedEdge::getSubWeight)
            .sum();
    }
}
