package wooteco.subway.path.service;

import org.springframework.stereotype.Service;
import wooteco.subway.line.dao.LineDao;
import wooteco.subway.path.domain.PathGraph;
import wooteco.subway.path.domain.PathFinder;
import wooteco.subway.path.dto.PathResponse;
import wooteco.subway.station.dao.StationDao;
import wooteco.subway.station.domain.Station;
import wooteco.subway.station.dto.StationResponse;

import java.util.stream.Collectors;

@Service
public class PathFindService {
    private final LineDao lineDao;
    private final StationDao stationDao;

    public PathFindService(final LineDao lineDao, final StationDao stationDao) {
        this.lineDao = lineDao;
        this.stationDao = stationDao;
    }

    public PathResponse findShortestPath(final Long source, final Long target) {
        PathFinder pathfinder = new PathFinder(new PathGraph(lineDao.findAll()));
        Station sourceStation = stationDao.findById(source);
        Station targetStation = stationDao.findById(target);

        return new PathResponse(
                pathfinder.getShortestPath(sourceStation, targetStation).stream()
                .map(StationResponse::of)
                .collect(Collectors.toList()),
                pathfinder.getShortestDistance(sourceStation, targetStation));
    }
}
