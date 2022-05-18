package wooteco.subway.service;

import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.Objects;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.dao.SectionDao;
import wooteco.subway.domain.Fare;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Sections;
import wooteco.subway.domain.Station;
import wooteco.subway.dto.PathResponse;
import wooteco.subway.dto.StationResponse;

@Transactional(readOnly = true)
@Service
public class PathService {

    private final SectionDao sectionDao;
    private final StationService stationService;

    public PathService(SectionDao sectionDao, StationService stationService) {
        this.sectionDao = sectionDao;
        this.stationService = stationService;
    }

    public PathResponse findPath(Long sourceId, Long targetId, int age) {
        Station source = stationService.findById(sourceId).toStation();
        Station target = stationService.findById(targetId).toStation();


        WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph(DefaultWeightedEdge.class);

        Sections sections = new Sections(sectionDao.findAll());
        for (Station station : sections.getStations()) {
            graph.addVertex(station);
        }

        for (Section section : sections.getValue()) {
            graph.setEdgeWeight(graph.addEdge(section.getUpStation(), section.getDownStation()), section.getDistance());
        }

        DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graph);
        GraphPath<Station, DefaultWeightedEdge> shortestPath = getPath(source, target, dijkstraShortestPath);

        if (Objects.isNull(shortestPath)) {
            throw new IllegalArgumentException("경로가 존재하지 않습니다.");
        }

        List<Station> vertexes = shortestPath.getVertexList();

        List<StationResponse> stationResponses = vertexes.stream()
                .map(StationResponse::new)
                .collect(toList());

        int shortestDistance = (int) shortestPath.getWeight();
        Fare fare = new Fare(shortestDistance);
        return new PathResponse(stationResponses, shortestDistance, fare.calculate());
    }

    private GraphPath getPath(Station source, Station target, DijkstraShortestPath dijkstraShortestPath) {
        try {
            return dijkstraShortestPath.getPath(source, target);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("노선에 등록되지 않는 지하철역입니다.");
        }
    }
}
