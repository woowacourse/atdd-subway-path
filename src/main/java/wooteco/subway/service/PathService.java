package wooteco.subway.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.dao.LineDao;
import wooteco.subway.domain.Line;
import wooteco.subway.domain.Path;
import wooteco.subway.domain.Station;
import wooteco.subway.domain.fare.Fare;
import wooteco.subway.dto.path.PathRequest;
import wooteco.subway.dto.path.PathResponse;
import wooteco.subway.infra.path.PathFinder;

import java.util.Collections;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class PathService {
    private final LineDao lineDao;
    private final LineService lineService;
    private final PathFinder pathFinder;

    public PathService(LineDao lineDao, LineService lineService, PathFinder pathFinder) {
        this.lineDao = lineDao;
        this.lineService = lineService;
        this.pathFinder = pathFinder;
    }

    public PathResponse findShortestPath(PathRequest pathRequest) {
        Long sourceStationId = pathRequest.getSource();
        Long targetStationId = pathRequest.getTarget();
        int age = pathRequest.getAge();

        validateSameStation(sourceStationId, targetStationId);

        List<Line> lines = lineDao.findAll();
        Station source = lineService.findStationById(sourceStationId);
        Station target = lineService.findStationById(targetStationId);

        Path path = pathFinder.findShortestPath(source, target, lines);
        Integer maxAdditionalLineFare = findLineFare(path);

        return PathResponse.of(path, new Fare(path.getDistance(), maxAdditionalLineFare, age));
    }

    private Integer findLineFare(Path path) {
        List<Long> shortestPathLineIds = path.findShortestPathLines();
        List<Integer> extraFares = lineDao.findExtraFareByIds(shortestPathLineIds);
        return Collections.max(extraFares);
    }

    private void validateSameStation(Long sourceStationId, Long targetStationId) {
        if (sourceStationId.equals(targetStationId)) {
            throw new IllegalArgumentException("출발역과 도착역이 동일합니다.");
        }
    }
}
