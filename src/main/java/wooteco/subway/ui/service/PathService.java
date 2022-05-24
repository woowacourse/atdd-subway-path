package wooteco.subway.ui.service;

import java.util.List;
import org.springframework.stereotype.Service;
import wooteco.subway.dao.LineDao;
import wooteco.subway.dao.StationDao;
import wooteco.subway.domain.Fare.FarePolicy;
import wooteco.subway.domain.Line;
import wooteco.subway.domain.PathCalculator;
import wooteco.subway.domain.Station;
import wooteco.subway.domain.path.ShortestPath;
import wooteco.subway.dto.response.PathResponse;
import wooteco.subway.dto.response.StationResponse;

@Service
public class PathService {
    private final LineDao lineDao;
    private final StationDao stationDao;

    public PathService(LineDao lineDao, StationDao stationDao) {
        this.lineDao = lineDao;
        this.stationDao = stationDao;
    }

    public PathResponse getPath(int source, int target) {
        List<Line> lines = lineDao.findAll();
        Station sourceStation = findStationById((long) source);
        Station targetStation = findStationById((long) target);

        ShortestPath shortestPath = PathCalculator.getShortestPath(lines, sourceStation, targetStation);
        List<Station> stations = shortestPath.getStations();
        int distance = shortestPath.getDistance().value();
        int extraFare = FarePolicy.getFare(shortestPath.getDistance()).value();
        return new PathResponse(StationResponse.of(stations), distance, extraFare);
    }

    private Station findStationById(Long id) {
        return stationDao.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(id + "에 해당하는 역을 찾을 수 없습니다."));
    }
}
