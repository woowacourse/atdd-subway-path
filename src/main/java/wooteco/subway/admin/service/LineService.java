package wooteco.subway.admin.service;

import java.util.List;

import org.springframework.stereotype.Service;

import wooteco.subway.admin.domain.Line;
import wooteco.subway.admin.domain.LineStation;
import wooteco.subway.admin.domain.Station;
import wooteco.subway.admin.exception.NoExistLineException;
import wooteco.subway.admin.exception.NoExistStationException;
import wooteco.subway.admin.repository.LineRepository;
import wooteco.subway.admin.repository.StationRepository;

@Service
public class LineService {
    public static final String NO_EXIST_LINE = "존재하지 않는 노선입니다.";
    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

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

    public Line findLineById(Long id) {
        return lineRepository.findById(id)
            .orElseThrow(() -> new NoExistLineException(NO_EXIST_LINE));
    }

    public void updateLine(Long id, Line line) {
        Line persistLine = lineRepository.findById(id)
            .orElseThrow(() -> new NoExistLineException(NO_EXIST_LINE));
        persistLine.update(line);
        lineRepository.save(persistLine);
    }

    public void deleteLineById(Long id) {
        lineRepository.deleteById(id);
    }

    public void addLineStation(Long id, LineStation lineStation) {
        Line line = lineRepository.findById(id)
            .orElseThrow(() -> new NoExistLineException(NO_EXIST_LINE));
        line.addLineStation(lineStation);
        lineRepository.save(line);
    }

    public void removeLineStation(Long lineId, Long stationId) {
        Line line = lineRepository.findById(lineId)
            .orElseThrow(() -> new NoExistLineException(NO_EXIST_LINE));
        line.removeLineStationById(stationId);
        lineRepository.save(line);
    }

    public Station addStation(Station station) {
        return stationRepository.save(station);
    }

    public List<Station> showStations() {
        return stationRepository.findAll();
    }

    public Station findStationByName(String name) {
        return stationRepository.findByName(name)
            .orElseThrow(() -> new NoExistStationException("존재하지 않는 역입니다."));
    }

    public List<Station> findAllStationsByIds(List<Long> stationIds) {
        return stationRepository.findAllById(stationIds);
    }

    public void deleteStationById(Long id) {
        stationRepository.deleteById(id);
    }
}
