package wooteco.subway.path.application;

import org.springframework.stereotype.Service;
import wooteco.subway.line.dao.LineDao;
import wooteco.subway.path.domain.NewPathFinder;
import wooteco.subway.path.dto.PathResponse;
import wooteco.subway.station.application.StationService;
import wooteco.subway.station.domain.Station;

@Service
public class NewPathService {

    private LineDao lineDao;
    private StationService stationService;
    private NewPathFinder pathFinder;

    public NewPathService(LineDao lineDao, StationService stationService) {
        this.stationService = stationService;
        this.lineDao = lineDao;
        this.pathFinder = new NewPathFinder(lineDao.findAll());
    }

    public PathResponse findShortestPath(final Long sourceId, final Long targetId) {
        final Station source = stationService.findStationById(sourceId);
        final Station target = stationService.findStationById(targetId);

        return new PathResponse(pathFinder.shortest(source, target));
    }

    public void update() {
        pathFinder = pathFinder.update(lineDao.findAll());
    }
}
