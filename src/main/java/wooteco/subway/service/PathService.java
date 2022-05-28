package wooteco.subway.service;

import java.util.List;
import org.springframework.stereotype.Service;
import wooteco.subway.dao.SectionDao;
import wooteco.subway.dao.StationDao;
import wooteco.subway.domain.Path;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Station;
import wooteco.subway.domain.fare.Fare;
import wooteco.subway.dto.request.PathRequest;
import wooteco.subway.dto.response.PathResponse;
import wooteco.subway.exception.NotFoundException;
import wooteco.subway.infra.DijkstraStrategy;
import wooteco.subway.infra.ShortestPathFinder;

@Service
public class PathService {

    private static final String NOT_FOUND_STATION_ERROR_MESSAGE = "해당하는 역이 존재하지 않습니다.";

    private final StationDao stationDao;
    private final SectionDao sectionDao;

    public PathService(StationDao stationDao, SectionDao sectionDao) {
        this.stationDao = stationDao;
        this.sectionDao = sectionDao;
    }

    public PathResponse createShortestPath(PathRequest pathRequest) {
        Station source = stationDao.findById(pathRequest.getSource())
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_STATION_ERROR_MESSAGE));
        Station target = stationDao.findById(pathRequest.getTarget())
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_STATION_ERROR_MESSAGE));

        List<Section> sections = sectionDao.findAll();

        ShortestPathFinder shortestPathFinder = new ShortestPathFinder(new DijkstraStrategy(sections));
        Path path = shortestPathFinder.createShortestPath(source, target);
        Fare fare = new Fare();

        return new PathResponse(path, fare.calculateFare(path.getDistance(), path.getExtraFare(), pathRequest.getAge()));
    }

}
