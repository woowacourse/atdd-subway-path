package wooteco.subway.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.dao.LineDao;
import wooteco.subway.dao.SectionDao;
import wooteco.subway.domain.Line;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Sections;
import wooteco.subway.domain.fare.AgeDiscountPolicy;
import wooteco.subway.domain.fare.DiscountPolicy;
import wooteco.subway.domain.fare.Fare;
import wooteco.subway.domain.fare.FarePolicy;
import wooteco.subway.domain.fare.FarePolicyImpl;
import wooteco.subway.domain.shortestpath.DistanceShortestPathStrategy;
import wooteco.subway.domain.shortestpath.ShortestPath;
import wooteco.subway.dto.request.PathRequest;
import wooteco.subway.dto.response.PathResponse;
import wooteco.subway.dto.response.StationResponse;

@Service
public class PathService {

    private final LineDao lineDao;
    private final SectionDao sectionDao;
    private final StationService stationService;

    public PathService(LineDao lineDao, SectionDao sectionDao, StationService stationService) {
        this.lineDao = lineDao;
        this.sectionDao = sectionDao;
        this.stationService = stationService;
    }

    @Transactional(readOnly = true)
    public PathResponse findShortestPath(PathRequest pathRequest) {
        validateExistStations(pathRequest);
        List<Section> allSections = sectionDao.findAll();
        ShortestPath shortestPath = new ShortestPath(new DistanceShortestPathStrategy(), new Sections(allSections),
                pathRequest.getSource(), pathRequest.getTarget());

        Fare fare = calculateFare(shortestPath);
        fare = discountFare(fare, pathRequest.getAge());

        List<Long> stationIds = shortestPath.getStationIds(pathRequest.getSource(), pathRequest.getTarget());
        List<StationResponse> stations = stationService.findByStationIds(stationIds);
        return new PathResponse(stations, shortestPath.getTotalDistance(), fare.getValue());
    }

    private Fare discountFare(Fare fare, int age) {
        DiscountPolicy discountPolicy = new AgeDiscountPolicy();
        return discountPolicy.discountFare(fare, age);
    }

    private void validateExistStations(PathRequest pathRequest) {
        stationService.validateExistById(pathRequest.getSource());
        stationService.validateExistById(pathRequest.getTarget());
    }

    private Fare calculateFare(ShortestPath shortestPath) {
        FarePolicy farePolicy = new FarePolicyImpl();

        List<Long> lineIds = shortestPath.getLineIds();
        List<Line> lines = lineDao.findByIds(lineIds);
        return farePolicy.calculateFare(shortestPath.getTotalDistance(), lines);
    }
}
