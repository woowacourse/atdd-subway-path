package wooteco.subway.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import wooteco.subway.domain.line.Line;
import wooteco.subway.domain.line.LineRepository;
import wooteco.subway.domain.station.Station;
import wooteco.subway.dto.service.StationDto;
import wooteco.subway.dto.service.request.LineServiceRequest;
import wooteco.subway.dto.service.request.LineUpdateRequest;
import wooteco.subway.dto.service.response.LineServiceResponse;

@Service
public class LineService {
    private final LineRepository lineRepository;

    public LineService(LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    @Transactional
    public LineServiceResponse save(LineServiceRequest lineInfo) {
        Line lineToAdd = new Line(lineInfo.getName(), lineInfo.getColor(), lineInfo.getExtraFare());
        Line resultLine = lineRepository.save(lineToAdd, lineInfo.getUpStationId(), lineInfo.getDownStationId(),
            lineInfo.getDistance());

        return new LineServiceResponse(resultLine.getId(), resultLine.getName(), resultLine.getColor(),
            resultLine.getExtraFare(), convertStationToInfo(resultLine.getStations()));
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
        Line line = new Line(lineUpdateRequest.getId(), lineUpdateRequest.getName(), lineUpdateRequest.getColor(),
            lineUpdateRequest.getExtraFare());
        lineRepository.update(line);
    }

    @Transactional
    public void delete(Long id) {
        lineRepository.delete(id);
    }

}
