package wooteco.subway.admin.service;

import org.springframework.stereotype.Service;
import wooteco.subway.admin.domain.Line;
import wooteco.subway.admin.domain.LineStation;
import wooteco.subway.admin.domain.Lines;
import wooteco.subway.admin.domain.Stations;
import wooteco.subway.admin.dto.LineDetailResponse;
import wooteco.subway.admin.dto.LineRequest;
import wooteco.subway.admin.dto.LineStationCreateRequest;
import wooteco.subway.admin.dto.WholeSubwayResponse;
import wooteco.subway.admin.exception.LineNotFoundException;
import wooteco.subway.admin.repository.LineRepository;
import wooteco.subway.admin.repository.StationRepository;

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

    public Lines showLines() {
        return new Lines(lineRepository.findAll());
    }

    public void updateLine(Long id, LineRequest request) {
        Line persistLine = lineRepository.findById(id).orElseThrow(LineNotFoundException::new);
        persistLine.update(request.toLine());
        lineRepository.save(persistLine);
    }

    public void deleteLineById(Long id) {
        lineRepository.deleteById(id);
    }

    public void addLineStation(Long id, LineStationCreateRequest request) {
        Line line = lineRepository.findById(id).orElseThrow(LineNotFoundException::new);
        LineStation lineStation = new LineStation(request.getPreStationId(), request.getStationId(), request.getDistance(), request.getDuration());
        line.addLineStation(lineStation);

        lineRepository.save(line);
    }

    public void removeLineStation(Long lineId, Long stationId) {
        Line line = lineRepository.findById(lineId).orElseThrow(LineNotFoundException::new);
        line.removeLineStationById(stationId);
        lineRepository.save(line);
    }

    public LineDetailResponse findLineWithStationsById(Long id) {
        Line line = lineRepository.findById(id).orElseThrow(LineNotFoundException::new);
        Stations stations = new Stations(stationRepository.findAllById(line.getLineStationsId()));
        return LineDetailResponse.of(line, stations);
    }

    public WholeSubwayResponse wholeLines() {
        Lines lines = new Lines(lineRepository.findAll());
        List<Long> stationIds = lines.getLineStationsId();
        Stations stations = new Stations(stationRepository.findAllById(stationIds));

        List<LineDetailResponse> lineDetailResponses = lines.getLines()
                .stream()
                .map(it -> LineDetailResponse.of(it, mapStations(it.getLineStationsId(), stations)))
                .collect(Collectors.toList());

        return WholeSubwayResponse.of(lineDetailResponses);
    }

    private Stations mapStations(List<Long> lineStationsId, Stations stations) {
        return stations.getStations()
                .stream()
                .filter(it -> lineStationsId.contains(it.getId()))
                .collect(Collectors.collectingAndThen(Collectors.toList(), Stations::new));
    }
}
