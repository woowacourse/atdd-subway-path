package wooteco.subway.path.application;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.jgrapht.GraphPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.springframework.stereotype.Service;
import wooteco.subway.line.application.LineService;
import wooteco.subway.line.domain.Path;
import wooteco.subway.line.domain.Section;
import wooteco.subway.path.dto.PathFindRequest;
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

    public PathResponse findStationInPath(PathFindRequest pathFindRequest) {
        Map<Long, Station> stationMap = stationService.findAllInMap();
        validateEachStationIsExist(stationMap, pathFindRequest);
        Path path = pathFindRequest.toPath();
        List<Section> sections = lineService.findAllSections();

        GraphPath<Long, DefaultWeightedEdge> graph = path
            .calculateShortestPath(stationMap, sections);

        return createPathResponse(stationMap, graph);
    }

    private void validateEachStationIsExist(Map<Long, Station> stationMap, PathFindRequest pathFindRequest) {
        if (!pathFindRequest.isAllStationInMap(stationMap)) {
            throw new PathException("경로에 입력한 역이 존재하지 않습니다.");
        }
    }

    private PathResponse createPathResponse(Map<Long, Station> stationMap, GraphPath<Long, DefaultWeightedEdge> path) {
        List<StationResponse> stationResponses = path.getVertexList()
            .stream()
            .map(stationMap::get)
            .map(StationResponse::of)
            .collect(Collectors.toList());
        int totalDistance = (int) path.getWeight();

        return new PathResponse(stationResponses, totalDistance);
    }
}
