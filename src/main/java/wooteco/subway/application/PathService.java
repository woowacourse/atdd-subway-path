package wooteco.subway.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.dao.LineDao;
import wooteco.subway.dao.SectionDao;
import wooteco.subway.dao.StationDao;
import wooteco.subway.domain.*;
import wooteco.subway.dto.PathResponse;
import wooteco.subway.exception.NoSuchLineException;
import wooteco.subway.exception.NoSuchStationException;

@Transactional(readOnly = true)
@Service
public class PathService {
    private final SectionDao sectionDao;
    private final StationDao stationDao;
    private final LineDao lineDao;

    public PathService(final SectionDao sectionDao, final StationDao stationDao, final LineDao lineDao) {
        this.sectionDao = sectionDao;
        this.stationDao = stationDao;
        this.lineDao = lineDao;
    }

    public PathResponse findPath(final Long sourceId, final Long targetId, final int age) {
        ShortestPath subwayGraph = new SubwayGraph(sectionDao.findAll());
        PathAdapter pathAdapter = new PathAdapter(subwayGraph);

        Station sourceStation = stationDao.findById(sourceId)
                .orElseThrow(() -> new NoSuchStationException(sourceId));
        Station targetStation = stationDao.findById(targetId)
                .orElseThrow(() -> new NoSuchStationException(targetId));

        Long lineId = pathAdapter.getExpensiveLineId(sourceStation, targetStation);
        Line line = lineDao.findById(lineId).orElseThrow(() -> new NoSuchLineException(lineId));
        Path path = pathAdapter.getShortestPath(sourceStation, targetStation, line.getExtraFare(), age);

        return PathResponse.from(path);
    }
}
