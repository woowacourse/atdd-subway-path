package wooteco.subway.service;

import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import wooteco.subway.dao.SectionDao;
import wooteco.subway.dao.StationDao;
import wooteco.subway.domain.Fare;
import wooteco.subway.domain.Path;
import wooteco.subway.domain.Sections;
import wooteco.subway.domain.Station;
import wooteco.subway.dto.PathResponse;
import wooteco.subway.dto.StationResponse;

@Service
public class PathService {

    private final StationDao stationDao;
    private final SectionDao sectionDao;

    public PathService(StationDao stationDao, SectionDao sectionDao) {
        this.stationDao = stationDao;
        this.sectionDao = sectionDao;
    }

    public PathResponse findPath(Long sourceId, Long targetId, int age) {
        final Sections sections = new Sections(sectionDao.findAll());
        final Path path = Path.of(sections, sourceId, targetId);


        final List<Long> shortestPath = path.getShortestPath();
        final List<StationResponse> stations = getStationResponses(shortestPath);

        final int distance = path.getShortestPathWeight();

        final Fare fare = Fare.from(distance);

        return new PathResponse(stations, distance, fare.getValue());
    }

    private List<StationResponse> getStationResponses(List<Long> shortestPath) {
        final List<Station> stations = shortestPath.stream()
                .map(stationDao::findById)
                .map(Optional::orElseThrow)
                .collect(toList());

        return stations.stream()
                .map(StationResponse::new)
                .collect(toList());
    }
}
