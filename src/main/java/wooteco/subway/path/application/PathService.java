package wooteco.subway.path.application;

import org.springframework.stereotype.Service;
import wooteco.subway.line.dao.SectionDao;
import wooteco.subway.path.domain.ShortestPath;
import wooteco.subway.path.dto.PathResponse;
import wooteco.subway.station.application.StationService;
import wooteco.subway.station.domain.Station;
import wooteco.subway.station.dto.StationResponse;

import java.util.List;

@Service
public class PathService {
    private final SectionDao sectionDao;
    private final StationService stationService;
    private final ShortestPath shortestPath;

    public PathService(SectionDao sectionDao, StationService stationService, ShortestPath shortestPath) {
        this.sectionDao = sectionDao;
        this.stationService = stationService;
        this.shortestPath = shortestPath;
        shortestPath.refresh(stationService.findAll(), sectionDao.findAll());
    }

    public PathResponse findShortestPath(Long sourceId, Long targetId) {
        Station source = stationService.findStationById(sourceId);
        Station target = stationService.findStationById(targetId);

        List<StationResponse> stationResponses = StationResponse.listOf(shortestPath.route(source, target));
        int distance = shortestPath.length(source, target);

        return new PathResponse(stationResponses, distance);
    }

    public void refreshPath() {
        shortestPath.refresh(stationService.findAll(), sectionDao.findAll());
    }
}
