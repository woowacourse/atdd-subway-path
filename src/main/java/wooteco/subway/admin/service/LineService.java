package wooteco.subway.admin.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import wooteco.subway.admin.domain.Line;
import wooteco.subway.admin.domain.LineStation;
import wooteco.subway.admin.domain.Station;
import wooteco.subway.admin.dto.LineDetailResponse;
import wooteco.subway.admin.dto.WholeSubwayResponse;
import wooteco.subway.admin.repository.LineRepository;
import wooteco.subway.admin.repository.StationRepository;

@Service
public class LineService {
    public static final String NO_EXIST_LINE = "존재하지 않는 노선입니다.";
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

    public void updateLine(Long id, Line line) {
        Line persistLine = lineRepository.findById(id)
            .orElseThrow(() -> new RuntimeException(NO_EXIST_LINE));
        persistLine.update(line);
        lineRepository.save(persistLine);
    }

    public void deleteLineById(Long id) {
        lineRepository.deleteById(id);
    }

    public void addLineStation(Long id, LineStation lineStation) {
        Line line = lineRepository.findById(id)
            .orElseThrow(() -> new RuntimeException(NO_EXIST_LINE));
        line.addLineStation(lineStation);
        lineRepository.save(line);
    }

    public void removeLineStation(Long lineId, Long stationId) {
        Line line = lineRepository.findById(lineId)
            .orElseThrow(() -> new RuntimeException(NO_EXIST_LINE));
        line.removeLineStationById(stationId);
        lineRepository.save(line);
    }

    public LineDetailResponse findLineWithStationsById(Long id) {
        Line line = lineRepository.findById(id)
            .orElseThrow(() -> new RuntimeException(NO_EXIST_LINE));
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

    public Station addStation(Station station) {
        return stationRepository.save(station);
    }

    public List<Station> showStations() {
        return stationRepository.findAll();
    }

    public Station findStationByName(String name) {
        return stationRepository.findByName(name)
            .orElseThrow(() -> new RuntimeException("존재하지 않는 역입니다."));
    }

    public List<Station> findAllStationsByIds(List<Long> stationIds) {
        return stationRepository.findAllById(stationIds);
    }

    public void deleteStationById(Long id) {
        stationRepository.deleteById(id);
    }
}
