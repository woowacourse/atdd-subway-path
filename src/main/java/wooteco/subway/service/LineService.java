package wooteco.subway.service;

import java.util.List;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.dao.LineDao;
import wooteco.subway.domain.Line;
import wooteco.subway.domain.Name;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Sections;
import wooteco.subway.dto.line.LineRequest;
import wooteco.subway.dto.section.SectionCreationRequest;
import wooteco.subway.exception.line.DuplicateLineException;
import wooteco.subway.exception.line.NoSuchLineException;

@Service
@Transactional
public class LineService {

    private final LineDao lineDao;
    private final SectionService sectionService;

    public LineService(final LineDao lineDao, @Lazy final SectionService sectionService) {
        this.lineDao = lineDao;
        this.sectionService = sectionService;
    }

    public Line create(final LineRequest request) {
        final Line line = new Line(request.getName(), request.getColor(), request.getExtraFare());
        final Line savedLine = lineDao.insert(line)
                .orElseThrow(DuplicateLineException::new);

        final SectionCreationRequest sectionCreationRequest = new SectionCreationRequest(
                savedLine.getId(),
                request.getUpStationId(),
                request.getDownStationId(),
                request.getDistance()
        );
        final Section section = sectionService.insert(sectionCreationRequest);

        return savedLine.addSections(new Sections(List.of(section)));
    }

    @Transactional(readOnly = true)
    public List<Line> findAll() {
        return lineDao.findAll();
    }

    @Transactional(readOnly = true)
    public Line findById(final Long id) {
        return lineDao.findById(id)
                .orElseThrow(NoSuchLineException::new);
    }

    public void updateById(final Long id, final LineRequest request) {
        final Line line = findById(id);
        final Line updatedLine = new Line(line.getId(), new Name(request.getName()), request.getColor(),
                request.getExtraFare(), line.getSections());
        lineDao.updateById(id, updatedLine)
                .orElseThrow(DuplicateLineException::new);
    }

    public void deleteById(final Long id) {
        lineDao.deleteById(id);
    }
}
