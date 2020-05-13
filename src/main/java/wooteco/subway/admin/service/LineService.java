package wooteco.subway.admin.service;

import org.jgrapht.Graphs;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Service;
import wooteco.subway.admin.domain.Line;
import wooteco.subway.admin.domain.LineStation;
import wooteco.subway.admin.domain.Station;
import wooteco.subway.admin.dto.*;
import wooteco.subway.admin.repository.LineRepository;
import wooteco.subway.admin.repository.StationRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LineService {
    private LineRepository lineRepository;
    private StationRepository stationRepository;

    public LineService(LineRepository lineRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    public Line save(Line line) {
        return lineRepository.save(line);
    }

    public List<Line> showLines() {
        return lineRepository.findAll();
    }

    public void updateLine(Long id, LineRequest request) {
        Line persistLine = lineRepository.findById(id).orElseThrow(RuntimeException::new);
        persistLine.update(request.toLine());
        lineRepository.save(persistLine);
    }

    public void deleteLineById(Long id) {
        lineRepository.deleteById(id);
    }

    public void addLineStation(Long id, LineStationCreateRequest request) {
        Line line = lineRepository.findById(id).orElseThrow(RuntimeException::new);
        LineStation lineStation = new LineStation(request.getPreStationId(), request.getStationId(), request.getDistance(), request.getDuration());
        line.addLineStation(lineStation);

        lineRepository.save(line);
    }

    public void removeLineStation(Long lineId, Long stationId) {
        Line line = lineRepository.findById(lineId).orElseThrow(RuntimeException::new);
        line.removeLineStationById(stationId);
        lineRepository.save(line);
    }

    public LineDetailResponse findLineWithStationsById(Long id) {
        Line line = lineRepository.findById(id).orElseThrow(RuntimeException::new);
        List<Station> stations = stationRepository.findAllById(line.getLineStationsId());

        // Station id
        List<Station> orderedStations = new ArrayList<>();
        for (Long stationId : line.getLineStationsId()) {
            stations.stream()
                    .filter(station -> station.getId().equals(stationId))
                    .findAny()
                    .ifPresent(orderedStations::add);
        }
        return LineDetailResponse.of(line, orderedStations);
    }

    public List<LineDetailResponse> findDetailLines() {
        List<Line> lines = lineRepository.findAll();
        List<List<Station>> stations = lines.stream()
                .map(line -> stationRepository.findAllById(line.getLineStationsId()))
                .collect(Collectors.toList());

        List<LineDetailResponse> response = new ArrayList<>();
        for (int i = 0; i < lines.size(); i++) {
            response.add(LineDetailResponse.of(lines.get(i), stations.get(i)));
        }
        return response;
    }

    public PathResponse findShortestDistancePath(PathRequest request) {
        // 모든 라인을 찾는다.
        List<Line> lines = lineRepository.findAll();

        // 모든 라인에 해당하는 그래프를 만든다.
        WeightedMultigraph<Long, DefaultWeightedEdge> totalGraph
                = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        lines.forEach(line ->
                Graphs.addGraph(totalGraph, line.createDistanceGraph()));

        // 모든 라인에서 sourceId -> targetId에 해당하는 경로를 찾는다. 거리도 찾는다.
        Long sourceId = request.getSourceId();
        Long targetId = request.getTargetId();

        DijkstraShortestPath dijkstraShortestPath
                = new DijkstraShortestPath(totalGraph);
        List<Long> shortestPath
                = dijkstraShortestPath.getPath(sourceId, targetId).getVertexList(); // stationId
        // a id -> b id -> c id

        List<Station> pathStations = stationRepository.findAllById(shortestPath);
        List<String> names = new ArrayList<>();

        for (Long id : shortestPath) {
            pathStations.stream()
                    .filter(station -> station.getId().equals(id))
                    .map(Station::getName)
                    .findAny()
                    .ifPresent(names::add);
        }

        // 최단 거리 구하기
        double distance = dijkstraShortestPath.getPath(sourceId, targetId).getWeight();

        // 최단 거리의 경로에 따른 최소 시간 구하기
        // preStationId, stationId, distance --> duration
        List<LineStation> lineStations = lines.stream()
                .map(Line::getStations)
                .flatMap(List::stream)
                .collect(Collectors.toList());

        int timeSum = 0;
        for (LineStation lineStation : lineStations) {
            for (int i = 0; i < shortestPath.size() - 1; i++) {
                Long preStationId = shortestPath.get(i);
                Long stationId = shortestPath.get(i + 1);

                if (preStationId.equals(lineStation.getPreStationId())
                        && stationId.equals(lineStation.getStationId())) {
                    timeSum += lineStation.getDuration();
                }
            }
        }
//        lineStations.stream()
//                .filter(lineStation -> sourceId.equals(lineStation.getPreStationId()))
//                .filter(lineStation -> targetId.equals(lineStation.getStationId()))
//                .filter(lineStation -> )
        return new PathResponse((int) distance, timeSum, names);
    }
}
