package wooteco.subway.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import wooteco.subway.dao.LineDao;
import wooteco.subway.dao.SectionDao;
import wooteco.subway.dao.StationDao;
import wooteco.subway.domain.fare.ExtraFareCalculator;
import wooteco.subway.domain.fare.Fare;
import wooteco.subway.domain.fare.AgeDiscountStrategy;
import wooteco.subway.domain.line.Line;
import wooteco.subway.domain.path.DijkstraStrategy;
import wooteco.subway.domain.path.Path;
import wooteco.subway.domain.section.Section;
import wooteco.subway.domain.station.Station;
import wooteco.subway.dto.PathRequest;
import wooteco.subway.dto.PathResponse;
import wooteco.subway.dto.StationResponse;

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
        List<Station> stations = stationDao.findAll();
        List<Section> sections = sectionDao.findAll();
        List<Line> lines = lineDao.findAll();

        Path path = new Path(stations, sections, new DijkstraStrategy());

        List<Long> shortestPath = path.getShortestPath(pathRequest.getSource(), pathRequest.getTarget());
        int distance = path.calculateShortestDistance(pathRequest.getSource(), pathRequest.getTarget());

        ExtraFareCalculator extraFareCalculator = new ExtraFareCalculator(sections, lines);
        int mostExpensiveExtraFare = extraFareCalculator.getMostExpensiveExtraFare(shortestPath);

        Fare fare = new Fare(distance, pathRequest.getAge(), mostExpensiveExtraFare, new AgeDiscountStrategy());

        return new PathResponse(createStationResponseOf(shortestPath), distance, fare.calculateFare());
    }

    private List<StationResponse> createStationResponseOf(List<Long> path) {
        List<Station> stations = stationDao.findByIdIn(path);

        Map<Long, String> stationMap = stations.stream()
                .collect(Collectors.toMap(Station::getId, Station::getName));

        return path.stream()
                .map(node -> new StationResponse(node, stationMap.get(node)))
                .collect(Collectors.toList());
    }
}
