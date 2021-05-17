package wooteco.subway.path.service;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.line.dao.LineDao;
import wooteco.subway.line.domain.Line;
import wooteco.subway.line.domain.Section;
import wooteco.subway.path.controller.domain.ShortestPath;
import wooteco.subway.path.controller.dto.PathResponse;
import wooteco.subway.station.dao.StationDao;
import wooteco.subway.station.domain.Station;

import java.util.List;

@Service
@Transactional
public class PathService {
    private final LineDao lineDao;
    private final StationDao stationDao;

    public PathService(LineDao lineDao, StationDao stationDao) {
        this.lineDao = lineDao;
        this.stationDao = stationDao;
    }

    public PathResponse findShortestPath(Long sourceId, Long targetId) {
        final List<Line> lines = lineDao.findAll();
        final Station source = stationDao.findById(sourceId);
        final Station target = stationDao.findById(targetId);

        final WeightedMultigraph<Station, DefaultWeightedEdge> graph = addAllStationsInLine(lines);
        ShortestPath shortestPath = new ShortestPath(graph);
        final List<Station> shor = shortestPath.getShortestPath(source, target);
        final double distance = shortestPath.getDistance(source, target);
        return PathResponse.of(shor, (int) distance);
    }

    private WeightedMultigraph<Station, DefaultWeightedEdge> addAllStationsInLine(List<Line> lines) {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph
                = new WeightedMultigraph<>(DefaultWeightedEdge.class);

        for (Line line : lines) {
            final List<Station> stations = line.getStations();
            stations.forEach(graph::addVertex);
            final List<Section> sections = line.getSections().getSections();
            sections.forEach(section -> graph.setEdgeWeight(
                    graph.addEdge(section.getUpStation(), section.getDownStation()),
                    section.getDistance()
            ));
        }

        return graph;
    }
}
