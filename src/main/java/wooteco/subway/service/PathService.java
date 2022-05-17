package wooteco.subway.service;

import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.stream.Collectors;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import wooteco.subway.dao.SectionDao;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Sections;
import wooteco.subway.domain.Station;
import wooteco.subway.dto.LineResponse;
import wooteco.subway.dto.PathResponse;
import wooteco.subway.dto.StationResponse;

public class PathService {

    private final StationService stationService;
    private final LineService lineService;
    private final SectionDao sectionDao;

    public PathService(StationService stationService, LineService lineService, SectionDao sectionDao) {
        this.stationService = stationService;
        this.lineService = lineService;
        this.sectionDao = sectionDao;
    }

    public PathResponse findPath(Long sourceId, Long targetId, int age) {
        Station source = stationService.findById(sourceId).toStation();
        Station target = stationService.findById(targetId).toStation();

        List<Long> lineIds = lineService.findAll()
                .stream()
                .map(LineResponse::getId)
                .collect(Collectors.toList());

        WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph(DefaultWeightedEdge.class);

        for (Long lineId : lineIds) {
            fillStationsGraph(graph, lineId);
        }

        DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graph);
        GraphPath<Station, DefaultWeightedEdge> shortestPath = dijkstraShortestPath.getPath(source, target);
        List<Station> vertexes = shortestPath.getVertexList();

        List<StationResponse> stationResponses = vertexes.stream()
                .map(StationResponse::new)
                .collect(toList());

        return new PathResponse(stationResponses, (int) shortestPath.getWeight());
    }

    private void fillStationsGraph(WeightedMultigraph<Station, DefaultWeightedEdge> graph, Long lineId) {
        Sections sections = new Sections(sectionDao.findByLineId(lineId));
        for (Station station : sections.getStations()) {
            graph.addVertex(station);
        }
        for (Section section : sections.getValue()) {
            graph.setEdgeWeight(graph.addEdge(section.getUpStation(), section.getDownStation()), section.getDistance());
        }
    }
}
