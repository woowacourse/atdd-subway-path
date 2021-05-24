package wooteco.subway.path.application;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.interfaces.ShortestPathAlgorithm;
import org.springframework.stereotype.Component;
import wooteco.subway.exception.ValidationFailureException;
import wooteco.subway.path.domain.Path;
import wooteco.subway.path.domain.PathEdge;
import wooteco.subway.path.exception.RoutingFailureException;
import wooteco.subway.station.domain.Station;

@Component
public class PathRouter {

    private final ShortestPathAlgorithm<Station, PathEdge> shortestPathAlgorithm;

    public PathRouter(ShortestPathAlgorithm<Station, PathEdge> shortestPathAlgorithm) {
        this.shortestPathAlgorithm = shortestPathAlgorithm;
    }

    public Path findByShortest(Station departureStation, Station arrivalStation) {
        GraphPath<Station, PathEdge> graphPath = shortestPathAlgorithm.getPath(departureStation, arrivalStation);

        try {
            return new Path(graphPath.getEdgeList());
        } catch (ValidationFailureException e) {
            throw new RoutingFailureException(e.getMessage());
        }
    }
}
