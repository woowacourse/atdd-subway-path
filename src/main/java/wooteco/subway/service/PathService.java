package wooteco.subway.service;

import java.util.List;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.springframework.stereotype.Service;
import wooteco.subway.dao.SectionDao;
import wooteco.subway.dao.StationDao;
import wooteco.subway.domain.BasicFareStrategy;
import wooteco.subway.domain.Fare;
import wooteco.subway.domain.Path;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Station;
import wooteco.subway.dto.request.PathRequest;
import wooteco.subway.dto.response.PathResponse;
import wooteco.subway.exception.NotFoundStationException;
import wooteco.subway.utils.Jgrapht;

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
                .orElseThrow(() -> new NotFoundStationException(NOT_FOUND_STATION_ERROR_MESSAGE));
        Station target = stationDao.findById(pathRequest.getTarget())
                .orElseThrow(() -> new NotFoundStationException(NOT_FOUND_STATION_ERROR_MESSAGE));

        List<Section> sections = sectionDao.findAll();
        DijkstraShortestPath shortestPath = Jgrapht.initSectionGraph(sections);
        List<Station> stations = Jgrapht.createShortestPath(shortestPath, source, target);
        int distance = Jgrapht.calculateDistance(shortestPath, source, target);

        Path path = new Path(stations, distance);
        Fare fare = new Fare();

        return new PathResponse(path, fare.calculateFare(distance, new BasicFareStrategy()));
    }

}
