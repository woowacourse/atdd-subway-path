package wooteco.subway.admin.path.service;

import static java.util.stream.Collectors.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.springframework.stereotype.Service;

import wooteco.subway.admin.common.exception.InvalidSubwayPathException;
import wooteco.subway.admin.line.domain.lineStation.LineStation;
import wooteco.subway.admin.line.repository.lineStation.LineStationRepository;
import wooteco.subway.admin.path.domain.ShortestPathType;
import wooteco.subway.admin.path.domain.SubwayGraphFactory;
import wooteco.subway.admin.path.domain.SubwayShortestPath;
import wooteco.subway.admin.path.domain.SubwayWeightedEdge;
import wooteco.subway.admin.path.service.dto.PathRequest;
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

    public PathResponse searchPath(PathRequest request) {
        final List<LineStation> lineStations = lineStationRepository.findAll();
        final Map<Long, Station> stations = getStationsWithId();

        final Station source = stations.get(request.getSource());
        final Station target = stations.get(request.getTarget());
        validateRequest(source, target);

        final ShortestPathType type = ShortestPathType.of(request.getType());
        final GraphPath<Station, SubwayWeightedEdge> graphPath = DijkstraShortestPath
            .findPathBetween(SubwayGraphFactory.createGraphBy(type, stations, lineStations), source, target);

        return getPathResponse(graphPath, type);
    }

    private void validateRequest(final Station source, final Station target) {
        if (Objects.isNull(source)) {
            throw new InvalidSubwayPathException("출발역이 존재하지 않습니다.");
        }

        if (Objects.isNull(target)) {
            throw new InvalidSubwayPathException("도착역이 존재하지 않습니다.");
        }
    }

    private Map<Long, Station> getStationsWithId() {
        return stationRepository.findAll()
                                .stream()
                                .collect(toMap(Station::getId, station -> station));
    }

    private PathResponse getPathResponse(final GraphPath<Station, SubwayWeightedEdge> graphPath,
        final ShortestPathType type) {
        final SubwayShortestPath subwayShortestPath = new SubwayShortestPath(graphPath);

        final List<StationResponse> stations =
            StationResponse.listOf(subwayShortestPath.getPathStations());
        final int weight = subwayShortestPath.getWeight();
        final int subWeight = subwayShortestPath.getSubWeight();

        if (type.isDistanceType()) {
            return new PathResponse(stations, weight, subWeight);
        }
        return new PathResponse(stations, subWeight, weight);
    }

}
