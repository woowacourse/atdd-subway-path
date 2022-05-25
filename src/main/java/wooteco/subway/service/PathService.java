package wooteco.subway.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import wooteco.subway.dao.LineDao;
import wooteco.subway.dao.SectionDao;
import wooteco.subway.dao.StationDao;
import wooteco.subway.domain.DiscountSpecification;
import wooteco.subway.domain.FareCacluateSpecification;
import wooteco.subway.domain.Path;
import wooteco.subway.domain.PathFindSpecification;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Station;
import wooteco.subway.domain.discount.DiscountStrategy;
import wooteco.subway.domain.path.PathFindStrategy;
import wooteco.subway.domain.pricing.PricingStrategy;
import wooteco.subway.service.dto.PathResponse;
import wooteco.subway.service.dto.StationResponse;
import wooteco.subway.ui.dto.PathRequest;

@Service
public class PathService {

    private final StationDao stationDao;
    private final LineDao lineDao;
    private final SectionDao sectionDao;
    private final PathFindStrategy pathFindStrategy;
    private final PricingStrategy pricingStrategy;
    private final DiscountStrategy discountStrategy;

    public PathService(StationDao stationDao, LineDao lineDao, SectionDao sectionDao,
                       @Qualifier("MinimumDistance") PathFindStrategy pathFindStrategy,
                       @Qualifier("AllPricingStrategy") PricingStrategy pricingStrategy,
                       @Qualifier("Age") DiscountStrategy discountStrategy) {
        this.stationDao = stationDao;
        this.lineDao = lineDao;
        this.sectionDao = sectionDao;
        this.pathFindStrategy = pathFindStrategy;
        this.pricingStrategy = pricingStrategy;
        this.discountStrategy = discountStrategy;
    }

    public PathResponse searchPaths(PathRequest pathRequest) {
        Path path = getPath(pathFindStrategy, pathRequest.getSource(), pathRequest.getTarget());
        int fare = getFare(path);
        int discountFare = applyFare(discountStrategy, pathRequest.getAge(), fare);

        return new PathResponse(
                path.getDistance(),
                discountFare,
                generateStationResponses(path.getStationsInPath())
        );
    }

    private int applyFare(DiscountStrategy discountStrategy, int age, int fare) {
        DiscountSpecification specification = new DiscountSpecification(age, fare);
        return discountStrategy.discount(specification);
    }

    private int getFare(Path path) {
        FareCacluateSpecification fareCacluateSpecification = new FareCacluateSpecification(path.getSectionsInPath(), lineDao.findAll());
        return pricingStrategy.calculateFee(fareCacluateSpecification);
    }

    private Path getPath(PathFindStrategy pathFindStrategy, Long sourceId, Long targetId) {
        List<Section> sections = sectionDao.findAll();
        List<Station> stations = stationDao.findAll();
        Station from = findById(stations, sourceId);
        Station to = findById(stations, targetId);

        PathFindSpecification pathFindSpecification = new PathFindSpecification(from, to, stations, sections);
        return pathFindStrategy.findPath(pathFindSpecification);
    }

    private Station findById(List<Station> stations, Long id) {
        return stations.stream()
                .filter(it -> id.equals(it.getId()))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("해당 id인 역은 존재하지 않습니다"));
    }

    private List<StationResponse> generateStationResponses(List<Station> stations) {
        return stations.stream()
                .map(StationResponse::from)
                .collect(Collectors.toList());
    }
}
