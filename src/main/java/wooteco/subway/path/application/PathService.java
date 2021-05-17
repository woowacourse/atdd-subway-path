package wooteco.subway.path.application;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Service;
import wooteco.subway.line.application.LineService;
import wooteco.subway.line.domain.PathSection;
import wooteco.subway.path.dto.PathResponse;
import wooteco.subway.path.exception.PathException;
import wooteco.subway.station.application.StationService;
import wooteco.subway.station.domain.Station;
import wooteco.subway.station.dto.StationResponse;

@Service
public class PathService {

    private final StationService stationService;
    private final LineService lineService;

    public PathService(StationService stationService, LineService lineService) {
        this.stationService = stationService;
        this.lineService = lineService;
    }

    public PathResponse findStationInPath(Long sourceStationId, Long targetStationId) {
        Map<Long, Station> stationMap = stationService.findAllInMap();
        validateEachStationIsExist(stationMap, sourceStationId, targetStationId);
        List<PathSection> sections = lineService.findAllSections();

        GraphPath<Long, DefaultWeightedEdge> path = calculateShortestPath(sourceStationId, targetStationId, stationMap, sections);

        return createPathResponse(stationMap, path);
    }

    private void validateEachStationIsExist(Map<Long, Station> stationMap, Long sourceStationId, Long targetStationId) {
        if (!stationMap.containsKey(sourceStationId) || !stationMap.containsKey(targetStationId)) {
            throw new PathException("경로에 입력한 역이 존재하지 않습니다.");
        }
    }

    private GraphPath<Long, DefaultWeightedEdge> calculateShortestPath(Long sourceStationId, Long targetStationId, Map<Long, Station> stationMap, List<PathSection> sections) {
        WeightedMultigraph<Long, DefaultWeightedEdge> stationGraph = new WeightedMultigraph<>(DefaultWeightedEdge.class);

        stationMap.keySet().forEach(stationGraph::addVertex);
        sections.forEach(section -> stationGraph.setEdgeWeight(
            stationGraph.addEdge(section.getUpStationId(), section.getDownStationId()),
            section.getDistance()));

        DijkstraShortestPath<Long, DefaultWeightedEdge> dijkstraShortestPath = new DijkstraShortestPath<>(stationGraph);
        return dijkstraShortestPath.getPath(sourceStationId, targetStationId);
    }

    private PathResponse createPathResponse(Map<Long, Station> stationMap,
        GraphPath<Long, DefaultWeightedEdge> path) {
        validatePathIsExist(path);
        List<StationResponse> stationResponses = path.getVertexList()
            .stream()
            .map(stationMap::get)
            .map(StationResponse::of)
            .collect(Collectors.toList());
        int totalDistance = (int) path.getWeight();

        return new PathResponse(stationResponses, totalDistance);
    }

    private void validatePathIsExist(GraphPath<Long, DefaultWeightedEdge> path) {
        if(Objects.isNull(path)) {
            throw new PathException("두 역 사이에 존재하는 경로가 없습니다.");
        }
    }
}
