package wooteco.subway.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import wooteco.subway.Infrastructure.line.LineDao;
import wooteco.subway.Infrastructure.section.SectionDao;
import wooteco.subway.Infrastructure.station.StationDao;
import wooteco.subway.domain.*;
import wooteco.subway.dto.response.PathResponse;
import wooteco.subway.exception.constant.NotExistException;
import wooteco.subway.exception.constant.NotExistException.Which;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class PathService {

    private final FareCalculator fareCalculator;
    private final SectionDao sectionDao;
    private final StationDao stationDao;
    private final LineDao lineDao;

    public PathResponse findPath(Long from, Long to, Integer age) {
        validateExistStations(from, to);
        Sections sections = new Sections(sectionDao.findAll());
        PathFinder pathFinder = new PathFinder(sections);
        List<Long> path = pathFinder.findPath(from, to);
        int distance = pathFinder.findDistance(from, to);
        int fare = getFare(age, sections, distance);
        List<Station> stations = stationDao.findByIdIn(path);

        return new PathResponse(stations, distance, fare);
    }

    private int getFare(Integer age, Sections sections, int distance) {
        Set<Long> lineIds = sections.distinctLineIds();
        Lines lines = new Lines(lineDao.findByIdIn(lineIds));
        int maxExtraFare = lines.maxExtraFare();
        int fare = fareCalculator.calculateFare(distance, age) + maxExtraFare;
        return fare;
    }

    private void validateExistStations(Long from, Long to) {
        if (!stationDao.existById(from) || !stationDao.existById(to)) {
            throw new NotExistException(Which.STATION);
        }
    }
}
