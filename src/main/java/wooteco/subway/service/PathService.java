package wooteco.subway.service;

import org.springframework.stereotype.Service;

import wooteco.subway.domain.Line;
import wooteco.subway.domain.Path;
import wooteco.subway.domain.Sections;
import wooteco.subway.domain.Station;
import wooteco.subway.domain.fare.Fare;
import wooteco.subway.domain.strategy.ShortestPathStrategy;
import wooteco.subway.dto.PathRequest;
import wooteco.subway.dto.PathResponse;
import wooteco.subway.exception.DuplicatedSourceAndTargetException;

@Service
public class PathService {

    private final StationService stationService;
    private final LineService lineService;
    private final SectionService sectionService;
    private final ShortestPathStrategy pathStrategy;

    public PathService(
        final StationService stationService,
        final LineService lineService,
        final SectionService sectionService,
        final ShortestPathStrategy pathStrategy
    ) {
        this.stationService = stationService;
        this.lineService = lineService;
        this.sectionService = sectionService;
        this.pathStrategy = pathStrategy;
    }

    public PathResponse createPath(final PathRequest pathRequest) {
        validateDuplicatedSourceAndTarget(pathRequest.getSource(), pathRequest.getTarget());
        final Station source = stationService.findStationById(pathRequest.getSource());
        final Station target = stationService.findStationById(pathRequest.getTarget());
        final Sections sections = new Sections(sectionService.findAllSections());

        final Path path = pathStrategy.findPath(source, target, sections);
        final int max = lineService.findAllByIds(path.getLineIds())
            .stream()
            .map(Line::getExtraFare)
            .max(Integer::compareTo)
            .orElseThrow();
        final Fare fare = Fare.from(path.getDistance(), max, pathRequest.getAge());

        return PathResponse.from(path, fare);
    }

    private void validateDuplicatedSourceAndTarget(final long sourceId, final long targetId) {
        if (sourceId == targetId) {
            throw new DuplicatedSourceAndTargetException("출발역과 도착역은 같을 수 없습니다.");
        }
    }
}
