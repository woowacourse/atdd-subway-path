package wooteco.subway.admin.service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import wooteco.subway.admin.domain.Lines;
import wooteco.subway.admin.domain.Station;
import wooteco.subway.admin.domain.path.GraphStrategy;
import wooteco.subway.admin.domain.path.Path;
import wooteco.subway.admin.domain.path.PathType;
import wooteco.subway.admin.dto.response.PathResponse;
import wooteco.subway.admin.dto.response.StationResponse;
import wooteco.subway.admin.exception.DuplicatedValueException;
import wooteco.subway.admin.exception.UnreachablePathException;
import wooteco.subway.admin.exception.ValueRequiredException;
import wooteco.subway.admin.repository.LineRepository;
import wooteco.subway.admin.repository.StationRepository;

@Service
public class PathService {
    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    public PathService(LineRepository lineRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    @Transactional(readOnly = true)
    public PathResponse findPath(Long sourceId, Long targetId, String pathType,
        GraphStrategy graphStrategy) {
        validatePathIds(sourceId, targetId);
        Lines lines = Lines.of(lineRepository.findAll());

        try {
            Path path = lines.findPath(graphStrategy, sourceId, targetId, PathType.of(pathType));
            // Path path = Path.of(sourceId, targetId, graph, pathStrategy);
            return toResponse(path);
        } catch (Exception e) {
            throw new UnreachablePathException("갈 수 없는 역입니다.");
        }
    }

    private void validatePathIds(Long sourceId, Long targetId) {
        if (Objects.isNull(sourceId) || Objects.isNull(targetId)) {
            throw new ValueRequiredException("출발역과 도착역을 모두 입력해주세요.");
        }
        if (sourceId.equals(targetId)) {
            throw new DuplicatedValueException("출발역과 도착역은 같을 수 없습니다.");
        }
    }

    private PathResponse toResponse(Path path) {
        Map<Long, Station> stations = stationRepository.findAll()
            .stream()
            .collect(Collectors.toMap(Station::getId, station -> station));
        List<StationResponse> stationResponse = path.getVertexList().stream()
            .map(stations::get)
            .map(StationResponse::of)
            .collect(Collectors.toList());

        return new PathResponse(stationResponse, path.totalDuration(), path.totalDistance());
    }
}
