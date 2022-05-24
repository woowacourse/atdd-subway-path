package wooteco.subway.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.dao.LineDao;
import wooteco.subway.domain.Line;
import wooteco.subway.domain.Station;
import wooteco.subway.domain.fare.Fare;
import wooteco.subway.domain.path.Path;
import wooteco.subway.domain.path.PathFinder;
import wooteco.subway.dto.path.PathRequest;
import wooteco.subway.dto.path.PathResponse;

import java.util.Comparator;
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
        List<Long> shortestPathLines = path.findShortestPathLines();
        return shortestPathLines.stream()
                .map(lineDao::findExtraFareById)
                .max(Comparator.comparingInt(extraFare -> extraFare))
                .orElseThrow(() -> new IllegalArgumentException("추가 요금이 존재하지 않습니다. 라인을 다시 추가해주세요."));
    }

    private void validateSameStation(Long sourceStationId, Long targetStationId) {
        if (sourceStationId.equals(targetStationId)) {
            throw new IllegalArgumentException("출발역과 도착역이 동일합니다.");
        }
    }
}
