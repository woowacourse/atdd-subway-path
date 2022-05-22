package wooteco.subway.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import wooteco.subway.dao.SectionDao;
import wooteco.subway.dao.StationDao;
import wooteco.subway.domain.Path;
import wooteco.subway.domain.path.PathFindStrategy;
import wooteco.subway.domain.pricing.PricingStrategy;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Station;
import wooteco.subway.service.dto.PathResponse;
import wooteco.subway.service.dto.StationResponse;

@Service
public class PathService {

    private final StationDao stationDao;
    private final SectionDao sectionDao;

    public PathService(StationDao stationDao, SectionDao sectionDao) {
        this.stationDao = stationDao;
        this.sectionDao = sectionDao;
    }

    public PathResponse searchPaths(PathFindStrategy pathFindStrategy, PricingStrategy pricingStrategy, Long sourceId, Long targetId) {
        List<Section> sections = sectionDao.findAll();
        List<Station> stations = stationDao.findAll();
        Station from = findById(stations, sourceId);
        Station to = findById(stations, targetId);
        Path path = pathFindStrategy.findPath(stations, sections, from, to);

        return new PathResponse(
                path.getDistance(),
                pricingStrategy.calculateFee(path.getSectionsInPath()),
                generateStationResponses(path.getStationsInPath())
        );
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
