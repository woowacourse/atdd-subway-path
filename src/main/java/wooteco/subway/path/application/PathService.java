package wooteco.subway.path.application;

import java.util.List;
import java.util.stream.Collectors;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.line.application.LineService;
import wooteco.subway.line.dao.LineDao;
import wooteco.subway.line.domain.Line;
import wooteco.subway.line.domain.Sections;
import wooteco.subway.path.dto.PathResponse;
import wooteco.subway.station.dao.StationDao;
import wooteco.subway.station.dto.StationResponse;

@Service
public class PathService {

    private final LineDao lineDao;
    private final StationDao stationDao;

    public PathService(LineDao lineDao, StationDao stationDao) {
        this.lineDao = lineDao;
        this.stationDao = stationDao;
    }

    @Transactional(readOnly = true)
    public PathResponse findPath(Long source, Long target) {
        GraphPath<String, String> subwayDijkstraGraph = subwayGraph(source.toString(), target.toString());
        List<String> shortestSubwayPath = subwayDijkstraGraph.getVertexList();

        List<StationResponse> stationResponses = shortestSubwayPath.stream().map(it -> {
            return StationResponse.of(stationDao.findById(Long.parseLong(it)));
        }).collect(Collectors.toList());
        int distance = (int) subwayDijkstraGraph.getWeight();
        return new PathResponse(stationResponses, distance);
    }

    private GraphPath<String, String> subwayGraph(String sourceStationId, String targetStationId) {
        WeightedMultigraph<String, DefaultWeightedEdge> graph = new WeightedMultigraph(DefaultWeightedEdge.class);
        initStationVertexes(graph);
        initSectionWeights(graph);

        DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graph);
        return dijkstraShortestPath.getPath(sourceStationId, targetStationId);
    }

    private void initStationVertexes(WeightedMultigraph<String, DefaultWeightedEdge> graph) {
        stationDao.findAll().forEach(station -> {
            graph.addVertex(station.getId().toString());
        });
    }

    private void initSectionWeights(WeightedMultigraph<String, DefaultWeightedEdge> graph) {
        for (Line line : lineDao.findAll()) {
            Sections sections = line.getSections();
            sections.registerSectionWeights(graph);
        }
    }

}
