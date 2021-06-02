package wooteco.subway.path.application;

import java.util.Objects;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.interfaces.ShortestPathAlgorithm;
import org.springframework.stereotype.Service;
import wooteco.subway.path.domain.Path;
import wooteco.subway.path.domain.PathEdge;
import wooteco.subway.path.exception.RoutingFailureException;
import wooteco.subway.station.domain.Station;

@Service
public class PathRouter {

    private final ShortestPathAlgorithm<Station, PathEdge> shortestPathAlgorithm;

    public PathRouter(ShortestPathAlgorithm<Station, PathEdge> shortestPathAlgorithm) {
        this.shortestPathAlgorithm = shortestPathAlgorithm;
    }

    public Path findByShortest(Station departureStation, Station arrivalStation) {
        GraphPath<Station, PathEdge> graphPath = shortestPathAlgorithm.getPath(departureStation, arrivalStation);
        validateExistentPath(graphPath);
        return new Path(graphPath.getEdgeList());
    }

    private void validateExistentPath(GraphPath<Station, PathEdge> graphPath) {
        if (Objects.isNull(graphPath)) {
            throw new RoutingFailureException("경로 없음");
        }
    }
}
