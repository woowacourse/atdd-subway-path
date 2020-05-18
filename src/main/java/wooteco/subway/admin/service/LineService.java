package wooteco.subway.admin.service;

import org.springframework.stereotype.Service;
import wooteco.subway.admin.domain.Line;
import wooteco.subway.admin.domain.Edge;
import wooteco.subway.admin.domain.Station;
import wooteco.subway.admin.dto.LineDetailResponse;
import wooteco.subway.admin.dto.LineRequest;
import wooteco.subway.admin.dto.EdgeCreateRequest;
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

    public List<Line> showLines() {
        return lineRepository.findAll();
    }

    public void updateLine(Long id, LineRequest request) {
        Line persistLine = lineRepository.findById(id).orElseThrow(LineNotFoundException::new);
        persistLine.update(request.toLine());
        lineRepository.save(persistLine);
    }

    public void deleteLineById(Long id) {
        lineRepository.deleteById(id);
    }

    public void addEdge(Long id, EdgeCreateRequest request) {
        Line line = lineRepository.findById(id).orElseThrow(LineNotFoundException::new);
        Edge edge = new Edge(request.getPreStationId(), request.getStationId(), request.getDistance(), request.getDuration());
        line.addEdge(edge);

        lineRepository.save(line);
    }

    public void removeEdge(Long lineId, Long stationId) {
        Line line = lineRepository.findById(lineId).orElseThrow(LineNotFoundException::new);
        line.removeEdgeById(stationId);
        lineRepository.save(line);
    }

    public LineDetailResponse findLineWithStationsById(Long id) {
        Line line = lineRepository.findById(id).orElseThrow(LineNotFoundException::new);
        List<Station> stations = stationRepository.findAllById(line.getEdgesId());
        return LineDetailResponse.of(line, stations);
    }

    public WholeSubwayResponse wholeLines() {
        List<Line> lines = lineRepository.findAll();
        List<Long> stationIds = lines.stream()
                .flatMap(it -> it.getEdges().stream())
                .map(it -> it.getStationId())
                .collect(Collectors.toList());

        List<Station> stations = stationRepository.findAllById(stationIds);

        List<LineDetailResponse> lineDetailResponses = lines.stream()
                .map(it -> LineDetailResponse.of(it, mapStations(it.getEdgesId(), stations)))
                .collect(Collectors.toList());

        return WholeSubwayResponse.of(lineDetailResponses);
    }

    private List<Station> mapStations(List<Long> edgesId, List<Station> stations) {
        return stations.stream()
                .filter(it -> edgesId.contains(it.getId()))
                .collect(Collectors.toList());
    }
}
