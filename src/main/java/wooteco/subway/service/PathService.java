package wooteco.subway.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import wooteco.subway.dao.SectionDao;
import wooteco.subway.dao.StationDao;
import wooteco.subway.domain.BasicFareStrategy;
import wooteco.subway.domain.Fare;
import wooteco.subway.domain.Path;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Station;
import wooteco.subway.dto.request.PathRequest;
import wooteco.subway.dto.response.PathResponse;
import wooteco.subway.exception.NotFoundStationException;

@Service
public class PathService {

    private static final String NOT_FOUND_STATION_ERROR_MESSAGE = "해당하는 역이 존재하지 않습니다.";
    private static final int BASIC_FARE = 1250;

    private final StationDao stationDao;
    private final SectionDao sectionDao;

    public PathService(StationDao stationDao, SectionDao sectionDao) {
        this.stationDao = stationDao;
        this.sectionDao = sectionDao;
    }

    public PathResponse createShortestPath(PathRequest pathRequest) {
        Long source = pathRequest.getSource();
        Long target = pathRequest.getTarget();

        List<Section> sections = sectionDao.findAll();

        Path path = new Path(sections);
        List<Long> shortestPath = path.createShortestPath(source, target);

        List<Station> stations = shortestPath.stream()
                .map(station -> stationDao.findById(station)
                        .orElseThrow(() -> new NotFoundStationException(NOT_FOUND_STATION_ERROR_MESSAGE)))
                .collect(Collectors.toList());

        int distance = path.calculateDistance(source, target);
        Fare fare = new Fare(BASIC_FARE);

        return new PathResponse(stations, distance,
                fare.calculateFare(distance, new BasicFareStrategy()));
    }
}
