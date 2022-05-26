package wooteco.subway.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.dao.LineDao;
import wooteco.subway.dao.SectionDao;
import wooteco.subway.dao.StationDao;
import wooteco.subway.domain.Path;
import wooteco.subway.domain.PathStrategy;
import wooteco.subway.domain.Station;
import wooteco.subway.dto.PathRequest;
import wooteco.subway.dto.PathResponse;

import java.util.ArrayList;
import java.util.List;

@Service
public class PathService {

    private final SectionDao sectionDao;
    private final LineDao lineDao;
    private final StationDao stationDao;

    private final PathStrategy pathStrategy;

    public PathService(SectionDao sectionDao, LineDao lineDao, StationDao stationDao, PathStrategy pathStrategy) {
        this.sectionDao = sectionDao;
        this.lineDao = lineDao;
        this.stationDao = stationDao;
        this.pathStrategy = pathStrategy;
    }

    @Transactional(readOnly = true)
    public PathResponse findShortestPath(PathRequest pathRequest) {
        Path path = pathStrategy.findPath(sectionDao.findAll(), pathRequest.getSource(), pathRequest.getTarget());
        List<Station> stations = stationDao.findByIds(path.getStationIds());
        return new PathResponse(stations, path.getDistance(), path.calculateFare(lineDao.findAll(), pathRequest.getAge()));
    }
}
