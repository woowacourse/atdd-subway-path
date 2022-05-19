package wooteco.subway.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import wooteco.subway.dao.SectionDao;
import wooteco.subway.dao.StationDao;
import wooteco.subway.domain.Path;
import wooteco.subway.domain.Sections;
import wooteco.subway.domain.Station;
import wooteco.subway.domain.strategy.fare.FareStrategy;
import wooteco.subway.domain.strategy.path.PathFindStrategy;
import wooteco.subway.dto.PathRequest;
import wooteco.subway.dto.PathResponse;
import wooteco.subway.dto.StationResponse;

@Service
public class PathService {

    private final StationDao stationDao;
    private final SectionDao sectionDao;
    private final PathFindStrategy pathStrategy;
    private final FareStrategy fareStrategy;

    public PathService(StationDao stationDao, SectionDao sectionDao, PathFindStrategy pathStrategy, FareStrategy fareStrategy) {
        this.stationDao = stationDao;
        this.sectionDao = sectionDao;
        this.pathStrategy = pathStrategy;
        this.fareStrategy = fareStrategy;
    }

    public PathResponse findShortestPath(PathRequest pathRequest) {
        Station source = stationDao.findById(pathRequest.getSource());
        Station target = stationDao.findById(pathRequest.getTarget());

        Sections sections = new Sections(sectionDao.findAll());

        Path path = pathStrategy.calculatePath(source, target, sections);

        return new PathResponse(toResponse(path.getStations()), path.getDistance(), fareStrategy.calculateFare(path.getDistance()));
    }

    private List<StationResponse> toResponse(List<Station> stations) {
        return stations.stream()
                .map(StationResponse::new)
                .collect(Collectors.toList());
    }
}
