package wooteco.subway.service;

import java.util.List;
import org.springframework.stereotype.Service;
import wooteco.subway.domain.Fare;
import wooteco.subway.domain.Line;
import wooteco.subway.domain.PathCalculator;
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

    public PathResponse findShortestPath(final Long sourceId, final Long targetId) {
        final Station source = stationRepository.findById(sourceId);
        final Station target = stationRepository.findById(targetId);
        final List<Line> lines = lineRepository.findAll();

        final PathCalculator pathCalculator = new PathCalculator(lines);
        final List<Station> stations = pathCalculator.findShortestPath(source, target);
        final int distance = pathCalculator.findShortestDistance(source, target);
        final int fare = (new Fare()).calculate(distance);

        return new PathResponse(stations, distance, fare);
    }
}
