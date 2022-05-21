package wooteco.subway.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.domain.line.Line;
import wooteco.subway.domain.line.LineEntity;
import wooteco.subway.domain.section.Section;
import wooteco.subway.domain.section.SectionEntity;
import wooteco.subway.domain.station.Station;
import wooteco.subway.dto.LineRequest;
import wooteco.subway.dto.LineResponse;
import wooteco.subway.dto.StationResponse;
import wooteco.subway.repository.LineRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LineService {
    private final LineRepository lineRepository;

    public LineService(LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    @Transactional
    public LineResponse save(LineRequest lineRequest) {
        LineEntity lineEntity = new LineEntity(lineRequest.getName(), lineRequest.getColor(), lineRequest.getExtraFare());
        checkDuplication(lineEntity.getName());
        Line line = lineRepository.saveLine(lineEntity);

        SectionEntity sectionEntity = new SectionEntity(line.getId(), lineRequest.getUpStationId(), lineRequest.getDownStationId(), lineRequest.getDistance());
        Section section = lineRepository.saveSection(sectionEntity);
        line.addSection(section);

        return new LineResponse(line, createStationResponseOf(line));
    }

    private void checkDuplication(String lineName) {
        if (lineRepository.existByName(lineName)) {
            throw new IllegalArgumentException("이미 존재하는 노선 이름입니다.");
        }
    }

    public List<LineResponse> findAll() {
        List<Line> lines = lineRepository.findAll();

        return lines.stream()
                .map(line -> new LineResponse(line, createStationResponseOf(line)))
                .collect(Collectors.toList());
    }

    public LineResponse findById(Long id) {
        Line line = lineRepository.findById(id);

        return new LineResponse(line, createStationResponseOf(line));
    }

    private List<StationResponse> createStationResponseOf(Line line) {
        List<Station> sortedStations = line.getStationsInLine();

        return sortedStations.stream()
                .map(station -> new StationResponse(station.getId(), station.getName()))
                .collect(Collectors.toList());
    }

    public void edit(Long id, String name, String color, int extraFare) {
        lineRepository.edit(id, name, color, extraFare);
    }

    public void delete(Long id) {
        lineRepository.delete(id);
    }
}
