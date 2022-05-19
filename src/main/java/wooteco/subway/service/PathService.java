package wooteco.subway.service;

import org.jgrapht.Graph;
import org.springframework.stereotype.Service;
import wooteco.subway.dao.LineDao;
import wooteco.subway.dao.SectionDao;
import wooteco.subway.dao.StationDao;
import wooteco.subway.domain.fare.Fare;
import wooteco.subway.domain.line.Line;
import wooteco.subway.domain.path.strategy.DijkstraStrategy;
import wooteco.subway.domain.path.Path;
import wooteco.subway.domain.path.PathEdge;
import wooteco.subway.domain.path.SubwayFactory;
import wooteco.subway.domain.station.Station;
import wooteco.subway.dto.PathRequest;
import wooteco.subway.dto.PathResponse;
import wooteco.subway.dto.StationResponse;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PathService {
    private final StationDao stationDao;
    private final SectionDao sectionDao;
    private final LineDao lineDao;

    public PathService(StationDao stationDao, SectionDao sectionDao, LineDao lineDao) {
        this.stationDao = stationDao;
        this.sectionDao = sectionDao;
        this.lineDao = lineDao;
    }

    public PathResponse searchPath(PathRequest pathRequest) {
        Graph<Long, PathEdge> subway = SubwayFactory.from(sectionDao.findAll());
        Path shortestPath = Path.of(subway, pathRequest.getSource(), pathRequest.getTarget(), new DijkstraStrategy());

        List<Long> shortestPathStationIds = shortestPath.getNodes();
        int distance = shortestPath.calculateDistance();

        Map<Long, Integer> lineExtraFares = lineDao.findAll()
                .stream()
                .collect(Collectors.toMap(Line::getId, Line::getExtraFare));
        int extraFare = shortestPath.getHighestExtraFare(lineExtraFares);
        Fare fare = Fare.of(distance, pathRequest.getAge(), extraFare);

        return new PathResponse(createStationResponseOf(shortestPathStationIds), distance, fare.calculateFare());
    }

    private List<StationResponse> createStationResponseOf(List<Long> path) {
        List<Station> stations = stationDao.findByIdIn(path);

        Map<Long, String> stationNames = stations.stream()
                .collect(Collectors.toMap(Station::getId, Station::getName));

        return path.stream()
                .map(node -> new StationResponse(node, stationNames.get(node)))
                .collect(Collectors.toList());
    }
}
