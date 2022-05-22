package wooteco.subway.service;

import java.util.List;

import org.springframework.stereotype.Service;
import wooteco.subway.domain.line.Line;
import wooteco.subway.domain.line.LineSeries;
import wooteco.subway.domain.path.Path;
import wooteco.subway.domain.property.Age;
import wooteco.subway.domain.property.Distance;
import wooteco.subway.repository.LineRepository;
import wooteco.subway.util.FareCalculator;
import wooteco.subway.domain.station.Station;
import wooteco.subway.dto.response.PathResponse;
import wooteco.subway.repository.StationRepository;

@Service
public class PathService {

    private final StationRepository stationRepository;
    private final LineRepository lineRepository;

    public PathService(StationRepository stationRepository, LineRepository lineRepository) {
        this.stationRepository = stationRepository;
        this.lineRepository = lineRepository;
    }

    public PathResponse findPath(Long source, Long target, Integer age) {
        List<Line> lines = lineRepository.findAllLines();

        Path finder = Path.of(lines,
                stationRepository.findById(source),
                stationRepository.findById(target));

        final List<Station> paths = finder.findShortestPath();
        final Distance distance = new Distance(finder.findDistance());
        final int fare = calculateFare(age, lines, finder, distance);

        return PathResponse.of(paths, distance, fare);
    }

    private int calculateFare(Integer age, List<Line> lines, Path path, Distance distance) {
        final LineSeries lineSeries = new LineSeries(lines);
        final int extraFare = lineSeries.findMaxExtraFare(path.findUsedLineId());

        return FareCalculator.calculate(distance, new Age(age), extraFare);
    }
}
