package wooteco.subway.service;

import org.springframework.stereotype.Service;
import wooteco.subway.dao.SectionDao;
import wooteco.subway.dao.StationDao;
import wooteco.subway.domain.Sections;
import wooteco.subway.domain.Station;
import wooteco.subway.domain.path.Path;
import wooteco.subway.domain.path.PathFinder;
import wooteco.subway.dto.PathResponse;
import wooteco.subway.util.FareCalculator;

@Service
public class PathService {

    private final SectionDao sectionDao;
    private final StationDao stationDao;

    public PathService(SectionDao sectionDao, StationDao stationDao) {
        this.sectionDao = sectionDao;
        this.stationDao = stationDao;
    }

    public PathResponse getPath(Long sourceId, Long targetId, int age) {
        PathFinder pathFinder = new PathFinder();
        Path path = pathFinder.getShortestPath(getStationById(sourceId), getStationById(targetId),
                new Sections(sectionDao.findAll()));

        return PathResponse.from(path, FareCalculator.calculate(path.getDistance()));
    }

    private Station getStationById(Long id) {
        return stationDao.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("지하철역이 존재하지 않습니다."));
    }
}
