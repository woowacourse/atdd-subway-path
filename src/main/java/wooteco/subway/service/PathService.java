package wooteco.subway.service;

import java.util.List;
import org.springframework.stereotype.Service;
import wooteco.subway.domain.fare.Age;
import wooteco.subway.domain.line.Line;
import wooteco.subway.domain.path.Path;
import wooteco.subway.domain.path.pathfinder.DijkstraShortestPathFinder;
import wooteco.subway.domain.section.Section;
import wooteco.subway.domain.station.Station;
import wooteco.subway.dto.request.PathRequest;
import wooteco.subway.dto.response.PathResponse;
import wooteco.subway.exception.NotFoundStationException;
import wooteco.subway.repository.LineRepository;
import wooteco.subway.repository.SectionRepository;
import wooteco.subway.repository.StationRepository;

@Service
public class PathService {

    private static final DijkstraShortestPathFinder PATH_FINDER = new DijkstraShortestPathFinder();

    private final SectionRepository sectionRepository;
    private final StationRepository stationRepository;
    private final LineRepository lineRepository;

    public PathService(SectionRepository sectionRepository, StationRepository stationRepository,
                       LineRepository lineRepository) {
        this.sectionRepository = sectionRepository;
        this.stationRepository = stationRepository;
        this.lineRepository = lineRepository;
    }

    public PathResponse getPath(PathRequest pathRequest) {
        List<Section> allSections = sectionRepository.findAll();
        List<Line> allLines = lineRepository.findAll();

        Station source = stationRepository.findById(pathRequest.getSource()).orElseThrow(NotFoundStationException::new);
        Station target = stationRepository.findById(pathRequest.getTarget()).orElseThrow(NotFoundStationException::new);
        Age age = new Age(pathRequest.getAge());

        Path path = PATH_FINDER.getPath(allSections, allLines, source, target);
        return PathResponse.of(path, path.getFare(age));
    }
}
