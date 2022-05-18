package wooteco.subway.service;

import java.util.List;

import org.springframework.stereotype.Repository;

import wooteco.subway.domain.path.PathFinder;
import wooteco.subway.domain.property.Distance;
import wooteco.subway.domain.property.Fare;
import wooteco.subway.domain.section.Section;
import wooteco.subway.domain.station.Station;
import wooteco.subway.dto.response.PathResponse;
import wooteco.subway.repository.LineRepository;
import wooteco.subway.repository.SectionRepository;
import wooteco.subway.repository.StationRepository;

@Repository
public class PathService {

    private final StationRepository stationRepository;
    private final SectionRepository sectionRepository;

    public PathService(StationRepository stationRepository, SectionRepository sectionRepository) {
        this.stationRepository = stationRepository;
        this.sectionRepository = sectionRepository;
    }

    public PathResponse findPath(Long source, Long target, Long age) {
        PathFinder finder = PathFinder.of(sectionRepository.findAllSections(),
                stationRepository.findById(source),
                stationRepository.findById(target));

        final List<Station> paths = finder.findShortestPath();
        final Distance distance = new Distance(finder.findDistance());
        final Fare fare = new Fare(distance.getValue());

        return PathResponse.of(paths, distance, fare);
    }
}
