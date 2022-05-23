package wooteco.subway.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.domain.Line;
import wooteco.subway.domain.Path;
import wooteco.subway.domain.PathFinder;
import wooteco.subway.domain.Station;
import wooteco.subway.dto.response.PathResponse;
import wooteco.subway.repository.LineRepository;
import wooteco.subway.repository.StationRepository;

@Service
public class PathService {

    private final StationRepository stationRepository;
    private final LineRepository lineRepository;

    public PathService(final StationRepository stationRepository, final LineRepository lineRepository) {
        this.stationRepository = stationRepository;
        this.lineRepository = lineRepository;
    }

    @Transactional(readOnly = true)
    public PathResponse find(final Long sourceId, final Long targetId, final int age) {
        final PathFinder pathFinder = createPathFinder();
        final Station source = stationRepository.findById(sourceId);
        final Station target = stationRepository.findById(targetId);
        final Path path = pathFinder.find(source, target);
        return PathResponse.of(path.getRouteStations(), path.getDistance(), path.calculateFare(age));
    }

    private PathFinder createPathFinder() {
        final List<Line> lines = lineRepository.findAll();
        return new PathFinder(lines);
    }
}
