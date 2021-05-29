package wooteco.subway.path.application;

import java.util.LinkedList;
import java.util.List;
import org.jgrapht.alg.shortestpath.BellmanFordShortestPath;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.line.dao.LineDao;
import wooteco.subway.line.domain.Section;
import wooteco.subway.path.domain.PathFinder;
import wooteco.subway.path.domain.SubwayWeightedMultiGraph;
import wooteco.subway.path.dto.PathResponse;
import wooteco.subway.station.dao.StationDao;
import wooteco.subway.station.domain.Station;
import wooteco.subway.station.dto.StationResponse;

@Service
public class PathService {

    private final LineDao lineDao;
    private final StationDao stationDao;

    public PathService(LineDao lineDao, StationDao stationDao) {
        this.lineDao = lineDao;
        this.stationDao = stationDao;
    }

    @Transactional(readOnly = true)
    public PathResponse findPath(Long source, Long target) {
        List<Section> sections = new LinkedList<>();
        lineDao.findAll().forEach(line -> {
            sections.addAll(line.sections());
        });
        Station sourceStation = stationDao.findById(source);
        Station targetStation = stationDao.findById(target);

        SubwayWeightedMultiGraph subwayWeightedMultiGraph = new SubwayWeightedMultiGraph(sections);
        PathFinder pathFinder = new PathFinder(new BellmanFordShortestPath<>(subwayWeightedMultiGraph.getGraph()));

        return new PathResponse(StationResponse.listOf(pathFinder.shortestPath(sourceStation, targetStation)),
            pathFinder.shortestPathDistance(sourceStation, targetStation));
    }

}
