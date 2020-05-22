package wooteco.subway.admin.service;

import org.springframework.stereotype.Service;
import wooteco.subway.admin.domain.*;
import wooteco.subway.admin.dto.request.LineRequest;
import wooteco.subway.admin.dto.request.LineStationCreateRequest;
import wooteco.subway.admin.dto.response.LineDetailResponse;
import wooteco.subway.admin.exception.NoLineExistException;
import wooteco.subway.admin.repository.LineRepository;
import wooteco.subway.admin.repository.StationRepository;

import java.util.List;

@Service
public class LineService {
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

    public void updateLine(Long id, LineRequest request) {
        Line persistLine = getLine(id);
        persistLine.update(request.toLine());
        lineRepository.save(persistLine);
    }

    public void deleteLineById(Long id) {
        lineRepository.deleteById(id);
    }

    public void addLineStation(Long id, LineStationCreateRequest request) {
        Line line = getLine(id);
        LineStation lineStation = new LineStation(
                request.getPreStationId(), request.getStationId(), request.getDistance(), request.getDuration()
        );
        line.addLineStation(lineStation);

        lineRepository.save(line);
    }

    public void removeLineStation(Long lineId, Long stationId) {
        Line line = getLine(lineId);
        line.removeLineStationById(stationId);
        lineRepository.save(line);
    }

    public LineDetailResponse findLineWithStationsById(Long id) {
        Line line = getLine(id);
        List<Station> stations = stationRepository
                .findAllById(line.getLineStationsId());
        return LineDetailResponse.of(line, stations);
    }

    private Line getLine(Long id) {
        return lineRepository.findById(id)
                .orElseThrow(NoLineExistException::new);
    }

    public  List<LineDetail> showLineDetails() {
        Lines lines = new Lines(lineRepository.findAll());
        List<Long> stationIds = lines.toLineStationIds();
        Stations stations = new Stations(stationRepository.findAllById(stationIds));

        return lines.toLineDetails(stations);
    }
}
