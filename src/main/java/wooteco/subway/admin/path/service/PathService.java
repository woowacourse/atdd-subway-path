package wooteco.subway.admin.path.service;

import static java.util.stream.Collectors.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Service;

import wooteco.subway.admin.line.domain.lineStation.LineStation;
import wooteco.subway.admin.line.repository.lineStation.LineStationRepository;
import wooteco.subway.admin.path.domain.ShortestPathType;
import wooteco.subway.admin.path.domain.SubwayGraphFactory;
import wooteco.subway.admin.path.domain.SubwayShortestPath;
import wooteco.subway.admin.path.domain.SubwayWeightedEdge;
import wooteco.subway.admin.path.service.dto.PathInfoResponse;
import wooteco.subway.admin.path.service.dto.PathResponse;
import wooteco.subway.admin.station.domain.Station;
import wooteco.subway.admin.station.repository.StationRepository;
import wooteco.subway.admin.station.service.dto.StationResponse;

@Service
public class PathService {

    private final LineStationRepository lineStationRepository;
    private final StationRepository stationRepository;

    public PathService(LineStationRepository lineStationRepository,
        StationRepository stationRepository) {
        this.lineStationRepository = lineStationRepository;
        this.stationRepository = stationRepository;
    }

    public PathInfoResponse searchPath(Long source, Long target) {
        final List<LineStation> lineStations = lineStationRepository.findAll();
        final Map<Long, Station> stations = getStationsWithId();
        final Station sourceStation = stations.get(source);
        final Station targetStation = stations.get(target);

        final List<PathResponse> pathResponses =
            Arrays.stream(ShortestPathType.values())
                  .map(weightType -> SubwayGraphFactory.createGraphBy(weightType, stations, lineStations))
                  .map(graph -> getPathResponse(sourceStation, targetStation, graph))
                  .collect(toList());

        return PathInfoResponse.of(pathResponses);
    }

    private Map<Long, Station> getStationsWithId() {
        return stationRepository.findAll()
                                .stream()
                                .collect(toMap(Station::getId, station -> station));
    }

    private PathResponse getPathResponse(Station source, Station target,
        WeightedMultigraph<Station, SubwayWeightedEdge> subwayGraph) {
        final SubwayShortestPath subwayShortestPath = new SubwayShortestPath(new DijkstraShortestPath<>(subwayGraph));

        final List<StationResponse> stations = StationResponse
            .listOf(subwayShortestPath.getPathStations(source, target));
        final int weight = subwayShortestPath.getWeight(source, target);
        final int subWeight = subwayShortestPath.getSubWeight(source, target);

        return new PathResponse(stations, weight, subWeight);
    }
}
