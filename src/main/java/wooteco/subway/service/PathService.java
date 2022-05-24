package wooteco.subway.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import wooteco.subway.dao.StationDao;
import wooteco.subway.domain.DiscountTable;
import wooteco.subway.domain.FareCalculator;
import wooteco.subway.domain.Path;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.SectionRepository;
import wooteco.subway.domain.Station;
import wooteco.subway.dto.PathResponse;
import wooteco.subway.dto.StationResponse;

@Service
public class PathService {

    private final SectionRepository sectionRepository;
    private final StationDao stationDao;
    private final LineService lineService;

    public PathService(SectionRepository sectionRepository, StationDao stationDao, LineService lineService) {
        this.sectionRepository = sectionRepository;
        this.stationDao = stationDao;
        this.lineService = lineService;
    }

    public PathResponse findPath(Long source, Long target, int age) {
        List<Section> sections = sectionRepository.getSections();
        Path path = new Path(sections);

        List<Long> shortestPath = path.calculateShortestPath(source, target);
        int shortestDistance = path.calculateShortestDistance(source, target);

        List<Station> stations = stationDao.getByIds(new ArrayList<>(shortestPath));
        List<StationResponse> stationResponses = stations.stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());

        int basicFare = FareCalculator.calculate(shortestDistance);
        int extraFare = path.calculateExtraFare(source, target);
        int fareWithDiscount = DiscountTable.calculateFareWithDiscount(basicFare + extraFare, age);

        return new PathResponse(stationResponses, shortestDistance, fareWithDiscount);
    }
}
