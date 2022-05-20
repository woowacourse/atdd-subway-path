package wooteco.subway.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.dao.StationDao;
import wooteco.subway.domain.Path;
import wooteco.subway.domain.Station;
import wooteco.subway.dto.PathResponse;
import wooteco.subway.exception.NoSuchStationException;

import java.util.List;

@Transactional(readOnly = true)
@Service
public class PathService {
    private final SectionService sectionService;
    private final StationDao stationDao;

    public PathService(final SectionService sectionService, final StationDao stationDao) {
        this.sectionService = sectionService;
        this.stationDao = stationDao;
    }

    public PathResponse findPath(final Long sourceId, final Long targetId, final int age) {
        Path path = new Path(sectionService.findAll());

        Station sourceStation = stationDao.findById(sourceId)
                .orElseThrow(() -> new NoSuchStationException(sourceId));
        Station targetStation = stationDao.findById(targetId)
                .orElseThrow(() -> new NoSuchStationException(targetId));

        List<Station> paths = path.findRoute(sourceStation, targetStation);
        int distance = path.calculateDistance(sourceStation, targetStation);
        int fare = path.calculateFare(sourceStation, targetStation);
        return PathResponse.from(paths, distance, fare);
    }
}
