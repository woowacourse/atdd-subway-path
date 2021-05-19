package wooteco.subway.path.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.line.dao.SectionDao;
import wooteco.subway.line.domain.Section;
import wooteco.subway.path.domain.PathGraph;
import wooteco.subway.path.dto.PathResponse;
import wooteco.subway.station.dao.StationDao;
import wooteco.subway.station.domain.Station;
import wooteco.subway.station.dto.StationResponse;

import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
public class PathService {
    private final SectionDao sectionDao;
    private final StationDao stationDao;

    public PathService(SectionDao sectionDao, StationDao stationDao) {
        this.sectionDao = sectionDao;
        this.stationDao = stationDao;
    }

    public PathResponse findShortPath(Long source, Long target) {
        List<Section> sections = sectionDao.findAll();
        PathGraph pathGraph = new PathGraph(sections);
        List<StationResponse> shortestPath = getShortestPath(pathGraph, source, target);
        int shortestDistance = pathGraph.getShortestDistance(source, target);
        return new PathResponse(shortestPath, shortestDistance);
    }

    private List<StationResponse> getShortestPath(PathGraph pathGraph, Long source, Long target) {
        List<Station> shortestPath = pathGraph.getShortestPath(source, target);
        return shortestPath.stream()
                .map(station -> stationDao.findById(station.getId()))
                .map(StationResponse::of)
                .collect(Collectors.toList());
    }
}
