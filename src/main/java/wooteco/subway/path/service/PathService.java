package wooteco.subway.path.service;

import java.util.List;
import org.springframework.stereotype.Service;
import wooteco.subway.line.dao.LineDao;
import wooteco.subway.line.domain.Line;
import wooteco.subway.path.domain.Path;
import wooteco.subway.path.dto.PathRequest;
import wooteco.subway.path.dto.PathResponse;
import wooteco.subway.station.dao.StationDao;
import wooteco.subway.station.domain.Station;
import wooteco.subway.station.exception.SameStationException;

@Service
public class PathService {

    private final LineDao lineDao;
    private final StationDao stationDao;

    public PathService(LineDao lineDao, StationDao stationDao) {
        this.lineDao = lineDao;
        this.stationDao = stationDao;
    }

    public PathResponse optimalPath(PathRequest pathRequest) {
        checkSameStation(pathRequest);
        Station sourceStation = stationDao.findById(pathRequest.getSourceStationId());
        Station targetStation = stationDao.findById(pathRequest.getTargetStationId());
        List<Line> enrolledLine = lineDao.findAll();
        Path path = new Path(enrolledLine);

        return path.optimizedPath(sourceStation, targetStation);
    }

    private void checkSameStation(PathRequest pathRequest) {
        if (pathRequest.getSourceStationId() == pathRequest.getTargetStationId()) {
            throw new SameStationException(pathRequest.getSourceStationId());
        }
    }
}
