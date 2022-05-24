package wooteco.subway.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.dao.LineDao;
import wooteco.subway.dao.SectionDao;
import wooteco.subway.domain.discount.DiscountCondition;
import wooteco.subway.domain.discount.DiscountPolicy;
import wooteco.subway.domain.Fare;
import wooteco.subway.domain.Line;
import wooteco.subway.domain.path.Path;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Sections;
import wooteco.subway.domain.path.PathFindStrategy;
import wooteco.subway.dto.request.PathRequest;
import wooteco.subway.dto.response.PathResponse;
import wooteco.subway.dto.response.StationResponse;

@Service
public class PathService {

    private final SectionDao sectionDao;
    private final LineDao lineDao;
    private final StationService stationService;
    private final List<DiscountPolicy> discountPolicies;
    private final PathFindStrategy pathFindStrategy;

    public PathService(final SectionDao sectionDao, final LineDao lineDao,
                       final StationService stationService, final List<DiscountPolicy> discountPolicies,
                       final PathFindStrategy pathFindStrategy) {
        this.sectionDao = sectionDao;
        this.lineDao = lineDao;
        this.stationService = stationService;
        this.discountPolicies = discountPolicies;
        this.pathFindStrategy = pathFindStrategy;
    }

    @Transactional(readOnly = true, isolation = Isolation.REPEATABLE_READ)
    public PathResponse findShortestPath(final PathRequest pathRequest) {
        validateExistStations(pathRequest);
        final Path shortestPath = createPath(pathRequest);
        final Fare fare = calculateFare(pathRequest, shortestPath);

        final List<Long> stationIds = shortestPath.getStationIds();
        final List<StationResponse> stations = stationService.findByStationIds(stationIds);
        return new PathResponse(stations, shortestPath.getTotalDistance(), fare.getValue());
    }

    private void validateExistStations(final PathRequest pathRequest) {
        stationService.validateExistById(pathRequest.getSource());
        stationService.validateExistById(pathRequest.getTarget());
    }

    private Path createPath(final PathRequest pathRequest) {
        final List<Section> allSections = sectionDao.findAll();
        return Path.of(new Sections(allSections), pathRequest.getSource(), pathRequest.getTarget(), pathFindStrategy);
    }

    private Fare calculateFare(final PathRequest pathRequest, final Path shortestPath) {
        final int maxExtraFare = getMaxExtraFare(shortestPath.getUsedLineIds());
        return Fare.of(
                shortestPath.getTotalDistance(),
                maxExtraFare,
                discountPolicies,
                new DiscountCondition(pathRequest.getAge(), shortestPath.getTotalDistance())
        );
    }

    private int getMaxExtraFare(final List<Long> lineIds) {
        final List<Line> allLines = lineDao.findAll();
        return allLines.stream()
                .filter(line -> lineIds.contains(line.getId()))
                .mapToInt(Line::getExtraFare)
                .max()
                .orElse(0);
    }
}
