package wooteco.subway.admin.service;

import org.jgrapht.Graphs;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Service;
import wooteco.subway.admin.domain.Line;
import wooteco.subway.admin.domain.LineStation;
import wooteco.subway.admin.domain.Station;
import wooteco.subway.admin.dto.LineDetailResponse;
import wooteco.subway.admin.dto.LineRequest;
import wooteco.subway.admin.dto.LineStationCreateRequest;
import wooteco.subway.admin.dto.PathResponse;
import wooteco.subway.admin.repository.LineRepository;
import wooteco.subway.admin.repository.StationRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
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

    public LineDetailResponse findDetailLineById(Long id) {
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

    public PathResponse findShortestDistancePath(String sourceName, String targetName) {
        WeightedMultigraph<Long, DefaultWeightedEdge> totalGraph
                = new WeightedMultigraph<>(DefaultWeightedEdge.class);

        List<Line> lines = lineRepository.findAll();
        lines.forEach(line ->
                Graphs.addGraph(totalGraph, line.createDistanceGraph()));

        Long sourceId = stationRepository.findIdByName(sourceName)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 역입니다."));
        Long targetId = stationRepository.findIdByName(targetName)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 역입니다."));
        DijkstraShortestPath shortestPath = new DijkstraShortestPath(totalGraph);

        List<Long> pathStationIds = shortestPath.getPath(sourceId, targetId).getVertexList(); // stationId
        List<String> pathStationNames = stationRepository.findAllNameById(pathStationIds);

        // 최단 거리 구하기
        double distance = shortestPath.getPath(sourceId, targetId).getWeight();

        // 최단 거리의 경로에 따른 최소 시간 구하기
        // preStationId, stationId, distance --> duration
        List<LineStation> lineStations = lines.stream()
                .map(Line::getStations)
                .flatMap(List::stream)
                .collect(Collectors.toList());

        int timeSum = 0;

        for (int i = 0; i < pathStationIds.size() - 1; i++) {
            Long preStationId = pathStationIds.get(i);
            Long stationId = pathStationIds.get(i + 1);

            timeSum += lineStations.stream()
                    .filter(lineStation -> preStationId.equals(lineStation.getPreStationId()))
                    .filter(lineStation -> stationId.equals(lineStation.getStationId()))
                    .mapToInt(LineStation::getDuration)
                    .sum();
        }

        return new PathResponse((int) distance, timeSum, pathStationNames);
    }
}
