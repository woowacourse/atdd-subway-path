package wooteco.subway.path.application;

import org.springframework.stereotype.Service;
import wooteco.subway.line.dao.LineDao;
import wooteco.subway.line.domain.Line;
import wooteco.subway.path.domain.Path;
import wooteco.subway.path.domain.strategy.shortestpath.DijkstraShortestPathStrategy;
import wooteco.subway.path.dto.PathResponse;
import wooteco.subway.station.domain.Station;
import wooteco.subway.station.dto.StationResponse;

import java.util.List;
import java.util.Optional;

@Service
public class PathService {
    private final LineDao lineDao;

    public PathService(final LineDao lineDao) {
        this.lineDao = lineDao;
    }

    public PathResponse findShortestPath(final Long sourceId, final Long targetId) {
        List<Line> lines = lineDao.findAll();
        Path path = new Path(new DijkstraShortestPathStrategy(), lines);
        Station sourceStation = findStationById(lines, sourceId);
        Station targetStation = findStationById(lines, targetId);

        List<Station> stations = path.shortestPath(sourceStation, targetStation);
        int distance = path.shortestDistance(sourceStation, targetStation);

        return new PathResponse(StationResponse.listOf(stations), distance);
    }

    private Station findStationById(List<Line> lines, Long stationId) {
        return lines.stream()
                .map(line -> line.findStationById(stationId))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("전체 노선에 존재하지 않는 역입니다"));
    }
}
