package wooteco.subway.admin.service;

import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import wooteco.subway.admin.domain.Line;
import wooteco.subway.admin.domain.LineStation;
import wooteco.subway.admin.domain.Station;
import wooteco.subway.admin.dto.LineDetailResponse;
import wooteco.subway.admin.dto.LineRequest;
import wooteco.subway.admin.dto.LineStationCreateRequest;
import wooteco.subway.admin.dto.WholeSubwayResponse;
import wooteco.subway.admin.repository.LineRepository;
import wooteco.subway.admin.repository.StationRepository;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
        return LineDetailResponse.of(line, stations);
    }

    // TODO: 구현하세요 :)
    public WholeSubwayResponse wholeLines() {
        List<Line> lines = lineRepository.findAll();
        Line line = lines.get(0);
        Line newLine = lines.get(1);

        List<Station> allStations = stationRepository.findAllById(new ArrayList());
        List<Station> lineStations = stationRepository.findAllById(line.getLineStationsId());
        List<Station> newLineStations = stationRepository.findAllById(newLine.getLineStationsId());

        return WholeSubwayResponse.of(Arrays.asList(LineDetailResponse.of(line, lineStations), LineDetailResponse.of(newLine, newLineStations)));
    }

    private LineDetailResponse createMockResponse() {
        Set<Station> stations = new HashSet();
        stations.add(new Station());
        stations.add(new Station());
        stations.add(new Station());
        return LineDetailResponse.of(new Line(), new ArrayList<>(stations));
    }
}
