package wooteco.subway.admin.service;

import java.util.List;

import org.jgrapht.GraphPath;
import org.springframework.stereotype.Service;

import wooteco.subway.admin.domain.Line;
import wooteco.subway.admin.domain.Station;
import wooteco.subway.admin.domain.Subway;
import wooteco.subway.admin.domain.SubwayEdge;
import wooteco.subway.admin.domain.WeightType;
import wooteco.subway.admin.dto.PathResponse;
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

    public PathResponse findShortestPath(String source, String target) {
        List<Line> lines = lineRepository.findAll();
        List<Station> stations = stationRepository.findAll();

        Subway subway = new Subway(lines, stations, WeightType.DISTANCE);

        GraphPath<Station, SubwayEdge> path = subway.findShortestPath(source, target);

        Long totalDistance = calculateTotalDistance(path);
        Long totalDuration = calculateTotalDuration(path);

        return PathResponse.of(path.getVertexList(), totalDistance, totalDuration);
    }

    private long calculateTotalDistance(GraphPath<Station, SubwayEdge> path) {
        return path.getEdgeList().stream()
                .mapToLong(SubwayEdge::getDistance)
                .sum();
    }

    private Long calculateTotalDuration(GraphPath<Station, SubwayEdge> path) {
        return path.getEdgeList().stream()
                .mapToLong(SubwayEdge::getDuration)
                .sum();
    }
}
