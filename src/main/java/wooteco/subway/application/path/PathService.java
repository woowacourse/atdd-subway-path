package wooteco.subway.application.path;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.application.exception.NotFoundStationException;
import wooteco.subway.application.exception.RidiculousAgeException;
import wooteco.subway.application.exception.UnreachablePathException;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Station;
import wooteco.subway.domain.path.FareCalculator;
import wooteco.subway.domain.path.Graph;
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

    @Transactional(readOnly = true)
    public PathResponse searchPath(Long source, Long target, Integer age) {

        //TODO: 도메인으로 옮기기
        if (age <= 0 || age > 150) {
            throw new RidiculousAgeException();
        }

        validate(source, target);

        List<Station> stations = stationRepository.findAll();
        List<Section> sections = sectionRepository.findAll();

        Graph graph = new JGraphtAdapter(stations, sections);
        FareCalculator fareCalculator = new FareCalculator();

        List<Station> path = graph.findPath(source, target);
        if (path.isEmpty()) {
            throw new UnreachablePathException(source, target);
        }

        int distance = graph.findDistance(source, target);
        int fare = fareCalculator.findFare(distance);
        return new PathResponse(path, distance, fare);
    }

    private void validate(Long source, Long target) {
        if (source.equals(target)) {
            throw new UnreachablePathException(source, target);
        }

        if (!stationRepository.existById(source)) {
            throw new NotFoundStationException(source);
        }

        if (!stationRepository.existById(target)) {
            throw new NotFoundStationException(target);
        }
    }
}
