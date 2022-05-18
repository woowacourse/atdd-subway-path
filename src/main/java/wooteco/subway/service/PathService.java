package wooteco.subway.service;

import org.springframework.stereotype.Service;
import wooteco.subway.dao.SectionDao;
import wooteco.subway.dao.StationDao;
import wooteco.subway.domain.FareCalculator;
import wooteco.subway.domain.PathFinder;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Station;
import wooteco.subway.dto.PathFindResponse;

import java.util.List;

@Service
public class PathService {

    private final FareCalculator fareCalculator;
    private final SectionDao sectionDao;
    private final StationDao stationDao;

    public PathService(FareCalculator fareCalculator, SectionDao sectionDao, StationDao stationDao) {
        this.fareCalculator = fareCalculator;
        this.sectionDao = sectionDao;
        this.stationDao = stationDao;
    }

    public PathFindResponse findPath(Long from, Long to) {
        List<Section> sections = sectionDao.findAll();
        PathFinder pathFinder = new PathFinder(sections);
        List<Long> path = pathFinder.findPath(from, to);
        int distance = pathFinder.findDistance(from, to);
        int fare = fareCalculator.calculateFare(distance);
        List<Station> stations = stationDao.findByIdIn(path);

        return PathFindResponse.of(stations, distance, fare);
    }
}
