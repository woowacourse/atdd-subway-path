package wooteco.subway.admin.service;

import org.springframework.stereotype.Service;
import wooteco.subway.admin.domain.*;
import wooteco.subway.admin.dto.PathResponse;
import wooteco.subway.admin.dto.StationResponse;
import wooteco.subway.admin.repository.LineRepository;
import wooteco.subway.admin.repository.StationRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class PathService {
    private final LineRepository lineRepository;
    private final StationRepository stationRepository;
    private final GraphService graphService;

    public PathService(LineRepository lineRepository, StationRepository stationRepository, GraphService graphService) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
        this.graphService = graphService;
    }

    public PathResponse calculatePath(String source, String target, PathType type) {
        Lines allLines = new Lines(lineRepository.findAll());
        LineStations lineStations = new LineStations(allLines.getAllLineStation());
        Stations stations = new Stations(stationRepository.findAll());
        Station sourceStation = stations.findStationByName(source);
        Station targetStation = stations.findStationByName(target);

        List<Long> shortestPathIds = graphService.calculateShortestPathIDs(lineStations, sourceStation, targetStation, type);

        List<Station> shortestPathStations = shortestPathIds.stream()
                .map(stations::findStationById)
                .collect(Collectors.toList());

        List<LineStation> shortestPathLineStations = calculateShortestPathLineStations(lineStations, sourceStation.getId(), shortestPathIds);

        int distance = getAccumulateValue(shortestPathLineStations, LineStation::getDistance);
        int duration = getAccumulateValue(shortestPathLineStations, LineStation::getDuration);

        return new PathResponse(StationResponse.listOf(shortestPathStations), distance, duration);
    }

    private List<LineStation> calculateShortestPathLineStations(LineStations lineStations, Long sourceStationId, List<Long> shortestPathIds) {
        Long preStationId = sourceStationId;

        List<LineStation> shortestPathLineStations = new ArrayList<>();
        for (Long stationId : shortestPathIds) {
            shortestPathLineStations.add(lineStations.findLineStation(preStationId, stationId));
            preStationId = stationId;
        }
        return shortestPathLineStations;
    }

    private Integer getAccumulateValue(List<LineStation> shortestPathLineStations, Function<LineStation, Integer> strategy) {
        return shortestPathLineStations.stream()
                .map(strategy)
                .reduce(Integer::sum)
                .orElse(0);
    }
}
