package wooteco.subway.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import wooteco.subway.dao.SectionDao;
import wooteco.subway.dao.StationDao;
import wooteco.subway.domain.DiscountTable;
import wooteco.subway.domain.FareCalculator;
import wooteco.subway.domain.Path;
import wooteco.subway.domain.Section;
import wooteco.subway.dto.PathResponse;
import wooteco.subway.dto.SectionDto;
import wooteco.subway.dto.StationResponse;

@Service
public class PathService {

    private final SectionDao sectionDao;
    private final StationDao stationDao;
    private final LineService lineService;

    public PathService(SectionDao sectionDao, StationDao stationDao, LineService lineService) {
        this.sectionDao = sectionDao;
        this.stationDao = stationDao;
        this.lineService = lineService;
    }

    public PathResponse findPath(Long source, Long target, int age) {
        List<SectionDto> sectionDtos = sectionDao.findAll();

        List<Section> sections = sectionDtos.stream()
                .map(sectionDto -> new Section(sectionDto.getId(), lineService.getById(sectionDto.getLineId()),
                        sectionDto.getUpStationId(), sectionDto.getDownStationId(), sectionDto.getDistance()))
                .collect(Collectors.toList());

        Path path = new Path(sections);

        List<Long> shortestPath = path.calculateShortestPath(source, target);
        int shortestDistance = path.calculateShortestDistance(source, target);

        List<StationResponse> stationResponses = shortestPath.stream()
                .map(stationDao::getById)
                .map(station -> new StationResponse(station.getId(), station.getName()))
                .collect(Collectors.toList());

        int basicFare = FareCalculator.calculate(shortestDistance);
        int extraFare = path.calculateExtraFare(source, target);
        int fareWithDiscount = DiscountTable.calculateFareWithDiscount(basicFare + extraFare, age);

        return new PathResponse(stationResponses, shortestDistance, fareWithDiscount);
    }
}
