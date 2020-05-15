package wooteco.subway.admin.service;

import java.util.List;

import org.springframework.stereotype.Service;

import wooteco.subway.admin.domain.Line;
import wooteco.subway.admin.domain.PathType;
import wooteco.subway.admin.domain.ShortestPath;
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

    public PathResponse findShortestPath(String sourceName, String targetName, PathType pathType) {
        List<Line> lines = lineRepository.findAll();
        List<Station> stations = stationRepository.findAll();

        Subway subway = new Subway(lines, stations);
        ShortestPath shortestDurationPath = subway.findShortestPath(sourceName, targetName, pathType);

        return PathResponse.of(shortestDurationPath);
    }
}
