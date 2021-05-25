package wooteco.subway.path.application;

import org.jgrapht.GraphPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.springframework.stereotype.Service;
import wooteco.subway.line.application.LineService;
import wooteco.subway.line.dao.SectionDao;
import wooteco.subway.line.domain.Line;
import wooteco.subway.line.domain.Section;
import wooteco.subway.path.domain.Path;
import wooteco.subway.path.domain.ShortestDistanceStrategy;
import wooteco.subway.path.dto.PathResponse;
import wooteco.subway.station.dao.StationDao;
import wooteco.subway.station.domain.Station;

import java.util.List;

@Service
public class PathService {
    private final StationDao stationDao;
    private final LineService lineService;

    public PathService(StationDao stationDao, LineService lineService) {
        this.stationDao = stationDao;
        this.lineService = lineService;
    }

    public PathResponse findShortestPath(Long sourceId, Long targetId) {
        List<Station> stations = stationDao.findAll();
        List<Line> lines = lineService.findLines();

        Path path = new Path(stations, lines, new ShortestDistanceStrategy());

        GraphPath<Station, DefaultWeightedEdge> shortestPath = path.calculateShortestPath(sourceId, targetId);
        List<Station> stationsInPath = shortestPath.getVertexList();
        int shortestDistance = (int) shortestPath.getWeight();

        return new PathResponse(stationsInPath, shortestDistance);
    }
}
