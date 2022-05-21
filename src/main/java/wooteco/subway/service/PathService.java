package wooteco.subway.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import wooteco.subway.dao.SectionDao;
import wooteco.subway.dao.StationDao;
import wooteco.subway.domain.FareCalculator;
import wooteco.subway.domain.Path;
import wooteco.subway.domain.Sections;
import wooteco.subway.domain.Station;
import wooteco.subway.dto.PathResponse;
import wooteco.subway.dto.StationResponse;

@Service
public class PathService {

    private final SectionDao sectionDao;
    private final StationDao stationDao;

    public PathService(final SectionDao sectionDao, final StationDao stationDao) {
        this.sectionDao = sectionDao;
        this.stationDao = stationDao;
    }

    public PathResponse createPath(final Long sourceStationId, final Long targetStationId, final int age) {
        Station source = stationDao.findById(sourceStationId);
        Station target = stationDao.findById(targetStationId);
        return createPathResponse(source, target);
    }

    private PathResponse createPathResponse(final Station source, final Station target) {
        Path path = initPath();
        List<Station> stations = path.calculateShortestPath(source, target);
        double distance = path.calculateShortestDistance(source, target);
        int fare = createFare(distance);
        return new PathResponse(convertToStationResponses(stations), distance, fare);
    }

    private Path initPath() {
        Sections sections = new Sections(sectionDao.findAll());
        return new Path(sections);
    }

    private int createFare(final double distance) {
        FareCalculator fareCalculator = new FareCalculator(distance);
        return fareCalculator.calculateFare();
    }

    private List<StationResponse> convertToStationResponses(final List<Station> stations) {
        return stations.stream()
                .map(StationResponse::from)
                .collect(Collectors.toList());
    }
}
