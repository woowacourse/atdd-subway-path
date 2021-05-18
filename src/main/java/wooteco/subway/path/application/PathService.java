package wooteco.subway.path.application;

import org.springframework.stereotype.Service;
import wooteco.subway.common.exception.BadRequestException;
import wooteco.subway.line.dao.LineDao;
import wooteco.subway.line.domain.Line;
import wooteco.subway.path.domain.Path;
import wooteco.subway.path.dto.PathResponse;
import wooteco.subway.path.infrastructure.PathFinder;
import wooteco.subway.station.dao.StationDao;
import wooteco.subway.station.domain.Station;

import java.util.List;

@Service
public class PathService {

    private final LineDao lineDao;
    private final StationDao stationDao;

    public PathService(LineDao lineDao, StationDao stationDao) {
        this.lineDao = lineDao;
        this.stationDao = stationDao;
    }

    public PathResponse shortestPath(Long source, Long target) {
        List<Line> lines = lineDao.findAll();
        Station sourceStation = findStation(source);
        Station targetStation = findStation(target);

        Path path = new PathFinder(lines).shortestPath(sourceStation, targetStation);
        return new PathResponse(path);
    }

    private Station findStation(Long id) {
        return stationDao.findById(id)
                .orElseThrow(() -> new BadRequestException("해당하는 역이 존재하지 않습니다."));
    }
}
