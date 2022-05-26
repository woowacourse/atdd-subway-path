package wooteco.subway.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.domain.Line;
import wooteco.subway.domain.Station;
import wooteco.subway.domain.fare.Fare;
import wooteco.subway.domain.path.Path;
import wooteco.subway.domain.path.PathFinder;
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
        final Fare fare = new Fare(path.getDistance(), age, path.getMaxExtraFare());
        return PathResponse.of(path.getVertices(), path.getDistance(), fare.getValue());
    }

    private PathFinder createPathFinder() {
        final List<Line> lines = lineRepository.findAll();
        return new PathFinder(lines);
    }
}
