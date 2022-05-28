package wooteco.subway.infra;

import java.util.List;
import org.jgrapht.alg.interfaces.ShortestPathAlgorithm;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import wooteco.subway.domain.Section;

public class DijkstraStrategy extends ShortestPathStrategy{

    public DijkstraStrategy(List<Section> sections) {
        super(sections);
    }

    @Override
    public ShortestPathAlgorithm createShortestPath() {
        return new DijkstraShortestPath<>(super.getGraph());
    }
}
