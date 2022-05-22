package wooteco.subway.service;

import java.util.List;

import org.springframework.stereotype.Service;
import wooteco.subway.domain.path.PathFinder;
import wooteco.subway.domain.property.Age;
import wooteco.subway.domain.property.Distance;
import wooteco.subway.util.FareCalculator;
import wooteco.subway.domain.station.Station;
import wooteco.subway.dto.response.PathResponse;
import wooteco.subway.repository.SectionRepository;
import wooteco.subway.repository.StationRepository;

@Service
public class PathService {

    private final StationRepository stationRepository;
    private final SectionRepository sectionRepository;

    public PathService(StationRepository stationRepository, SectionRepository sectionRepository) {
        this.stationRepository = stationRepository;
        this.sectionRepository = sectionRepository;
    }

    public PathResponse findPath(Long source, Long target, Integer age) {
        PathFinder finder = PathFinder.of(sectionRepository.findAllSections(),
                stationRepository.findById(source),
                stationRepository.findById(target));

        final List<Station> paths = finder.findShortestPath();
        final Distance distance = new Distance(finder.findDistance());
        final int fare = FareCalculator.calculate(distance, new Age(age));

        return PathResponse.of(paths, distance, fare);
    }
}
