package wooteco.subway.admin.service;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import wooteco.subway.admin.domain.Line;
import wooteco.subway.admin.domain.LineStation;
import wooteco.subway.admin.domain.Station;
import wooteco.subway.admin.dto.PathResponse;
import wooteco.subway.admin.repository.LineRepository;
import wooteco.subway.admin.repository.StationRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class PathService {
    private LineRepository lineRepository;
    private StationRepository stationRepository;

    public PathService(LineRepository lineRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    public PathResponse calculatePath(String source, String target) {
//        List<Station> stations = new ArrayList<>();
//        stations.add(new Station(1L, "양재시민의숲역"));
//        stations.add(new Station(2L, "양재역"));
//        stations.add(new Station(3L, "강남역"));
//        stations.add(new Station(4L, "역삼역"));
//        stations.add(new Station(5L, "선릉역"));
//        PathResponse pathResponse = new PathResponse(stations, 40, 40);
//        return pathResponse;

        List<Station> stations = stationRepository.findAll();
        List<Line> lines = lineRepository.findAll();
        Long sourceId = stations
                .stream()
                .filter(station -> station.getName().equals(source))
                .map(Station::getId)
                .findFirst()
                .orElseThrow(RuntimeException::new);
        Long targetId = stations
                .stream()
                .filter(station -> station.getName().equals(target))
                .map(Station::getId)
                .findFirst()
                .orElseThrow(RuntimeException::new);
        WeightedMultigraph<Long, DefaultWeightedEdge> graph = new WeightedMultigraph(DefaultWeightedEdge.class);
        for(Station station : stations) {
            graph.addVertex(station.getId());
        }
        for(Line line : lines) {
            for(LineStation lineStation : line.getStations()) {
                if(Objects.isNull(lineStation.getPreStationId())) {
                    continue;
                }
                graph.setEdgeWeight(graph.addEdge(lineStation.getPreStationId(),lineStation.getStationId()), lineStation.getDistance());
            }
        }
        //----

        DijkstraShortestPath<Long, DefaultWeightedEdge> dijkstraShortestPath = new DijkstraShortestPath(graph);
        List<Long> shortestPath = dijkstraShortestPath.getPath(sourceId, targetId).getVertexList();
        System.out.println(shortestPath);

        List<Station> result = new ArrayList<>();

        for(Long id : shortestPath) {
            for(Station station : stations) {
                if(station.getId() == id) {
                    result.add(station);
                    break;
                }
            }
        }

        PathResponse pathResponse = new PathResponse(result, 40, 40);
        return pathResponse;
    }
}
