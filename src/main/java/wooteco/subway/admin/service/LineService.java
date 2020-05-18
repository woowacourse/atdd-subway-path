package wooteco.subway.admin.service;

import org.springframework.stereotype.Service;
import wooteco.subway.admin.domain.Line;
import wooteco.subway.admin.domain.LineStation;
import wooteco.subway.admin.domain.Station;
import wooteco.subway.admin.dto.request.LineRequest;
import wooteco.subway.admin.dto.request.LineStationCreateRequest;
import wooteco.subway.admin.dto.response.LineDetailResponse;
import wooteco.subway.admin.dto.response.WholeSubwayResponse;
import wooteco.subway.admin.exception.NoLineExistException;
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

    public WholeSubwayResponse showLinesDetail() {
        List<Line> lines = lineRepository.findAll();
        List<Long> stationIds = lines.stream()
                .flatMap(line -> line.getStations().stream())
                .map(LineStation::getStationId)
                .collect(Collectors.toList());

        List<Station> stations = stationRepository.findAllById(stationIds);

        List<LineDetailResponse> lineDetailResponses = lines.stream()
                .map(line -> LineDetailResponse.of(line, mapStations(line.getLineStationsId(), stations)))
                .collect(Collectors.toList());

        return WholeSubwayResponse.of(lineDetailResponses);
    }

    private List<Station> mapStations(List<Long> lineStationsId, List<Station> stations) {
        return stations.stream()
                .filter(station -> lineStationsId.contains(station.getId()))
                .collect(Collectors.toList());
    }
}
