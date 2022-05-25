package wooteco.subway.service;

import java.util.List;
import org.springframework.stereotype.Service;
import wooteco.subway.domain.fare.Age;
import wooteco.subway.domain.path.Path;
import wooteco.subway.domain.path.pathfinder.DijkstraShortestPathFinder;
import wooteco.subway.domain.section.Section;
import wooteco.subway.domain.station.Station;
import wooteco.subway.dto.response.PathResponse;
import wooteco.subway.exception.NotFoundStationException;
import wooteco.subway.repository.SectionRepository;
import wooteco.subway.repository.StationRepository;

@Service
public class PathService {

    private static final DijkstraShortestPathFinder PATH_FINDER = new DijkstraShortestPathFinder();

    private final SectionRepository sectionRepository;
    private final StationRepository stationRepository;

    public PathService(SectionRepository sectionRepository, StationRepository stationRepository) {
        this.sectionRepository = sectionRepository;
        this.stationRepository = stationRepository;
    }

    public PathResponse getPath(Long sourceStationId, Long targetStationId, Integer ageValue) {
        List<Section> allSections = sectionRepository.findAll();

        Station departure = stationRepository.findById(sourceStationId).orElseThrow(NotFoundStationException::new);
        Station arrival = stationRepository.findById(targetStationId).orElseThrow(NotFoundStationException::new);
        Age age = new Age(ageValue);

        Path path = PATH_FINDER.getPath(allSections, departure, arrival);
        System.out.println(path);
        return PathResponse.of(path, path.getFare(age));
    }
}
