package wooteco.subway.admin.service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import wooteco.subway.admin.domain.Graph;
import wooteco.subway.admin.domain.Line;
import wooteco.subway.admin.domain.LineStationEdge;
import wooteco.subway.admin.domain.Path;
import wooteco.subway.admin.domain.PathType;
import wooteco.subway.admin.domain.Station;
import wooteco.subway.admin.dto.response.PathResponse;
import wooteco.subway.admin.exception.InvalidPathException;
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
    public PathResponse findPath(Long sourceId, Long targetId, PathType pathType) {
        try {
            validate(sourceId, targetId);
            Graph graph = new Graph(new WeightedMultigraph<>(LineStationEdge.class));
            List<Line> lines = lineRepository.findAll();
            List<Station> stations = stationRepository.findAll();
            graph.addVertex(stations);
            graph.readyToEdge(lines, pathType);
            Path path = graph.createPath(sourceId, targetId);
            Map<Long, Station> idToStation = stations.stream()
                .collect(Collectors.toMap(Station::getId, Function.identity()));
            return path.createPathResponse(idToStation);
        } catch (IllegalArgumentException e) {
            throw new InvalidPathException("등록되지 않은 역이 포함되어 있습니다.");
        } catch (NullPointerException e) {
            throw new InvalidPathException("갈 수 없는 역입니다.");
        }
    }

    private void validate(Long sourceId, Long targetId) {
        validateEmpty(sourceId, targetId);
        validateSameIds(sourceId, targetId);
    }

    private void validateEmpty(Long sourceId, Long targetId) {
        if (Objects.isNull(sourceId) && Objects.isNull(targetId)) {
            throw new InvalidPathException("출발역과 도착역이 비어있습니다.");
        }
        if (Objects.isNull(sourceId)) {
            throw new InvalidPathException("출발역이 비어있습니다.");
        }
        if (Objects.isNull(targetId)) {
            throw new InvalidPathException("도착역이 비어있습니다.");
        }
    }

    private void validateSameIds(Long sourceId, Long targetId) {
        if (Objects.equals(sourceId, targetId)) {
            throw new InvalidPathException("출발역과 도착역이 같습니다.");
        }
    }


}
