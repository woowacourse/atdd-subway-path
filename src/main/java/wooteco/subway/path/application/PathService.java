package wooteco.subway.path.application;

import org.jgrapht.GraphPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.line.dao.LineDao;
import wooteco.subway.path.domain.Subway;
import wooteco.subway.path.dto.PathResponse;
import wooteco.subway.station.dao.StationDao;
import wooteco.subway.station.domain.Station;

import java.util.List;

@Service
public class PathService {
    private final StationDao stationDao;
    private final LineDao lineDao;

    public PathService(StationDao stationDao, LineDao lineDao) {
        this.stationDao = stationDao;
        this.lineDao = lineDao;
    }

    @Transactional(readOnly = true)
    public PathResponse findPath(Long sourceId, Long targetId) {
        Subway subway = new Subway(lineDao.findAll());
        GraphPath<Station, DefaultWeightedEdge> graphPath = subway.findPath(
                stationDao.findById(sourceId),
                stationDao.findById(targetId)
        );
        List<Station> shortestPath = graphPath.getVertexList();
        int distance = (int) graphPath.getWeight();
        return new PathResponse(shortestPath, distance);
    }
}
