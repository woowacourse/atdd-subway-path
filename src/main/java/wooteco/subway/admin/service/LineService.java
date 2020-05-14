package wooteco.subway.admin.service;

import static java.util.stream.Collectors.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.springframework.stereotype.Service;

import wooteco.subway.admin.domain.line.Line;
import wooteco.subway.admin.domain.line.LineStation;
import wooteco.subway.admin.domain.line.LineStations;
import wooteco.subway.admin.domain.line.path.EdgeWeightType;
import wooteco.subway.admin.domain.line.path.SubwayRoute;
import wooteco.subway.admin.domain.line.vo.PathInfo;
import wooteco.subway.admin.domain.station.Station;
import wooteco.subway.admin.dto.LineDetailResponse;
import wooteco.subway.admin.dto.LineRequest;
import wooteco.subway.admin.dto.LineStationCreateRequest;
import wooteco.subway.admin.dto.PathRequest;
import wooteco.subway.admin.dto.PathResponse;
import wooteco.subway.admin.dto.PathResponses;
import wooteco.subway.admin.dto.StationResponse;
import wooteco.subway.admin.dto.WholeSubwayResponse;
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

    public Long findIdByName(String name) {
        return stationRepository.findByName(name)
            .map(Station::getId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 Station 입니다."));
    }

    public PathResponses findPaths(PathRequest pathRequest) {
        PathInfo pathInfo = pathRequest.toPathInfo();
        Long departureId = findIdByName(pathInfo.getDepartureStationName());
        Long arrivalId = findIdByName(pathInfo.getArrivalStationName());

        LineStations lineStations = new LineStations(lineRepository.findAllLineStations());

        Map<String, PathResponse> responses = Arrays.stream(EdgeWeightType.values())
            .collect(toMap(EdgeWeightType::getName, edgeWeightType ->
                getPathResponse(getStationMap(),
                    getSubwayRoute(departureId, arrivalId, lineStations, edgeWeightType)))
            );

        return new PathResponses(responses);
    }

    private Map<Long, Station> getStationMap() {
        return stationRepository.findAll()
            .stream()
            .collect(toMap(Station::getId, Function.identity()));
    }

    private PathResponse getPathResponse(Map<Long, Station> stationMap, SubwayRoute subwayRoute) {
        List<Long> shortestPath = subwayRoute.getShortestPath();
        List<StationResponse> responses = shortestPath.stream()
            .map(stationMap::get)
            .map(StationResponse::of)
            .collect(toList());
        return new PathResponse(responses, subwayRoute.calculateTotalDuration(),
            subwayRoute.calculateTotalDistance());
    }

    private SubwayRoute getSubwayRoute(Long departureId, Long arrivalId, LineStations lineStations,
        EdgeWeightType edgeWeightType) {
        return edgeWeightType.getFactory().create(lineStations, departureId, arrivalId);
    }

    public WholeSubwayResponse wholeLines() {
        List<Line> lines = lineRepository.findAll();
        return lines.stream()
            .map(line -> LineDetailResponse.of(line, stationRepository.findAllById(line.getLineStationsId())))
            .collect(collectingAndThen(toList(), WholeSubwayResponse::new));
    }
}
