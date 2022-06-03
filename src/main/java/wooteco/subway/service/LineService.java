package wooteco.subway.service;

import org.springframework.stereotype.Service;
import wooteco.subway.dao.line.LineDao;
import wooteco.subway.domain.line.Line;
import wooteco.subway.domain.line.Lines;
import wooteco.subway.domain.section.Section;
import wooteco.subway.dto.line.LineRequest;
import wooteco.subway.exception.DataNotExistException;

@Service
public class LineService {

    private final LineDao lineDao;

    private final SectionService sectionService;

    public LineService(LineDao lineDao, SectionService sectionService) {
        this.lineDao = lineDao;
        this.sectionService = sectionService;
    }

    public long save(LineRequest lineRequest) {
        Line line = new Line(lineRequest.getName(), lineRequest.getColor(), lineRequest.getExtraFare());
        Lines lines = findAll();
        lines.validateDuplication(line);
        long lineId = lineDao.save(line);

        saveSection(lineRequest, lineId);
        return lineId;
    }

    private void saveSection(LineRequest lineRequest, long lineId) {
        Section section = new Section(lineId,
                lineRequest.getUpStationId(), lineRequest.getDownStationId(), lineRequest.getDistance());
        sectionService.save(section);
    }

    public Lines findAll() {
        return new Lines(lineDao.findAll());
    }

    public Line findById(Long id) {
        validateExistLine(id);
        return lineDao.findById(id);
    }

    public void update(Line line) {
        validateExistLine(line.getId());
        Lines lines = findAll();
        lines.validateDuplication(line);
        lineDao.update(line);
    }

    public void delete(Long id) {
        validateExistLine(id);
        lineDao.delete(id);
    }

    private void validateExistLine(Long id) {
        if (!lineDao.existLineById(id)) {
            throw new DataNotExistException("존재하지 않는 지하철 노선입니다.");
        }
    }
}
