package wooteco.subway.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import wooteco.subway.dao.StationDao;
import wooteco.subway.domain.Station;
import wooteco.subway.domain.line.Line;
import wooteco.subway.domain.line.LineRepository;
import wooteco.subway.dto.service.StationDto;
import wooteco.subway.dto.service.request.LineServiceRequest;
import wooteco.subway.dto.service.request.LineUpdateRequest;
import wooteco.subway.dto.service.response.LineServiceResponse;

@Service
public class LineService {
    private static final String ERROR_MESSAGE_NOT_EXISTS_STATION = "존재하지 않는 역을 지나는 노선은 만들 수 없습니다.";

    private final LineRepository lineRepository;
    private final StationDao stationDao;

    public LineService(LineRepository lineRepository, StationDao stationDao) {
        this.stationDao = stationDao;
        this.lineRepository = lineRepository;
    }

    @Transactional
    public LineServiceResponse save(LineServiceRequest lineInfo) {
        long upStationId = lineInfo.getUpStationId();
        long downStationId = lineInfo.getDownStationId();

        validateBeforeSave(upStationId, downStationId);

        Line lineToAdd = new Line(lineInfo.getName(), lineInfo.getColor(), lineInfo.getExtraFare());
        Line resultLine = lineRepository.save(lineToAdd, upStationId, downStationId, lineInfo.getDistance());

        return new LineServiceResponse(resultLine.getId(), resultLine.getName(), resultLine.getColor(),
            resultLine.getExtraFare(), convertStationToInfo(resultLine.getStations()));
    }

    private void validateBeforeSave(Long upStationId, Long downStationId) {
        validateNotExistStation(upStationId);
        validateNotExistStation(downStationId);
    }

    public List<LineServiceResponse> findAll() {
        List<Line> lines = lineRepository.findAll();

        List<LineServiceResponse> lineServiceResponses = new ArrayList<>();
        for (Line line : lines) {
            lineServiceResponses.add(new LineServiceResponse(line.getId(), line.getName(), line.getColor(),
                line.getExtraFare(), convertStationToInfo(line.getStations())));
        }
        return lineServiceResponses;
    }

    public LineServiceResponse find(Long id) {
        Line line = lineRepository.find(id);
        return new LineServiceResponse(line.getId(), line.getName(), line.getColor(),
            line.getExtraFare(), convertStationToInfo(line.getStations()));
    }

    private List<StationDto> convertStationToInfo(List<Station> stations) {
        return stations.stream()
            .map(station -> new StationDto(station.getId(), station.getName()))
            .collect(Collectors.toList());
    }

    public void update(LineUpdateRequest lineUpdateRequest) {
        Long id = lineUpdateRequest.getId();
        String name = lineUpdateRequest.getName();
        int extraFare = lineUpdateRequest.getExtraFare();

        Line line = new Line(id, name, lineUpdateRequest.getColor(), extraFare);
        lineRepository.update(line);
    }

    @Transactional
    public void delete(Long id) {
        lineRepository.delete(id);
    }

    private void validateNotExistStation(Long stationId) {
        if (!stationDao.existById(stationId)) {
            throw new IllegalArgumentException(ERROR_MESSAGE_NOT_EXISTS_STATION);
        }
    }
}
