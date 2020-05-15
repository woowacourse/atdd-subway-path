package wooteco.subway.admin.service;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Service;
import wooteco.subway.admin.domain.Line;
import wooteco.subway.admin.domain.LineStation;
import wooteco.subway.admin.domain.Station;
import wooteco.subway.admin.dto.SearchPathResponse;
import wooteco.subway.admin.repository.LineRepository;
import wooteco.subway.admin.repository.LineStationRepository;
import wooteco.subway.admin.repository.StationRepository;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PathService {
    private LineRepository lineRepository;
    private StationRepository stationRepository;
    private LineStationRepository lineStationRepository;

    public PathService(LineRepository lineRepository, StationRepository stationRepository, LineStationRepository lineStationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
        this.lineStationRepository = lineStationRepository;
    }

    public SearchPathResponse searchPath(String startStationName, String targetStationName, String type) {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph = makeGraph(type);
        DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graph);

        Station startStation = stationRepository.findByName(startStationName)
                .orElseThrow(() -> new IllegalArgumentException("역이 존재하지 않습니다."));
        Station targetStation = stationRepository.findByName(targetStationName)
                .orElseThrow(() -> new IllegalArgumentException("역이 존재하지 않습니다."));

        GraphPath path = dijkstraShortestPath.getPath(startStation, targetStation);
        if (path == null) {
            throw new IllegalArgumentException("두 역이 연결되어있지 않습니다.");
        }

        List<Station> shortestPath = path.getVertexList();
        if (shortestPath.size() == 1) {
            throw new IllegalArgumentException("시작역과 도착역이 같습니다.");
        }

        int durationSum = calculateValueSum(shortestPath, "duration");
        int distanceSum = calculateValueSum(shortestPath, "distance");
        List<String> stationNames = shortestPath.stream()
                .map(o -> o.getName())
                .collect(Collectors.toList());

        return new SearchPathResponse(durationSum, distanceSum, stationNames);
    }

    private WeightedMultigraph<Station, DefaultWeightedEdge> makeGraph(String type) {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph(DefaultWeightedEdge.class);
        List<Line> lines = lineRepository.findAll();
        for (Line line : lines) {
            Set<LineStation> stations = line.getStations();

            Iterator<LineStation> lineStationIterator = stations.iterator();
            while (lineStationIterator.hasNext()) {
                LineStation lineStation = lineStationIterator.next();
                Station station = stationRepository.findById(lineStation.getStationId())
                        .orElseThrow(() -> new IllegalArgumentException("역이 존재하지 않습니다."));
                graph.addVertex(station);

                if (lineStation.getPreStationId() == null) {
                    continue;
                }

                Station preStation = stationRepository.findById(lineStation.getPreStationId())
                        .orElseThrow(() -> new IllegalArgumentException("이전역이 존재하지 않습니다."));
                graph.addVertex(preStation);

                if (type.equals("duration")) {
                    graph.setEdgeWeight(graph.addEdge(station, preStation), lineStation.getDuration());
                    graph.setEdgeWeight(graph.addEdge(preStation, station), lineStation.getDuration());
                    continue;
                }
                graph.setEdgeWeight(graph.addEdge(preStation, station), lineStation.getDistance());
                graph.setEdgeWeight(graph.addEdge(station, preStation), lineStation.getDistance());
            }
        }
        return graph;
    }

    private int calculateValueSum(List<Station> shortestPath, String type) {
        int valueSum = 0;
        for (int i = 1; i < shortestPath.size(); i++) {
            Station preStation = shortestPath.get(i - 1);
            Station station = shortestPath.get(i);
            int value = 0;

            //todo: exception message add
            if (type.equals("duration")) {
                value = lineStationRepository.findById(preStation.getId(), station.getId())
                        .orElseThrow(IllegalArgumentException::new)
                        .getDuration();
            }
            if (type.equals("distance")) {
                value = lineStationRepository.findById(preStation.getId(), station.getId())
                        .orElseThrow(IllegalArgumentException::new)
                        .getDistance();
            }
            valueSum += value;
        }
        return valueSum;
    }
}
