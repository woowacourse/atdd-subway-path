package wooteco.subway.admin.service;

import org.springframework.stereotype.Service;
import wooteco.subway.admin.domain.Line;
import wooteco.subway.admin.domain.LineStation;
import wooteco.subway.admin.domain.Station;
import wooteco.subway.admin.dto.request.LineRequest;
import wooteco.subway.admin.dto.request.LineStationCreateRequest;
import wooteco.subway.admin.dto.response.LineDetailResponse;
import wooteco.subway.admin.dto.response.WholeSubwayResponse;
import wooteco.subway.admin.repository.LineRepository;
import wooteco.subway.admin.repository.StationRepository;

import java.util.List;
import java.util.Map;
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
        LineStation lineStation = new LineStation(request.getPreStationId(), request.getStationId(),
                request.getDistance(), request.getDuration());
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
        return LineDetailResponse.of(line, stations);
    }

    public WholeSubwayResponse wholeLines() {
        List<Line> lines = lineRepository.findAll();
        Map<Long, Station> stations = stationRepository.findAll()
                .stream()
                .collect(Collectors.toMap(Station::getId, station -> station));
        List<LineDetailResponse> responses = lines.stream()
                .map(line -> getLineDetailResponse(stations, line))
                .collect(Collectors.toList());
        return WholeSubwayResponse.of(responses);
    }

    private LineDetailResponse getLineDetailResponse(Map<Long, Station> stations, Line line) {
        List<Long> stationIds = line.getLineStationsId();
        List<Station> stationsList = stationIds.stream()
                .map(stations::get)
                .collect(Collectors.toList());
        return LineDetailResponse.of(line, stationsList);
    }
}
