package wooteco.subway.admin.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import wooteco.subway.admin.domain.Line;
import wooteco.subway.admin.domain.LineStation;
import wooteco.subway.admin.domain.Station;
import wooteco.subway.admin.dto.LineDetailResponse;
import wooteco.subway.admin.dto.LineRequest;
import wooteco.subway.admin.dto.LineResponse;
import wooteco.subway.admin.dto.LineStationCreateRequest;
import wooteco.subway.admin.repository.LineRepository;
import wooteco.subway.admin.repository.StationRepository;

@Service
public class LineService {
    private LineRepository lineRepository;
    private StationRepository stationRepository;

    public LineService(LineRepository lineRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    public LineResponse save(LineRequest lineRequest) {
        Line line = lineRequest.toLine();
        Line persistLine = lineRepository.save(line);

        return LineResponse.of(persistLine);
    }

    public List<LineResponse> showLines() {
        List<Line> lines = lineRepository.findAll();
        return LineResponse.listOf(lines);
    }

    public void updateLine(Long id, LineRequest request) {
        Line persistLine = lineRepository.findById(id)
            .orElseThrow(() -> new NoSuchElementException("노선을 찾을 수 없습니다."));
        persistLine.update(request.toLine());
        lineRepository.save(persistLine);
    }

    public void deleteLineById(Long lineId) {
        lineRepository.deleteById(lineId);
    }

    public void addLineStation(Long lineId, LineStationCreateRequest request) {
        Line line = findBy(lineId);
        LineStation lineStation = new LineStation(request.getPreStationId(), request.getStationId(), request.getDistance(), request.getDuration());
        line.addLineStation(lineStation);

        lineRepository.save(line);
    }

    public Line findBy(Long lineId){
        return lineRepository.findById(lineId)
            .orElseThrow(() -> new NoSuchElementException("노선을 찾을 수 없습니다."));
    }

    public void removeLineStation(Long lineId, Long stationId) {
        Line line = findBy(lineId);
        line.removeLineStationById(stationId);
        lineRepository.save(line);
    }

    public LineDetailResponse findLineWithStationsById(Long lineId) {
        Line line = findBy(lineId);
        List<Long> lineStationsIds = line.getLineStationsIds();
        return LineDetailResponse.of(line, sortBySubwayRule(lineStationsIds));
    }

    public List<LineDetailResponse> wholeLines() {
        List<Line> lines = lineRepository.findAll();

        return lines.stream()
            .map(line -> LineDetailResponse.of(line, sortBySubwayRule(line.getLineStationsIds())))
            .collect(Collectors.toList());
    }

    public List<Station> sortBySubwayRule(List<Long> lineStationsIds) {
        List<Station> stations = stationRepository.findAllById(lineStationsIds);

        return lineStationsIds.stream()
            .map(lineStationsId -> stations.stream()
                .filter(station -> lineStationsId.equals(station.getId()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 역입니다")))
            .collect(Collectors.toList());
    }
}
