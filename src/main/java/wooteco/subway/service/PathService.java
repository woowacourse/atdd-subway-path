package wooteco.subway.service;

import java.util.List;
import java.util.function.Supplier;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import wooteco.subway.dao.LineDao;
import wooteco.subway.dao.StationDao;
import wooteco.subway.domain.Line;
import wooteco.subway.domain.Path;
import wooteco.subway.domain.Station;
import wooteco.subway.domain.SubwayMap;
import wooteco.subway.dto.PathRequest;
import wooteco.subway.dto.PathResponse;
import wooteco.subway.exception.EmptyResultException;

@Service
@Transactional(readOnly = true)
public class PathService {
    private final LineDao lineDao;
    private final StationDao stationDao;

    public PathService(LineDao lineDao, StationDao stationDao) {
        this.lineDao = lineDao;
        this.stationDao = stationDao;
    }

    public PathResponse findShortestPath(PathRequest pathRequest) {
        Long sourceStationId = pathRequest.getSource();
        Long targetStationId = pathRequest.getTarget();

        validateSameStation(sourceStationId, targetStationId);

        List<Line> lines = lineDao.findAll();
        SubwayMap subwayMap = SubwayMap.of(lines);
        Station source = findStationById(sourceStationId);
        Station target = findStationById(targetStationId);

        Path path = subwayMap.findShortestPath(source, target);
        return PathResponse.of(path);
    }

    private void validateSameStation(Long sourceStationId, Long targetStationId) {
        if (sourceStationId.equals(targetStationId)) {
            throw new IllegalArgumentException("출발역과 도착역이 동일합니다.");
        }
    }

    private Station findStationById(Long id) {
        return stationDao.findById(id)
            .orElseThrow((throwEmptyStationException(id)));
    }

    private Supplier<EmptyResultException> throwEmptyStationException(Long id) {
        return () -> new EmptyResultException(String.format("아이디 값이 %s인 역을 찾을 수 없습니다.", id));
    }
}
