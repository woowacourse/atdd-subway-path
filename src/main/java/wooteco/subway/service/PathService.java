package wooteco.subway.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.domain.Line;
import wooteco.subway.domain.Path;
import wooteco.subway.domain.pathfinder.PathFinder;
import wooteco.subway.domain.Station;
import wooteco.subway.dto.PathRequest;
import wooteco.subway.dto.PathResponse;
import wooteco.subway.repository.LineRepository;
import wooteco.subway.repository.StationRepository;

@Service
@Transactional(readOnly = true)
public class PathService {

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;
    private final PathFinder pathFinder;

    public PathService(LineRepository lineRepository, StationRepository stationRepository, PathFinder pathFinder) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
        this.pathFinder = pathFinder;
    }

    public PathResponse findPath(PathRequest pathRequest) {
        List<Line> lines = lineRepository.findAll();
        Station source = stationRepository.findById(pathRequest.getSource());
        Station target = stationRepository.findById(pathRequest.getTarget());
        Path path = pathFinder.findShortest(lines, source, target);
        return new PathResponse(path.getStations(), path.getDistance(), path.finalFare(pathRequest.getAge()));
    }
}
