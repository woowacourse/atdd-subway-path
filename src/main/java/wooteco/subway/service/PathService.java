package wooteco.subway.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.domain.Path;
import wooteco.subway.domain.PathFinder;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Station;
import wooteco.subway.dto.request.FindPathRequest;
import wooteco.subway.dto.response.PathResponse;
import wooteco.subway.repository.SectionRepository;
import wooteco.subway.repository.StationRepository;

@Service
public class PathService {

    private final StationRepository stationRepository;
    private final SectionRepository sectionRepository;

    public PathService(final StationRepository stationRepository, final SectionRepository sectionRepository) {
        this.stationRepository = stationRepository;
        this.sectionRepository = sectionRepository;
    }

    @Transactional(readOnly = true)
    public PathResponse findPath(final FindPathRequest findPathRequest) {
        final PathFinder pathFinder = createPathFinder();
        final Station source = stationRepository.getById(findPathRequest.getSource());
        final Station target = stationRepository.getById(findPathRequest.getTarget());
        final Path path = pathFinder.find(source, target);
        return PathResponse.of(path.getRouteStations(), path.getDistance(), path.calculateFare());
    }

    private PathFinder createPathFinder() {
        final List<Station> stations = stationRepository.findAll();
        final List<Section> sections = sectionRepository.findAll();
        return new PathFinder(stations, sections);
    }
}
