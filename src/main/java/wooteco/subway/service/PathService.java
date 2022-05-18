package wooteco.subway.service;

import java.util.List;
import org.springframework.stereotype.Service;
import wooteco.subway.dao.SectionDao;
import wooteco.subway.dao.StationDao;
import wooteco.subway.domain.Path;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Station;
import wooteco.subway.dto.PathResponse;

@Service
public class PathService {

    private final StationDao stationDao;
    private final SectionDao sectionDao;

    public PathService(StationDao stationDao, SectionDao sectionDao) {
        this.stationDao = stationDao;
        this.sectionDao = sectionDao;
    }

    public PathResponse getPath(Long sourceStationId, Long targetStationId) {
        List<Section> sections = sectionDao.findAll();

        Station departure = stationDao.findById(sourceStationId);
        Station arrival = stationDao.findById(targetStationId);

        return new PathResponse(new Path(sections, departure, arrival));
    }
}
