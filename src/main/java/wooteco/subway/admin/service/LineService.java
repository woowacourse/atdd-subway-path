package wooteco.subway.admin.service;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import wooteco.subway.admin.domain.Line;
import wooteco.subway.admin.domain.LineStation;
import wooteco.subway.admin.domain.Path;
import wooteco.subway.admin.domain.Station;
import wooteco.subway.admin.dto.LineDetailResponse;
import wooteco.subway.admin.dto.LineRequest;
import wooteco.subway.admin.dto.LineStationCreateRequest;
import wooteco.subway.admin.dto.ShortestDistanceResponse;
import wooteco.subway.admin.dto.StationCreateRequest;
import wooteco.subway.admin.dto.StationResponse;
import wooteco.subway.admin.dto.WholeSubwayResponse;
import wooteco.subway.admin.repository.LineRepository;
import wooteco.subway.admin.repository.StationRepository;

@Service
public class LineService {
    private LineRepository lineRepository;
    private StationRepository stationRepository;
    private Path path;

    public LineService(LineRepository lineRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
        this.path = new Path();
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
        LineStation lineStation = new LineStation(request.getPreStationId(), request.getStationId(),
            request.getDistance(), request.getDuration());
        line.addLineStation(lineStation);

        if (Objects.isNull(request.getPreStationId())) {
            lineRepository.save(line);
            return;
        }

        List<Station> stations = stationRepository.findAllById(
            Arrays.asList(request.getPreStationId(), request.getStationId()));
        Station preStation = stations.get(0);
        Station station = stations.get(1);
        path.setDistanceWeight(preStation, station, request.getDistance());
        path.setDurationWeight(preStation, station, request.getDuration());

        lineRepository.save(line);
    }

    public void removeLineStation(Long lineId, Long stationId) {
        Line line = lineRepository.findById(lineId).orElseThrow(RuntimeException::new);

        LineStation targetLineStation = line.getStations().stream()
            .filter(it -> Objects.equals(it.getStationId(), stationId))
            .findFirst()
            .orElseThrow(RuntimeException::new);
        List<Station> stations = stationRepository.findAllById(
            Arrays.asList(targetLineStation.getPreStationId(), targetLineStation.getStationId()));
        Station preStation = stations.get(0);
        Station station = stations.get(1);
        path.removeDistanceEdge(preStation, station);
        path.removeDurationEdge(preStation, station);

        line.removeLineStationById(stationId);
        lineRepository.save(line);
    }

    public LineDetailResponse findLineWithStationsById(Long id) {
        Line line = lineRepository.findById(id).orElseThrow(RuntimeException::new);
        List<Station> stations = stationRepository.findAllById(line.getLineStationsId());
        return LineDetailResponse.of(line, stations);
    }

    public WholeSubwayResponse wholeLines() {
        List<Line> lines = lineRepository.findAll();
        List<LineDetailResponse> lineDetailResponses = lines.stream()
            .map(line -> findLineWithStationsById(line.getId()))
            .collect(Collectors.toList());
        return WholeSubwayResponse.of(lineDetailResponses);
    }

    public ShortestDistanceResponse searchShortestDistancePath(String source, String target) {
        Station sourceStation = stationRepository.findByName(source)
            .orElseThrow(RuntimeException::new);
        Station targetStation = stationRepository.findByName(target)
            .orElseThrow(RuntimeException::new);
        List<Station> stations = path.searchShortestDistancePath(sourceStation, targetStation);
        int distance = path.calculateDistance();
        int duration = path.calculateDuration();
        return new ShortestDistanceResponse(StationResponse.listOf(stations), distance, duration);
    }

    public Station addStation(StationCreateRequest view) {
        Station station = view.toStation();
        Station persistStation = stationRepository.save(station);
        path.addStation(persistStation);
        return persistStation;
    }

    public List<Station> showStations() {
        return stationRepository.findAll();
    }

    public void deleteStationById(Long id) {
        Station station = stationRepository.findById(id).orElseThrow(RuntimeException::new);
        path.deleteStation(station);
        stationRepository.deleteById(id);
    }
}
