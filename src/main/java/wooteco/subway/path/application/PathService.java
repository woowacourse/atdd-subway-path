package wooteco.subway.path.application;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Service;
import wooteco.subway.line.dao.LineDao;
import wooteco.subway.line.domain.Line;
import wooteco.subway.line.domain.Section;
import wooteco.subway.line.domain.Sections;
import wooteco.subway.path.dto.PathResponse;
import wooteco.subway.station.dao.StationDao;
import wooteco.subway.station.domain.Station;
import wooteco.subway.station.dto.StationResponse;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PathService {
    private final LineDao lineDao;
    private final StationDao stationDao;

    public PathService(LineDao lineDao, StationDao stationDao) {
        this.lineDao = lineDao;
        this.stationDao = stationDao;
    }

    public PathResponse findShortestPath(Long sourceId, Long targetId) {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph = loadCurrentSubwayMap();
        Station source = stationDao.findById(sourceId);
        Station target = stationDao.findById(targetId);
        return calculateShortestPath(graph, source, target);
    }

    private WeightedMultigraph<Station, DefaultWeightedEdge> loadCurrentSubwayMap() {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        loadStationInfo(graph);
        loadLineInfo(graph);
        return graph;
    }

    private void loadStationInfo(WeightedMultigraph<Station, DefaultWeightedEdge> graph) {
        final List<Station> stations = stationDao.findAll();
        for (Station station : stations) {
            graph.addVertex(station);
        }
    }

    private void loadLineInfo(WeightedMultigraph<Station, DefaultWeightedEdge> graph) {
        final List<Line> lines = lineDao.findAll();
        for (Line line : lines) {
            final Sections sections = line.getSections();
            loadSectionInfo(graph, sections);
        }
    }

    private void loadSectionInfo(WeightedMultigraph<Station, DefaultWeightedEdge> graph, Sections sections) {
        final List<Section> sectionsList = sections.getSections();
        for (Section section : sectionsList) {
            graph.setEdgeWeight(graph.addEdge(section.getUpStation(), section.getDownStation()), section.getDistance());
        }
    }

    private PathResponse calculateShortestPath(WeightedMultigraph<Station, DefaultWeightedEdge> graph, Station source,
                                               Station target) {
        DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
        final GraphPath<Station, DefaultWeightedEdge> shortestGraphPath = dijkstraShortestPath.getPath(source, target);
        final List<Station> shortestPath = shortestGraphPath.getVertexList();
        final double distance = shortestGraphPath.getWeight();
        return PathResponse.of(shortestPath, distance);
    }
}
