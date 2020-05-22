package wooteco.subway.admin.service;

import org.springframework.stereotype.Service;

import wooteco.subway.admin.domain.Lines;
import wooteco.subway.admin.domain.ShortestPathStrategy;
import wooteco.subway.admin.domain.Stations;
import wooteco.subway.admin.domain.SubwayPath;
import wooteco.subway.admin.dto.FindPathRequest;
import wooteco.subway.admin.dto.PathResponse;
import wooteco.subway.admin.repository.LineRepository;
import wooteco.subway.admin.repository.StationRepository;

@Service
public class PathService {
    private final LineRepository lineRepository;
    private final StationRepository stationRepository;
    private final ShortestPathStrategy shortestPathStrategy;

    public PathService(LineRepository lineRepository, StationRepository stationRepository,
            ShortestPathStrategy shortestPathStrategy) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
        this.shortestPathStrategy = shortestPathStrategy;
    }

    public PathResponse findShortestPath(FindPathRequest findPathRequest) {
        Lines lines = new Lines(lineRepository.findAll());
        Stations stations = new Stations(stationRepository.findAll());

        SubwayPath shortestPath = shortestPathStrategy.findShortestPath(lines, stations, findPathRequest);
        return PathResponse.of(shortestPath);
    }
}
