package wooteco.subway.service;

import org.springframework.stereotype.Service;
import wooteco.subway.dao.LineDao;
import wooteco.subway.dao.SectionDao;
import wooteco.subway.dao.StationDao;
import wooteco.subway.domain.*;
import wooteco.subway.dto.PathFindResponse;
import wooteco.subway.exception.NotFoundException;

import java.util.List;

@Service
public class PathService {

    private final LineDao lineDao;
    private final StationDao stationDao;
    private final SectionDao sectionDao;

    public PathService(LineDao lineDao, StationDao stationDao, SectionDao sectionDao) {
        this.lineDao = lineDao;
        this.stationDao = stationDao;
        this.sectionDao = sectionDao;
    }

    public PathFindResponse findPath(Long from, Long to, int age) {
        validateExistStations(from, to);
        List<Section> sections = sectionDao.findAll();
        List<Line> lines = lineDao.findAll();
        PathFinder pathFinder = new PathFinder(sections);
        List<Long> path = pathFinder.findPath(from, to);
        int distance = pathFinder.findDistance(from, to);
        FareCalculator fareCalculator = new FareCalculator(lines, sections);
        int fare = fareCalculator.calculateFare(path, distance, age);

        List<Station> stations = stationDao.findByIdIn(path);

        return PathFindResponse.of(stations, distance, fare);
    }

    private void validateExistStations(Long from, Long to) {
        if (!stationDao.existStationById(from) || !stationDao.existStationById(to)) {
            throw new NotFoundException("등록되지 않은 역으로는 구간을 만들 수 없습니다.");
        }
    }
}
