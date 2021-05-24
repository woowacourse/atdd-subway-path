package wooteco.subway.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.dao.LineDao;
import wooteco.subway.domain.DijkstraPath;
import wooteco.subway.domain.Lines;
import wooteco.subway.domain.Station;
import wooteco.subway.domain.Stations;
import wooteco.subway.web.dto.PathResponse;

@Service
@Transactional(readOnly = true)
public class PathService {

    private final LineDao lineDao;

    public PathService(LineDao lineDao) {
        this.lineDao = lineDao;
    }

    public PathResponse findShortestPaths(Long sourceStationId, Long targetStationId) {
        final Lines lines = new Lines(lineDao.findAll());
        final Stations stations = lines.stations();
        final DijkstraPath dijkstraPath = new DijkstraPath(lines);
        final List<Station> stationsInPath =
            dijkstraPath.shortestPath(sourceStationId, targetStationId)
            .stream()
            .map(stations::findStationById)
            .collect(Collectors.toList());
        final int distance = dijkstraPath.distance(sourceStationId, targetStationId);

        return new PathResponse(stationsInPath, distance);
    }
}
