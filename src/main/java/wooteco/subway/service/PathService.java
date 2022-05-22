package wooteco.subway.service;

import java.util.Comparator;
import java.util.List;
import java.util.function.Supplier;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import wooteco.subway.dao.LineDao;
import wooteco.subway.dao.StationDao;
import wooteco.subway.domain.*;
import wooteco.subway.domain.fare.Fare;
import wooteco.subway.domain.fare.FareCalculator;
import wooteco.subway.dto.PathRequest;
import wooteco.subway.dto.PathResponse;
import wooteco.subway.exception.EmptyResultException;

@Service
@Transactional(readOnly = true)
public class PathService {
    private final LineDao lineDao;
    private final LineService lineService;

    public PathService(LineDao lineDao, LineService lineService) {
        this.lineDao = lineDao;
        this.lineService = lineService;
    }

    public PathResponse findShortestPath(PathRequest pathRequest) {
        Long sourceStationId = pathRequest.getSource();
        Long targetStationId = pathRequest.getTarget();
        int age = pathRequest.getAge();

        validateSameStation(sourceStationId, targetStationId);

        List<Line> lines = lineDao.findAll();
        SubwayMap subwayMap = SubwayMap.of(lines);
        Station source = lineService.findStationById(sourceStationId);
        Station target = lineService.findStationById(targetStationId);

        Path path = subwayMap.findShortestPath(source, target);
        Integer maxAdditionalLineFare = findLineFare(path);

        return PathResponse.of(path, new Fare(path.getDistance(), maxAdditionalLineFare, age));
    }

    private Integer findLineFare(Path path) {
        List<Long> shortestPathLines = path.findShortestPathLines();
        return shortestPathLines.stream()
                .map(lineService::findLineById)
                .map(Line::getExtraFare)
                .max(Comparator.comparingInt(extraFare -> extraFare))
                .orElseThrow(() -> new IllegalArgumentException("잘못된 요금입니다."));
    }

    private void validateSameStation(Long sourceStationId, Long targetStationId) {
        if (sourceStationId.equals(targetStationId)) {
            throw new IllegalArgumentException("출발역과 도착역이 동일합니다.");
        }
    }

    private Supplier<EmptyResultException> throwEmptyStationException(Long id) {
        return () -> new EmptyResultException(String.format("아이디 값이 %s인 역을 찾을 수 없습니다.", id));
    }
}
