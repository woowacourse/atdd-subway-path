package wooteco.subway.path.application;

import org.springframework.stereotype.Service;
import wooteco.subway.line.dao.SectionDao;
import wooteco.subway.line.domain.Sections;
import wooteco.subway.path.domain.Path;
import wooteco.subway.path.dto.PathResponse;
import wooteco.subway.station.dao.StationDao;
import wooteco.subway.station.domain.Station;
import wooteco.subway.station.dto.StationResponse;

import java.util.List;

@Service
public class PathService {
    private final SectionDao sectionDao;
    private final StationDao stationDao;

    public PathService(SectionDao sectionDao, StationDao stationDao) {
        this.sectionDao = sectionDao;
        this.stationDao = stationDao;
    }

    public PathResponse findPath(Long sourceId, Long targetId) {
        Sections sections = sectionDao.findAll();
        Path path = new Path(sections);

        Station sourceStation = stationDao.findById(sourceId);
        Station targetStation = stationDao.findById(targetId);

        List<StationResponse> stationResponses = StationResponse.listOf(path.findPath(sourceStation, targetStation));
        int distance = path.findDistance(sourceStation, targetStation);

        return new PathResponse(stationResponses, distance);
    }
}
