package wooteco.subway.service;

import org.springframework.stereotype.Service;
import wooteco.subway.dao.SectionDao;
import wooteco.subway.dao.StationDao;
import wooteco.subway.domain.FareCalculator;
import wooteco.subway.domain.PathFinder;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Station;
import wooteco.subway.dto.PathFindResponse;
import wooteco.subway.exception.NotFoundException;

import java.util.List;

@Service
public class PathService {

    private final SectionDao sectionDao;
    private final StationDao stationDao;

    public PathService(SectionDao sectionDao, StationDao stationDao) {
        this.sectionDao = sectionDao;
        this.stationDao = stationDao;
    }

    public PathFindResponse findPath(Long from, Long to) {
        validateExistStations(from, to);
        List<Section> sections = sectionDao.findAll();
        PathFinder pathFinder = new PathFinder(sections);
        List<Long> path = pathFinder.findPath(from, to);
        int distance = pathFinder.findDistance(from, to);
        int fare = FareCalculator.calculateFare(distance);
        List<Station> stations = stationDao.findByIdIn(path);

        return PathFindResponse.of(stations, distance, fare);
    }

    private void validateExistStations(Long from, Long to) {
        if (!stationDao.existStationById(from) || !stationDao.existStationById(to)) {
            throw new NotFoundException("등록되지 않은 역으로는 구간을 만들 수 없습니다.");
        }
    }
}
