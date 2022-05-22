package wooteco.subway.application.path;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.application.exception.NotFoundStationException;
import wooteco.subway.application.exception.RidiculousAgeException;
import wooteco.subway.application.exception.UnreachablePathException;
import wooteco.subway.domain.Line;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Station;
import wooteco.subway.domain.path.FareCalculator;
import wooteco.subway.domain.path.Graph;
import wooteco.subway.dto.PathResponse;
import wooteco.subway.repository.LineRepository;
import wooteco.subway.repository.SectionRepository;
import wooteco.subway.repository.StationRepository;

@Service
public class PathService {

    private final StationRepository stationRepository;
    private final SectionRepository sectionRepository;
    private final LineRepository lineRepository;

    public PathService(StationRepository stationRepository, SectionRepository sectionRepository,
                       LineRepository lineRepository) {
        this.stationRepository = stationRepository;
        this.sectionRepository = sectionRepository;
        this.lineRepository = lineRepository;
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

        //TODO: 메서드 분리 및 Graph 추상화 함수 생각
        List<Long> lineIds = graph.findLineIdsRelatedPath(source, target);
        List<Line> relatedLines = lineIds.stream()
                .map(lineId -> lineRepository.findById(lineId)
                        .orElseThrow(() -> new IllegalArgumentException("말도 안되는 노선 아이디 입니다.")))
                .collect(Collectors.toUnmodifiableList());

        int fare = fareCalculator.findFare(distance, relatedLines);
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
