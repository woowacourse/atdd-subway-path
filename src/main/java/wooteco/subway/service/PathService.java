package wooteco.subway.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import wooteco.subway.domain.PathFinder;
import wooteco.subway.domain.PathFinderFactory;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Station;
import wooteco.subway.domain.policy.DiscountPolicy;
import wooteco.subway.domain.policy.DiscountPolicyFactory;
import wooteco.subway.domain.fare.Fare;
import wooteco.subway.domain.fare.FareFactory;
import wooteco.subway.dto.PathResponse;
import wooteco.subway.repository.LineRepository;
import wooteco.subway.repository.StationRepository;

@Service
public class PathService {

    private final StationRepository stationRepository;
    private final LineRepository lineRepository;
    private final PathFinderFactory pathFinderFactory;

    public PathService(StationRepository stationRepository,
                       LineRepository lineRepository, PathFinderFactory pathFinderFactory) {
        this.stationRepository = stationRepository;
        this.lineRepository = lineRepository;
        this.pathFinderFactory = pathFinderFactory;
    }

    public PathResponse getPath(Long from, Long to, int age) throws Exception {
        PathFinder pathFinder = pathFinderFactory.getObject();
        Station fromStation = stationRepository.findById(from);
        Station toStation = stationRepository.findById(to);
        List<Station> stations = pathFinder.calculatePath(fromStation, toStation);
        List<Section> sections = pathFinder.calculateSections(fromStation, toStation);
        int extraFare = calculateMaxExtraFare(sections);
        int distance = pathFinder.calculateDistance(fromStation, toStation);
        Fare fare = new FareFactory().getFare(distance, extraFare);
        DiscountPolicy discountPolicy = new DiscountPolicyFactory().getDiscountPolicy(age);
        return PathResponse.of(stations, distance, discountPolicy.calculateDiscountFare(fare));
    }

    private int calculateMaxExtraFare(List<Section> sections) {
        List<Long> lineIds = sections.stream()
                .map(Section::getLineId)
                .distinct()
                .collect(Collectors.toList());
        return lineRepository.findMaxExtraFare(lineIds);
    }
}
