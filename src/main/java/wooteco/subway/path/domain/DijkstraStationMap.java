package wooteco.subway.path.domain;

import java.util.List;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import wooteco.subway.line.domain.Line;
import wooteco.subway.station.domain.Station;

public final class DijkstraStationMap extends AbstractStationMap {

    private final DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraPath;

    public DijkstraStationMap(List<Line> lines) {
        super(lines);
        this.dijkstraPath = new DijkstraShortestPath<>(getMap());
    }

    @Override
    public Path pathOf(Station source, Station target) {
        GraphPath<Station, DefaultWeightedEdge> graphPath = dijkstraPath.getPath(source, target);

        List<Station> stations = graphPath.getVertexList();
        int distance = (int) graphPath.getWeight();
        return new Path(stations, distance);
    }
}
