package wooteco.subway.path.application;

import org.springframework.stereotype.Service;
import wooteco.subway.line.dao.SectionDao;
import wooteco.subway.line.domain.Section;
import wooteco.subway.path.domain.Path;
import wooteco.subway.path.dto.PathResponse;
import wooteco.subway.station.dao.StationDao;
import wooteco.subway.station.domain.Station;
import wooteco.subway.station.dto.StationResponse;

import java.util.List;

@Service
public class PathService {

    private final StationDao stationDao;
    private final SectionDao sectionDao;

    public PathService(StationDao stationDao, SectionDao sectionDao) {
        this.stationDao = stationDao;
        this.sectionDao = sectionDao;
    }

    public PathResponse findPath(Long source, Long target) {
        List<Station> stations = stationDao.findAll();
        List<Section> sections = sectionDao.findAll();
        Path path = new Path(stations, sections);
        Station sourceStation = stationDao.findById(source);
        Station targetStation = stationDao.findById(target);

        return new PathResponse(StationResponse.listOf(
                path.shortestPath(sourceStation, targetStation)),
                path.shortestDistance(sourceStation, targetStation));
    }
}
