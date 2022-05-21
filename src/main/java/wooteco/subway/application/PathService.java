package wooteco.subway.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.dao.SectionDao;
import wooteco.subway.dao.StationDao;
import wooteco.subway.domain.Path;
import wooteco.subway.domain.SubwayGraph;
import wooteco.subway.domain.Station;
import wooteco.subway.dto.PathResponse;
import wooteco.subway.exception.NoSuchStationException;

import java.util.List;

@Transactional(readOnly = true)
@Service
public class PathService {
    private final SectionDao sectionDao;
    private final StationDao stationDao;

    public PathService(final SectionDao sectionDao, final StationDao stationDao) {
        this.sectionDao = sectionDao;
        this.stationDao = stationDao;
    }


    public PathResponse findPath(final Long sourceId, final Long targetId, final int age) {
        SubwayGraph subwayGraph = new SubwayGraph(sectionDao.findAll());

        Station sourceStation = stationDao.findById(sourceId)
                .orElseThrow(() -> new NoSuchStationException(sourceId));
        Station targetStation = stationDao.findById(targetId)
                .orElseThrow(() -> new NoSuchStationException(targetId));

        Path path = subwayGraph.getPath(sourceStation, targetStation);
        return PathResponse.from(path);
    }
}
