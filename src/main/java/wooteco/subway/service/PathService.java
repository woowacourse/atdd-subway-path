package wooteco.subway.service;

import java.util.List;

import org.springframework.stereotype.Repository;

import wooteco.subway.domain.line.LineSeries;
import wooteco.subway.domain.path.PathFinder;
import wooteco.subway.domain.property.Distance;
import wooteco.subway.domain.property.Fare;
import wooteco.subway.domain.station.Station;
import wooteco.subway.dto.response.PathResponse;
import wooteco.subway.repository.LineRepository;
import wooteco.subway.repository.StationRepository;

@Repository
public class PathService {

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    public PathService(LineRepository lineRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    public PathResponse findPath(Long source, Long target, Long ignored) {
        LineSeries lineSeries = new LineSeries(lineRepository.findAllLines());
        PathFinder finder = PathFinder.from(lineSeries);

        final Station sourceStation = stationRepository.findById(source);
        final Station targetStation = stationRepository.findById(target);

        final List<Station> paths = finder.findShortestPath(sourceStation, targetStation);
        final Distance distance = new Distance(finder.getDistance(sourceStation, targetStation));

        final Fare calculatedFare = Fare.from(distance);
        return PathResponse.of(paths, distance, calculatedFare);
    }
}
