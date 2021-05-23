package wooteco.subway.path.application;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Service;
import wooteco.subway.line.dao.LineDao;
import wooteco.subway.line.domain.Line;
import wooteco.subway.line.domain.Section;
import wooteco.subway.line.domain.Sections;
import wooteco.subway.path.domain.SubwayMap;
import wooteco.subway.path.dto.PathResponse;
import wooteco.subway.station.dao.StationDao;
import wooteco.subway.station.domain.Station;

import java.util.List;
import java.util.Map;

@Service
public class PathService {
    private final LineDao lineDao;
    private final StationDao stationDao;

    public PathService(LineDao lineDao, StationDao stationDao) {
        this.lineDao = lineDao;
        this.stationDao = stationDao;
    }

    public PathResponse findShortestPath(Long sourceId, Long targetId) {
        WeightedMultigraph<Station, DefaultWeightedEdge> currentSubwayMap = loadCurrentSubwayMap();
        final SubwayMap subwayMap = new SubwayMap(currentSubwayMap);

        Station source = stationDao.findById(sourceId);
        Station target = stationDao.findById(targetId);

        final Map<List<Station>, Double> shortestPath = subwayMap.calculateShortestPath(source, target);
        final List<Station> shortestPathStations = shortestPath.keySet().iterator().next();
        final Double shortestDistance = shortestPath.get(shortestPathStations);
        return PathResponse.of(shortestPathStations, shortestDistance);
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
}
