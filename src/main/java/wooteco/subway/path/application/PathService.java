package wooteco.subway.path.application;

import org.springframework.stereotype.Service;
import wooteco.subway.line.application.LineService;
import wooteco.subway.line.domain.Line;
import wooteco.subway.path.domain.Path;
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
        List<Station> stations = stationDao.findAll();
        List<Line> lines = lineService.findLines();

        Path path = new Path(stations, lines);
        Station sourceStation = stationDao.findById(sourceId);
        Station targetStation = stationDao.findById(targetId);

        List<Station> stationList = path.findShortestPath(sourceStation, targetStation);
        double distance = path.findShortestDistance(sourceStation, targetStation);

        return PathResponse.of(stationList, distance);
    }
}
