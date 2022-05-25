package wooteco.subway.service;

import java.util.List;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.Multigraph;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Service;
import wooteco.subway.dao.SectionDao;
import wooteco.subway.dao.StationDao;
import wooteco.subway.domain.fare.FareCalculator;
import wooteco.subway.domain.path.Path;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.path.ShortestPathEdge;
import wooteco.subway.domain.Station;
import wooteco.subway.dto.request.PathRequest;
import wooteco.subway.dto.response.PathResponse;
import wooteco.subway.exception.NotFoundException;
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
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_STATION_ERROR_MESSAGE));
        Station target = stationDao.findById(pathRequest.getTarget())
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_STATION_ERROR_MESSAGE));

        List<Section> sections = sectionDao.findAll();
        Multigraph<Station, ShortestPathEdge> graph = new WeightedMultigraph<>(ShortestPathEdge.class);
        DijkstraShortestPath<Station, ShortestPathEdge> shortestPath = Jgrapht.initSectionGraph(sections, graph);
        List<Station> stations = Jgrapht.createShortestPath(shortestPath, source, target);
        int distance = Jgrapht.calculateDistance(shortestPath, source, target);
        int extraFare = Jgrapht.calculateExtraFare(shortestPath, source, target);

        Path path = new Path(stations, distance);
        FareCalculator fareCalculator = new FareCalculator();

        return new PathResponse(path, fareCalculator.calculateFare(distance, extraFare, pathRequest.getAge()));
    }

}
