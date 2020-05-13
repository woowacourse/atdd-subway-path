package wooteco.subway.admin.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.jgrapht.GraphPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.springframework.stereotype.Service;

import wooteco.subway.admin.domain.Line;
import wooteco.subway.admin.domain.LineStation;
import wooteco.subway.admin.domain.Station;
import wooteco.subway.admin.domain.Subway;
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

        Subway subway = new Subway(lines, stations);

        GraphPath<Station, DefaultWeightedEdge> path = subway.findShortestPath(source, target);

        Long totalDistance = Double.valueOf(path.getWeight()).longValue();
        Long totalDuration = calculateTotalDuration(lines, path);

        return PathResponse.of(path.getVertexList(), totalDistance, totalDuration);
    }

    private Long calculateTotalDuration(List<Line> lines, GraphPath<Station, DefaultWeightedEdge> path) {
        Map<Long, LineStation> lineStationMapper = generateLineStationMapper(lines);
        return path.getVertexList()
                .stream()
                .skip(1)
                .mapToLong(station -> lineStationMapper.get(station.getId()).getDuration())
                .sum();
    }

    private Map<Long, LineStation> generateLineStationMapper(List<Line> lines) {
        return lines.stream()
                .flatMap(line -> line.getStations().stream())
                .collect(Collectors.toMap(
                        LineStation::getStationId,
                        lineStation -> lineStation));
    }
}
