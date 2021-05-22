package wooteco.subway.application;

import org.springframework.stereotype.Service;
import wooteco.subway.domain.line.Line;
import wooteco.subway.domain.path.Path;
import wooteco.subway.domain.path.PathFinder;
import wooteco.subway.domain.station.Station;
import wooteco.subway.exception.BadRequestException;
import wooteco.subway.presentation.dto.response.PathResponse;
import wooteco.subway.repository.LineDao;
import wooteco.subway.repository.StationDao;

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
