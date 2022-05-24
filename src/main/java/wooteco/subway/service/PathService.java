package wooteco.subway.service;

import java.util.List;
import java.util.stream.Collectors;
import org.jgrapht.GraphPath;
import org.springframework.stereotype.Service;
import wooteco.subway.dao.LineDao;
import wooteco.subway.dao.SectionDao;
import wooteco.subway.dao.StationDao;
import wooteco.subway.domain.Lines;
import wooteco.subway.domain.Sections;
import wooteco.subway.domain.ShortestPathEdge;
import wooteco.subway.domain.Station;
import wooteco.subway.domain.SubwayGraph;
import wooteco.subway.domain.fare.FareCalculator;
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
        List<Station> stations = graph.getVertexList();
        double distance = graph.getWeight();
        List<Long> lineIds = graph.getEdgeList().stream()
                .map(ShortestPathEdge::getLineId)
                .collect(Collectors.toList());
        Lines lines = new Lines(lineDao.findAll());
        int maxExtraFare = lines.findMaxExtraFare(lineIds);
        int fare = new FareCalculator(distance).calculateFare(age, maxExtraFare);
        return new PathResponse(stations, distance, fare);
    }

    private GraphPath<Station, ShortestPathEdge> findGraph(final Station source, final Station target) {
        SubwayGraph subwayGraph = new SubwayGraph();
        subwayGraph.init(new Sections(sectionDao.findAll()));
        return subwayGraph.graphResult(source, target);
    }

}
