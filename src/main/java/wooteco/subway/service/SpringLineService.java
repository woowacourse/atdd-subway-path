package wooteco.subway.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.domain.line.Line;
import wooteco.subway.domain.line.LineRepository;
import wooteco.subway.domain.section.Section;
import wooteco.subway.domain.station.Station;
import wooteco.subway.ui.dto.request.LineRequest;
import wooteco.subway.ui.dto.request.LineUpdateRequest;
import wooteco.subway.ui.dto.request.SectionRequest;
import wooteco.subway.ui.dto.response.LineResponse;

@Transactional
@Service
public class SpringLineService implements LineService {

    private final LineRepository lineRepository;

    public SpringLineService(LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    @Override
    public LineResponse create(LineRequest lineRequest) {
        Section section = createSection(lineRequest);
        Line line = lineRepository.saveLine(DtoAssembler.line(section, lineRequest));
        return DtoAssembler.lineResponse(line);
    }

    private Section createSection(LineRequest lineRequest) {
        return new Section(
                lineRepository.findStationById(lineRequest.getUpStationId()),
                lineRepository.findStationById(lineRequest.getDownStationId()),
                lineRequest.getDistance());
    }

    @Transactional(readOnly = true)
    @Override
    public List<LineResponse> findAll() {
        List<Line> lines = lineRepository.findLines();
        return DtoAssembler.lineResponses(lines);
    }

    @Transactional(readOnly = true)
    @Override
    public LineResponse findById(Long id) {
        Line line = lineRepository.findLineById(id);
        return DtoAssembler.lineResponse(line);
    }

    @Override
    public void updateById(Long id, LineUpdateRequest lineUpdateRequest) {
        Line line = lineRepository.findLineById(id);
        line.update(lineUpdateRequest.getName(), lineUpdateRequest.getColor());
        lineRepository.updateLine(line);
    }

    @Override
    public void deleteById(Long id) {
        lineRepository.removeLine(id);
    }

    @Override
    public void addSection(Long lineId, SectionRequest sectionRequest) {
        Line line = lineRepository.findLineById(lineId);
        Section section = createSection(sectionRequest);
        line.appendSection(section);
        lineRepository.updateSections(line);
    }

    private Section createSection(SectionRequest sectionRequest) {
        return new Section(
                lineRepository.findStationById(sectionRequest.getUpStationId()),
                lineRepository.findStationById(sectionRequest.getDownStationId()),
                sectionRequest.getDistance());
    }

    @Override
    public void removeSection(Long lineId, Long stationId) {
        Line line = lineRepository.findLineById(lineId);
        Station station = lineRepository.findStationById(stationId);
        line.removeStation(station);
        lineRepository.updateSections(line);
    }
}
