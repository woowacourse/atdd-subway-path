package wooteco.subway.application;

import java.util.List;
import org.springframework.stereotype.Service;
import wooteco.subway.application.exception.NotFoundStationException;
import wooteco.subway.application.exception.UnreachablePathException;
import wooteco.subway.domain.FareCalculator;
import wooteco.subway.domain.Graph;
import wooteco.subway.domain.Path;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Station;
import wooteco.subway.dto.PathResponse;
import wooteco.subway.repository.SectionRepository;
import wooteco.subway.repository.StationRepository;

@Service
public class PathService {

    private final StationRepository stationRepository;
    private final SectionRepository sectionRepository;

    public PathService(StationRepository stationRepository,
                       SectionRepository sectionRepository) {
        this.stationRepository = stationRepository;
        this.sectionRepository = sectionRepository;
    }

    public PathResponse searchPath(Long source, Long target) {
        validate(source, target);
        Path path = searchPath(createGraph(), source, target);
        int fare = new FareCalculator().calculateFare(path.getDistance());
        return new PathResponse(path, fare);
    }

    private void validate(Long source, Long target) {
        if (source.equals(target)) {
            throw new UnreachablePathException(source, target);
        }
        if (!stationRepository.existById(source)) {
            throw new NotFoundStationException(source);
        }
        if (!stationRepository.existById(target)) {
            throw new NotFoundStationException(source);
        }
    }

    private Graph createGraph() {
        List<Station> stations = stationRepository.findAll();
        List<Section> sections = sectionRepository.findAll();
        return new JGraphtAdapter(stations, sections);
    }

    private Path searchPath(Graph graph, Long source, Long target) {
        Path path = graph.search(source, target);
        if (path.isEmpty()) {
            throw new UnreachablePathException(source, target);
        }
        return path;
    }
}
