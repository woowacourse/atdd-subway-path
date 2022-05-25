package wooteco.subway.service;

import org.springframework.stereotype.Service;

import wooteco.subway.domain.line.LineSeries;
import wooteco.subway.domain.path.JgraphtPathFinder;
import wooteco.subway.domain.path.Path;
import wooteco.subway.domain.path.PathFinder;
import wooteco.subway.domain.property.Age;
import wooteco.subway.domain.property.fare.Fare;
import wooteco.subway.domain.property.fare.FarePolicies;
import wooteco.subway.domain.station.Station;
import wooteco.subway.dto.request.PathRequest;
import wooteco.subway.dto.response.PathResponse;
import wooteco.subway.repository.LineRepository;
import wooteco.subway.repository.StationRepository;

@Service
public class PathService {

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    public PathService(LineRepository lineRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    public PathResponse findPath(PathRequest pathRequest) {
        LineSeries lineSeries = new LineSeries(lineRepository.findAllLines());
        final Station sourceStation = stationRepository.findById(pathRequest.getSource());
        final Station targetStation = stationRepository.findById(pathRequest.getTarget());

        final PathFinder pathFinder = JgraphtPathFinder.of(lineSeries, sourceStation, targetStation);
        final Path path = Path.of(pathFinder);
        final Fare fare = Fare.calculateFrom(FarePolicies.of(pathFinder, new Age(pathRequest.getAge())));
        return PathResponse.of(path, fare);
    }
}
