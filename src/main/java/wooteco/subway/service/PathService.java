package wooteco.subway.service;

import org.jgrapht.GraphPath;
import org.springframework.stereotype.Service;
import wooteco.subway.dao.LineDao;
import wooteco.subway.dao.SectionDao;
import wooteco.subway.dao.StationDao;
import wooteco.subway.domain.line.Lines;
import wooteco.subway.domain.path.Path;
import wooteco.subway.domain.path.PathFinder;
import wooteco.subway.domain.secion.Sections;
import wooteco.subway.domain.graph.ShortestPathEdge;
import wooteco.subway.domain.Station;
import wooteco.subway.domain.graph.SubwayGraph;
import wooteco.subway.dto.PathResponse;

@Service
public class PathService {

    private final SectionDao sectionDao;
    private final StationDao stationDao;
    private final LineDao lineDao;

    public PathService(final SectionDao sectionDao, final StationDao stationDao, final LineDao lineDao) {
        this.sectionDao = sectionDao;
        this.stationDao = stationDao;
        this.lineDao = lineDao;
    }

    public PathResponse createPath(final Long sourceStationId, final Long targetStationId, final int age) {
        Station source = stationDao.findById(sourceStationId);
        Station target = stationDao.findById(targetStationId);
        return createPathResponse(source, target, age);
    }

    private PathResponse createPathResponse(final Station source, final Station target, final int age) {
        GraphPath<Station, ShortestPathEdge> graph = findGraph(source, target);
        Lines lines = new Lines(lineDao.findAll());
        PathFinder pathFinder = new PathFinder();
        Path path = pathFinder.getPath(graph, lines, age);
        return PathResponse.from(path);
    }

    private GraphPath<Station, ShortestPathEdge> findGraph(final Station source, final Station target) {
        SubwayGraph subwayGraph = new SubwayGraph();
        subwayGraph.init(new Sections(sectionDao.findAll()));
        return subwayGraph.graphResult(source, target);
    }

}
