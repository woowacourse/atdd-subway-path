package wooteco.subway.path.application;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Service;
import wooteco.subway.line.application.LineService;
import wooteco.subway.line.domain.Line;
import wooteco.subway.line.domain.Section;
import wooteco.subway.path.dto.PathResponse;
import wooteco.subway.station.dao.StationDao;
import wooteco.subway.station.domain.Station;

import java.util.List;

@Service
public class PathService {
    private final StationDao stationDao;
    private final LineService lineService;

    public PathService(StationDao stationDao, LineService lineService) {
        this.stationDao = stationDao;
        this.lineService = lineService;
    }

    public PathResponse getDijkstraShortestPath(long sourceId, long targetId) {
        GraphPath shortestPath = findShortestPath(sourceId, targetId);

        List<Station> stationList = shortestPath.getVertexList();
        double distance = shortestPath.getWeight();

        return PathResponse.of(stationList, distance);
    }

    private GraphPath findShortestPath(long sourceId, long targetId) {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph = initializeGraph();

        DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graph);
        Station sourceStation = stationDao.findById(sourceId);
        Station targetStation = stationDao.findById(targetId);

        return dijkstraShortestPath.getPath(sourceStation, targetStation);
    }

    private WeightedMultigraph<Station, DefaultWeightedEdge> initializeGraph() {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph
                = new WeightedMultigraph(DefaultWeightedEdge.class);
        initializeStations(graph);
        initializeSections(graph);
        return graph;
    }

    private void initializeStations(WeightedMultigraph<Station, DefaultWeightedEdge> graph) {
        List<Station> stations = stationDao.findAll();
        for (Station station: stations) {
            graph.addVertex(station);
        }
    }

    private void initializeSections(WeightedMultigraph<Station, DefaultWeightedEdge> graph) {
        List<Line> lines = lineService.findLines();
        lines.forEach(line -> addSectionsByLine(graph, line));
    }

    private void addSectionsByLine(WeightedMultigraph<Station, DefaultWeightedEdge> graph, Line line) {
        List<Section> sections = line.getSections().getSections();
        for (Section section: sections) {
            graph.setEdgeWeight(graph.addEdge(section.getUpStation(), section.getDownStation()), section.getDistance());
        }
    }
}
