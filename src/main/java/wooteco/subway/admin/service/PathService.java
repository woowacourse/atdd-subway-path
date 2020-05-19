package wooteco.subway.admin.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import wooteco.subway.admin.domain.DijkstraShortestPathStrategy;
import wooteco.subway.admin.domain.LineStationEdge;
import wooteco.subway.admin.domain.Lines;
import wooteco.subway.admin.domain.Path;
import wooteco.subway.admin.domain.PathType;
import wooteco.subway.admin.domain.Station;
import wooteco.subway.admin.dto.response.PathResponse;
import wooteco.subway.admin.dto.response.StationResponse;
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
    public PathResponse findPath(Long sourceId, Long targetId, String pathType) {
        Lines lines = Lines.of(lineRepository.findAll());
        WeightedMultigraph<Long, LineStationEdge> graph = lines.makeGraph(PathType.of(pathType));

        try {
            Path path = Path.of(sourceId, targetId, graph, new DijkstraShortestPathStrategy());
            return toResponse(path);
        } catch (NullPointerException e) {
            throw new IllegalArgumentException("갈 수 없는 역입니다.");
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
