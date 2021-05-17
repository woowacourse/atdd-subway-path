package wooteco.subway.path.service;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.path.domain.SubwayMap;
import wooteco.subway.path.dto.PathResponse;
import wooteco.subway.section.dao.SectionDao;
import wooteco.subway.section.domain.Section;
import wooteco.subway.station.domain.Station;
import wooteco.subway.station.dto.StationResponse;
import wooteco.subway.station.service.StationService;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class PathService {

    private final SectionDao sectionDao;
    private final StationService stationService;

    public PathService(SectionDao sectionDao, StationService stationService) {
        this.sectionDao = sectionDao;
        this.stationService = stationService;
    }

    public PathResponse findShortestPath(long sourceStationId, long targetStationId) {
        List<Section> sections = sectionDao.findAll();
        SubwayMap subwayMap = new SubwayMap(new WeightedMultigraph<>(DefaultWeightedEdge.class));
        sections.forEach(subwayMap::addSection);
        Station sourceStation = stationService.findStationById(sourceStationId);
        Station targetStation = stationService.findStationById(targetStationId);
        List<Station> shortestPath = subwayMap.findShortestPath(sourceStation, targetStation);
        int shortestDistance = subwayMap.findShortestDistance(sourceStation, targetStation);
        List<StationResponse> stations = StationResponse.listOf(shortestPath);
        return new PathResponse(stations, shortestDistance);
    }
}
