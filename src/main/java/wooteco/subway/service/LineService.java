package wooteco.subway.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import wooteco.subway.dao.LineDao;
import wooteco.subway.domain.Line;
import wooteco.subway.domain.Section;
import wooteco.subway.exception.DataNotFoundException;
import wooteco.subway.exception.DuplicateNameException;

@Service
public class LineService {

    private final LineDao lineDao;
    private final SectionService sectionService;
    private final StationService stationService;

    public LineService(final LineDao lineDao, final SectionService sectionService, final StationService stationService) {
        this.lineDao = lineDao;
        this.sectionService = sectionService;
        this.stationService = stationService;
    }

    @Transactional
    public Line createLine(final Line line, final Section section) {
        validateDuplicateNameExist(line);
        validateStationsNames(section);
        final Line savedLine = lineDao.save(line);
        final Section sectionToSave = new Section(
            section.getUpStation(),
            section.getDownStation(),
            section.getDistance(),
            savedLine.getId()
        );
        sectionService.saveInitialSection(sectionToSave);
        return savedLine;
    }

    @Transactional(readOnly = true)
    public List<Line> findAllLines() {
        return lineDao.findAll();
    }

    @Transactional(readOnly = true)
    public List<Line> findAllByIds(final List<Long> ids) {
        return lineDao.findAllByIds(ids);
    }

    @Transactional(readOnly = true)
    public Line findLineById(final Long id) {
        return lineDao.findById(id)
            .orElseThrow(() -> new DataNotFoundException("존재하지 않는 노선 ID입니다."));
    }

    @Transactional
    public void update(final Long id, final Line line) {
        final Line result = findLineById(id);
        validateUpdatedName(line, result);
        result.update(line);

        lineDao.update(id, result);
    }

    @Transactional
    public void delete(final Long id) {
        final int affectedRows = lineDao.deleteById(id);

        if (affectedRows == 0) {
            throw new DataNotFoundException("존재하지 않는 노선 id 입니다.");
        }
    }

    private void validateUpdatedName(final Line newLine, final Line result) {
        if (!result.getName().equals(newLine.getName())) {
            validateDuplicateNameExist(newLine);
        }
    }

    private void validateDuplicateNameExist(final Line line) {
        if (lineDao.existByName(line.getName())) {
            throw new DuplicateNameException("이미 존재하는 노선입니다.");
        }
    }

    private void validateStationsNames(final Section section) {
        stationService.findStationById(section.getUpStation().getId());
        stationService.findStationById(section.getDownStation().getId());
    }
}
