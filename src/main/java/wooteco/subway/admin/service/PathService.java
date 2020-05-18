package wooteco.subway.admin.service;

import org.springframework.stereotype.Service;
import wooteco.subway.admin.domain.Edges;
import wooteco.subway.admin.domain.Lines;
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
    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    public PathService(LineRepository lineRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    public PathResponse calculatePath(PathRequest request) {
        Lines wholeLines = new Lines(lineRepository.findAll());
        Stations wholeStations = new Stations(stationRepository.findAll());
        Edges wholeEdges = new Edges(wholeLines.findWholeEdges());
        Long sourceStationId = wholeStations.findIdByName(request.getSource());
        Long targetStationId = wholeStations.findIdByName(request.getTarget());

        if (sourceStationId.equals(targetStationId)) {
            throw new WrongPathException();
        }

        List<Long> StationIdsInShortestPath
                = wholeLines.createShortestPath(sourceStationId, targetStationId, request.getType());

        Stations stationsInShortestPath = StationIdsInShortestPath.stream()
                .map(wholeStations::findStationById)
                .collect(Collectors.collectingAndThen(Collectors.toList(), Stations::new));

        Edges edgesInShortestPath = wholeEdges.extractPathEdges(StationIdsInShortestPath);
        return new PathResponse(
                stationsInShortestPath.getStations(),
                edgesInShortestPath.getDistance(),
                edgesInShortestPath.getDuration()
        );
    }
}
