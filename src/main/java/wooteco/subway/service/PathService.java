package wooteco.subway.service;

import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import wooteco.subway.dao.SectionDao;
import wooteco.subway.dao.StationDao;
import wooteco.subway.domain.Fare;
import wooteco.subway.domain.Path;
import wooteco.subway.dto.path.PathResponse;
import wooteco.subway.dto.station.StationResponse;

@Service
public class PathService {

    private final StationDao stationDao;
    private final SectionDao sectionDao;

    public PathService(StationDao stationDao, SectionDao sectionDao) {
        this.stationDao = stationDao;
        this.sectionDao = sectionDao;
    }

    public PathResponse findPath(Long source, Long target, int age) {
        var path = new Path(stationDao.findAll(), sectionDao.findAll());

        var shortestPath = path.getPath(source, target);

        var stations = shortestPath.getVertexList().stream()
                .map(stationDao::findById)
                .map(StationResponse::new)
                .collect(Collectors.toList());

        var distance = shortestPath.getWeight();

        var fare = new Fare(distance);

        return new PathResponse(stations, distance, fare);
    }
}
