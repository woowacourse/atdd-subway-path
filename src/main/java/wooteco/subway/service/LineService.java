package wooteco.subway.service;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import wooteco.subway.dao.LineDao;
import wooteco.subway.domain.Line;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Station;
import wooteco.subway.ui.dto.LineRequest;
import wooteco.subway.ui.dto.LineResponse;
import wooteco.subway.ui.dto.SectionRequest;
import wooteco.subway.exception.EmptyResultException;

@Service
@Transactional
public class LineService {
    private final LineDao lineDao;
    private final StationService stationService;
    private final SectionService sectionService;

    public LineService(LineDao lineDao, StationService stationService, SectionService sectionService) {
        this.lineDao = lineDao;
        this.stationService = stationService;
        this.sectionService = sectionService;
    }

    public LineResponse save(LineRequest lineRequest) {
        Line line = new Line(lineRequest.getName(), lineRequest.getColor(), lineRequest.getExtraFare());
        Long savedLineId = lineDao.save(line);

        sectionService.save(SectionRequest.of(lineRequest), savedLineId);
        return LineResponse.from(savedLineId, line);
    }

    @Transactional(readOnly = true)
    public LineResponse findById(Long id) {
        return lineDao.findById(id)
            .map(LineResponse::from)
            .orElseThrow(throwEmptyLineResultException());
    }

    @Transactional(readOnly = true)
    public List<LineResponse> findAll() {
        return lineDao.findAll().stream()
            .map(LineResponse::from)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<Line> findAllLines() {
        return lineDao.findAll();
    }

    public boolean deleteById(Long id) {
        return lineDao.deleteById(id);
    }

    public boolean updateById(Long id, LineRequest lineRequest) {
        Line line = findLineById(id);

        line.update(lineRequest.getName(), lineRequest.getColor());
        return lineDao.updateById(id, line);
    }

    public void insertSection(Long lineId, SectionRequest sectionRequest) {
        Line line = findLineById(lineId);
        Section section = sectionService.createSection(sectionRequest);

        line.insertSection(section);
        sectionService.update(line.getSections());
        sectionService.save(sectionRequest, lineId);
    }

    public void deleteStation(Long lineId, Long stationId) {
        Station station = stationService.findById(stationId);
        Line line = findLineById(lineId);
        Long sectionId = line.deleteSection(station);
        checkEmptyResult(sectionId);

        sectionService.update(line.getSections());
        sectionService.deleteById(sectionId);
    }

    private Line findLineById(Long id) {
        return lineDao.findById(id)
            .orElseThrow(throwEmptyLineResultException());
    }

    private void checkEmptyResult(Long sectionId) {
        if (sectionId == -1L) {
            throw new EmptyResultException("삭제할 구간을 찾지 못했습니다.");
        }
    }

    private Supplier<EmptyResultException> throwEmptyLineResultException() {
        return () -> new EmptyResultException("해당 노선을 찾을 수 없습니다.");
    }
}
