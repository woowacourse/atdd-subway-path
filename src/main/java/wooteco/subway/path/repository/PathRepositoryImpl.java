package wooteco.subway.path.repository;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Repository;
import wooteco.subway.line.dao.LineDao;
import wooteco.subway.line.domain.Line;
import wooteco.subway.line.domain.Sections;
import wooteco.subway.path.domin.Path;
import wooteco.subway.path.domin.PathRepository;
import wooteco.subway.station.domain.Station;

import java.util.List;

@Repository
public class PathRepositoryImpl implements PathRepository {
    private final LineDao lineDao;

    public PathRepositoryImpl(final LineDao lineDao) {
        this.lineDao = lineDao;
    }

    @Override
    public Path generateShortestDistancePath(final Station start, final Station destination) {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph = buildAllPath();

        DijkstraShortestPath dijkstraShortestPath
                = new DijkstraShortestPath(graph);

        GraphPath shortestPath = dijkstraShortestPath.getPath(start, destination);

        double distance = shortestPath.getWeight();

        return new Path(shortestPath.getVertexList(), (int) distance);
    }

    private WeightedMultigraph<Station, DefaultWeightedEdge> buildAllPath() {
        WeightedMultigraph graph = new WeightedMultigraph(DefaultWeightedEdge.class);

        List<Line> lines = lineDao.findAll();
        lines.forEach(line -> {
            registerNode(line.getStations(), graph);
            registerEdgeDistanceWeight(line.getSections(), graph);
        });

        return graph;
    }

    private void registerNode(List<Station> stations, WeightedMultigraph graph) {
        stations.forEach(graph::addVertex);
    }

    private void registerEdgeDistanceWeight(Sections sections, WeightedMultigraph graph) {
        sections.getSections().forEach(section -> {
            graph.addEdge(section.getUpStation(), section.getDownStation(), section.getDistance());
        });
    }


}
