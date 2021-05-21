package wooteco.subway.path.application;

import org.springframework.stereotype.Service;
import wooteco.subway.line.dao.SectionDao;
import wooteco.subway.line.domain.Line;
import wooteco.subway.line.domain.Section;
import wooteco.subway.path.domain.Path;
import wooteco.subway.path.dto.PathResponse;
import wooteco.subway.station.application.StationService;
import wooteco.subway.station.domain.Station;
import wooteco.subway.station.dto.StationResponse;

import java.util.List;

@Service
public class PathService {
    private final SectionDao sectionDao;
    private final StationService stationService;
    private final Path path;

    public PathService(SectionDao sectionDao, StationService stationService) {
        this.sectionDao = sectionDao;
        this.stationService = stationService;
        this.path = new Path(stationService.findAll(), sectionDao.findAll());
    }

    public PathResponse findShortestPath(Long sourceId, Long targetId) {
        Station source = stationService.findStationById(sourceId);
        Station target = stationService.findStationById(targetId);

        List<StationResponse> stationResponses = StationResponse.listOf(path.shortestPath(source, target));
        int distance = path.shortestPathLength(source, target);

        return new PathResponse(stationResponses, distance);
    }

    public void refreshPath() {
        path.refresh(stationService.findAll(), sectionDao.findAll());
    }
}
