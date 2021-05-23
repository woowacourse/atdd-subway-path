package wooteco.subway.path.application;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import wooteco.subway.line.dao.LineDao;
import wooteco.subway.line.domain.Line;
import wooteco.subway.line.domain.Section;
import wooteco.subway.path.domain.JgraphtPathFinder;
import wooteco.subway.path.domain.ShortestPathFinder;
import wooteco.subway.path.dto.PathResponse;
import wooteco.subway.path.exception.NotFoundStationException;
import wooteco.subway.station.dao.StationDao;
import wooteco.subway.station.domain.Station;
import wooteco.subway.station.dto.StationResponse;

@Service
public class PathService {

    private final StationDao stationDao;
    private final LineDao lineDao;

    public PathService(StationDao stationDao, LineDao lineDao) {
        this.stationDao = stationDao;
        this.lineDao = lineDao;
    }

    public PathResponse findShortestStationPath(Long fromStationId, Long toStationId) {
        List<Station> stations = stationDao.findAll();
        List<Line> lines = lineDao.findAll();
        List<Section> sections = findAllSections(lines);

        ShortestPathFinder shortestPathFinder = new JgraphtPathFinder(stations, sections);
        Station fromStation = findStationById(stations,fromStationId);
        Station toStation = findStationById(stations, toStationId);
        List<Station> shortestPath = shortestPathFinder.findShortestPath(fromStation, toStation);
        int shortestDistance = shortestPathFinder.findShortestDistance(fromStation, toStation);
        return new PathResponse(StationResponse.listOf(shortestPath), shortestDistance);
    }

    private List<Section> findAllSections(List<Line> lines) {
        return lines.stream()
            .flatMap(line -> line.getSections().getSections().stream())
            .collect(Collectors.toList());
    }

    private Station findStationById(List<Station> stations, Long stationId) {
        return stations.stream()
            .filter(station -> station.isEqualToId(stationId))
            .findAny()
            .orElseThrow(() -> new NotFoundStationException(stationId));
    }
}
