package wooteco.subway.path.application;

import java.util.List;
import org.springframework.stereotype.Service;
import wooteco.subway.line.dao.SectionDao;
import wooteco.subway.line.domain.Section;
import wooteco.subway.path.domain.Path;
import wooteco.subway.path.dto.PathRequest;
import wooteco.subway.path.dto.PathResponse;
import wooteco.subway.station.dao.StationDao;
import wooteco.subway.station.domain.Station;

@Service
public class PathService {

    private final StationDao stationDao;
    private final SectionDao sectionDao;
    private final PathFinder pathFinder;

    public PathService(StationDao stationDao, SectionDao sectionDao,
        PathFinder pathFinder) {
        this.stationDao = stationDao;
        this.sectionDao = sectionDao;
        this.pathFinder = pathFinder;
    }

    public PathResponse findPath(PathRequest pathRequest) {
        List<Station> stations = stationDao.findAll();
        List<Section> sections = sectionDao.findAll();
        Path path = pathFinder.findPath(stations, sections, pathRequest);
        return PathResponse.of(path);
    }
}
