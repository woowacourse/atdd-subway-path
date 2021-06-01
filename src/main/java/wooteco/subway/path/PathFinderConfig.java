package wooteco.subway.path;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import wooteco.subway.line.dao.LineDao;
import wooteco.subway.line.domain.Line;
import wooteco.subway.path.infra.DijkstraPathFinder;
import wooteco.subway.path.domain.PathFinder;
import wooteco.subway.path.domain.SubwayGraph;

import java.util.List;

@Configuration
public class PathFinderConfig {

    @Bean
    public PathFinder pathFinder(LineDao lineDao) {
        SubwayGraph subwayGraph = new SubwayGraph(DefaultWeightedEdge.class);
        List<Line> lines = lineDao.findAll();
        subwayGraph.addVertices(lines);
        subwayGraph.addEdges(lines);
        return new DijkstraPathFinder(subwayGraph, new DijkstraShortestPath(subwayGraph));
    }
}
