package wooteco.subway.application;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.jgrapht.GraphPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.springframework.stereotype.Service;
import wooteco.subway.application.exception.NotFoundStationException;
import wooteco.subway.application.exception.UnsearchablePathException;
import wooteco.subway.domain.PathFinder;
import wooteco.subway.domain.PathFinder.Path;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Station;
import wooteco.subway.dto.PathResponse;
import wooteco.subway.dto.StationResponse;
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
        if (source.equals(target)) {
            throw new UnsearchablePathException();
        }

        if (!stationRepository.existById(source)) {
            throw new NotFoundStationException(source);
        }

        if (!stationRepository.existById(target)) {
            throw new NotFoundStationException(source);
        }

        List<Station> stations = stationRepository.findAll();
        List<Section> sections = sectionRepository.findAll();

        Path path = PathFinder.find(stations, sections, source, target);
        List<StationResponse> responses = path.getPath().stream()
            .map(StationResponse::new)
            .collect(Collectors.toList());

        return new PathResponse(responses, path.getDistance(), path.getFare());
    }
}
