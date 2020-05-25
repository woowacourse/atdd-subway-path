package wooteco.subway.admin.service;

import org.springframework.stereotype.Service;
import wooteco.subway.admin.domain.*;
import wooteco.subway.admin.dto.LineDetailResponse;
import wooteco.subway.admin.dto.LineRequest;
import wooteco.subway.admin.dto.LineStationCreateRequest;
import wooteco.subway.admin.dto.WholeSubwayResponse;
import wooteco.subway.admin.exception.LineNotFoundException;
import wooteco.subway.admin.exception.StationNotFoundException;
import wooteco.subway.admin.repository.LineRepository;
import wooteco.subway.admin.repository.StationRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LineService {
    private LineRepository lineRepository;
    private StationRepository stationRepository;
    private GraphService graphService;

    public LineService(LineRepository lineRepository, StationRepository stationRepository, GraphService graphService) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
        this.graphService = graphService;
        graphService.initialize(Stations.of(findAllStations()), LineStations.from(showLines()));
    }

    public Line save(Line line) {
        return lineRepository.save(line);
    }

    public List<Line> showLines() {
        return lineRepository.findAll();
    }

    public void updateLine(Long id, LineRequest request) {
        Line persistLine = lineRepository.findById(id)
                .orElseThrow(() -> new LineNotFoundException(id));
        persistLine.update(request.toLine());
        lineRepository.save(persistLine);
    }

    public void deleteLineById(Long id) {
        lineRepository.deleteById(id);
    }

    public void addLineStation(Long id, LineStationCreateRequest request) {
        Line line = lineRepository.findById(id).orElseThrow(() -> new LineNotFoundException(id));
        LineStation lineStation = new LineStation(request.getPreStationId(), request.getStationId(), request.getDistance(), request.getDuration());
        line.addLineStation(lineStation);

        lineRepository.save(line);
        graphService.initialize(Stations.of(findAllStations()), LineStations.from(showLines()));
    }

    public void removeLineStation(Long lineId, Long stationId) {
        Line line = lineRepository.findById(lineId).orElseThrow(() -> new LineNotFoundException(lineId));
        line.removeLineStationById(stationId);
        lineRepository.save(line);
        graphService.initialize(Stations.of(findAllStations()), LineStations.from(showLines()));
    }

    public LineDetailResponse findLineWithStationsById(Long id) {
        Line line = lineRepository.findById(id).orElseThrow(() -> new LineNotFoundException(id));
        List<Station> stations = stationRepository.findAllById(line.getLineStationsId());
        return LineDetailResponse.of(line, Stations.of(stations));
    }

    public WholeSubwayResponse wholeLines() {
        Lines lines = Lines.of(showLines());
        List<Long> wholeStationIds = lines.getWholeStationIds();
        Stations wholeStations = Stations.of(stationRepository.findAllById(wholeStationIds));

        List<LineDetailResponse> lineDetailResponses = getLineDetailResponses(lines, wholeStations);
        return WholeSubwayResponse.of(lineDetailResponses);
    }

    private List<LineDetailResponse> getLineDetailResponses(Lines lines, Stations wholeStations) {
        return lines.getLines()
                .stream()
                .map(line -> LineDetailResponse.of(line, line.getMatchingStations(wholeStations)))
                .collect(Collectors.toList());
    }

    public Station findStationWithName(String name) {
        return stationRepository.findByName(name)
                .orElseThrow(() -> new StationNotFoundException(name));
    }

    public List<Station> findAllStations() {
        return stationRepository.findAll();
    }
}
