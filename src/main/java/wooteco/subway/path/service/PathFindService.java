package wooteco.subway.path.service;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Service;
import wooteco.subway.line.dao.LineDao;
import wooteco.subway.path.domain.PathGraph;
import wooteco.subway.path.domain.StationPathFinder;
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
        StationPathFinder pathfinder = new StationPathFinder(new PathGraph(lineDao.findAll(), new WeightedMultigraph<>(DefaultWeightedEdge.class)));
        Station sourceStation = stationDao.findById(source);
        Station targetStation = stationDao.findById(target);

        return new PathResponse(
                pathfinder.getShortestPath(sourceStation, targetStation).stream()
                .map(StationResponse::of)
                .collect(Collectors.toList()),
                pathfinder.getShortestDistance(sourceStation, targetStation));
    }
}
