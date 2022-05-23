package wooteco.subway.service;

import java.util.List;
import org.springframework.stereotype.Service;
import wooteco.subway.dao.SectionDao;
import wooteco.subway.dao.StationDao;
import wooteco.subway.domain.BasicFareStrategy;
import wooteco.subway.domain.Fare;
import wooteco.subway.domain.Path;
import wooteco.subway.domain.Station;
import wooteco.subway.dto.request.PathRequest;
import wooteco.subway.dto.response.PathResponse;
import wooteco.subway.exception.NotFoundStationException;

@Service
public class PathService {

    private static final String NOT_FOUND_STATION_ERROR_MESSAGE = "해당하는 역이 존재하지 않습니다.";

    private final StationDao stationDao;
    private final SectionDao sectionDao;

    public PathService(StationDao stationDao, SectionDao sectionDao) {
        this.stationDao = stationDao;
        this.sectionDao = sectionDao;
    }

    public PathResponse createShortestPath(PathRequest pathRequest) {
        Station source = stationDao.findById(pathRequest.getSource())
                .orElseThrow(() -> new NotFoundStationException(NOT_FOUND_STATION_ERROR_MESSAGE));
        Station target = stationDao.findById(pathRequest.getTarget())
                .orElseThrow(() -> new NotFoundStationException(NOT_FOUND_STATION_ERROR_MESSAGE));

        Path path = new Path(sectionDao.findAll());
        List<Station> shortestPath = path.createShortestPath(source, target);
        int distance = path.calculateDistance(source, target);
        Fare fare = new Fare();

        return new PathResponse(shortestPath, distance,
                fare.calculateFare(distance, new BasicFareStrategy()));
    }
}
