package wooteco.subway.admin.service;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.admin.domain.*;
import wooteco.subway.admin.dto.response.PathResponse;
import wooteco.subway.admin.dto.response.StationResponse;
import wooteco.subway.admin.repository.LineRepository;
import wooteco.subway.admin.repository.StationRepository;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

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
        validate(sourceId, targetId);
        List<Line> lines = lineRepository.findAll();
        Map<Long, Station> stations = stationRepository.findAll()
                .stream()
                .collect(Collectors.toMap(Station::getId, station -> station));

        SubwayGraph subwayGraph = new SubwayGraph();
        WeightedMultigraph<Long, LineStationEdge> graph = subwayGraph.makeGraph(lines, stations, pathType);

        try {
            Path path = Path.of(sourceId, targetId, new DijkstraShortestPath<>(graph));
            return toResponse(stations, path);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("등록되지 않은 역이 포함되어 있습니다.");
        } catch (NullPointerException e) {
            throw new IllegalArgumentException("갈 수 없는 역입니다.");
        }
    }

    private void validate(Long sourceId, Long targetId) {
        validateEmpty(sourceId, targetId);
        validateSameIds(sourceId, targetId);
    }

    private void validateEmpty(Long sourceId, Long targetId) {
        if (Objects.isNull(sourceId) || Objects.isNull(targetId)) {
            throw new IllegalArgumentException("출발역과 도착역을 입력해주세요.");
        }
    }

    private void validateSameIds(Long sourceId, Long targetId) {
        if (Objects.equals(sourceId, targetId)) {
            throw new IllegalArgumentException("출발역과 도착역이 같습니다.");
        }
    }

    private PathResponse toResponse(Map<Long, Station> stations, Path path) {
        List<StationResponse> stationResponse = path.getVertexList().stream()
                .map(stations::get)
                .map(StationResponse::of)
                .collect(Collectors.toList());

        return new PathResponse(stationResponse, path.totalDuration(), path.totalDistance());
    }
}
