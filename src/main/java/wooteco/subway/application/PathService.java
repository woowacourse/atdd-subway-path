package wooteco.subway.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import wooteco.subway.Infrastructure.line.LineDao;
import wooteco.subway.Infrastructure.section.SectionDao;
import wooteco.subway.Infrastructure.station.StationDao;
import wooteco.subway.domain.fare.FareCalculator;
import wooteco.subway.domain.line.Lines;
import wooteco.subway.domain.path.PathFinder;
import wooteco.subway.domain.path.PathFinderFactory;
import wooteco.subway.domain.section.Sections;
import wooteco.subway.domain.station.Station;
import wooteco.subway.dto.response.PathResponse;
import wooteco.subway.exception.constant.NotExistException;
import wooteco.subway.exception.constant.NotExistException.Which;

import java.util.Collection;
import java.util.List;

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
        PathFinder pathFinder = PathFinderFactory.create(sections);

        List<Long> path = pathFinder.findPath(from, to);
        int distance = pathFinder.findDistance(from, to);
        List<Long> lineIds = pathFinder.getLinesAlongSections(from, to);
        int fare = calculateAndGetFinalFare(age, distance, lineIds);
        List<Station> stations = stationDao.findByIdIn(path);

        return new PathResponse(stations, distance, fare);
    }

    private int calculateAndGetFinalFare(Integer age, int distance, Collection<Long> lineIds) {
        Lines lines = new Lines(lineDao.findByIdIn(lineIds));
        int maxExtraFare = lines.maxExtraFare();
        return fareCalculator.calculateFare(distance, age) + maxExtraFare;
    }

    private void validateExistStations(Long from, Long to) {
        if (!stationDao.existById(from) || !stationDao.existById(to)) {
            throw new NotExistException(Which.STATION);
        }
    }
}
