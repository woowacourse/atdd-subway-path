package wooteco.subway.admin.service;

import org.springframework.stereotype.Service;
import wooteco.subway.admin.domain.LineStations;
import wooteco.subway.admin.domain.Lines;
import wooteco.subway.admin.domain.PathType;
import wooteco.subway.admin.domain.Stations;
import wooteco.subway.admin.dto.PathRequest;
import wooteco.subway.admin.dto.PathResponse;
import wooteco.subway.admin.exception.WrongPathException;
import wooteco.subway.admin.repository.LineRepository;
import wooteco.subway.admin.repository.StationRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PathService {
    private LineRepository lineRepository;
    private StationRepository stationRepository;
    private GraphService graphService;

    public PathService(LineRepository lineRepository, StationRepository stationRepository, GraphService graphService) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
        this.graphService = graphService;
    }

    public PathResponse calculatePath(PathRequest request) {
        validate(request);
        Stations stations = new Stations(stationRepository.findAll());
        Lines lines = new Lines(lineRepository.findAll());
        LineStations lineStations = new LineStations(lines.getLineStations());
        Long sourceId = stations.findStationIdByName(request.getSource());
        Long targetId = stations.findStationIdByName(request.getTarget());

        List<Long> shortestPath = graphService.createShortestPath(lines, sourceId, targetId, request.getType());
        return createPathResponse(shortestPath, stations, lineStations);
    }

    private void validate(PathRequest request) {
        if (request.getSource().equals(request.getTarget())) {
            throw new WrongPathException();
        }
    }

    private PathResponse createPathResponse(List<Long> path, Stations stations, LineStations lineStations) {
        Stations pathStations = path.stream()
                .map(stations::findStationById)
                .collect(Collectors.collectingAndThen(Collectors.toList(), Stations::new));
        LineStations pathLineStations = lineStations.findLineStationsByIds(path);

        int duration = pathLineStations.getWeightBy(PathType.DURATION);
        int distance = pathLineStations.getWeightBy(PathType.DISTANCE);
        return new PathResponse(pathStations, distance, duration);
    }
}
