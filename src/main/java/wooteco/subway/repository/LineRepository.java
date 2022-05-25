package wooteco.subway.repository;

import java.util.List;
import org.springframework.stereotype.Repository;
import wooteco.subway.dao.LineDao;
import wooteco.subway.dao.SectionDao;
import wooteco.subway.domain.line.Line;
import wooteco.subway.domain.line.LineMap;
import wooteco.subway.domain.section.Section;
import wooteco.subway.domain.section.Sections;
import wooteco.subway.exception.ExceptionType;
import wooteco.subway.exception.NotFoundException;

@Repository
public class LineRepository {

    private final LineDao lineDao;
    private final SectionDao sectionDao;

    public LineRepository(LineDao lineDao,
                          SectionDao sectionDao) {
        this.lineDao = lineDao;
        this.sectionDao = sectionDao;
    }

    public List<Line> findAllLines() {
        return lineDao.findAll();
    }

    public List<Line> findAllLinesByIds(List<Long> ids) {
        return lineDao.findAllByIds(ids);
    }

    public LineMap findExistingLine(Long id) {
        Line line = lineDao.findById(id)
                .orElseThrow(() -> new NotFoundException(ExceptionType.LINE_NOT_FOUND));
        Sections sections = new Sections(sectionDao.findAllByLineId(id));
        return new LineMap(line, sections);
    }

    public boolean checkExistingLine(Long id) {
        return lineDao.findById(id).isPresent();
    }

    public boolean checkExistingLineName(String name) {
        return lineDao.findByName(name).isPresent();
    }

    public LineMap saveLine(Line line, Section section) {
        String name = line.getName();
        String color = line.getColor();
        int extraFare = line.getExtraFare();

        Line savedLine = lineDao.save(new Line(name, color, extraFare));
        sectionDao.save(new Section(savedLine.getId(), section));
        return LineMap.of(savedLine, section);
    }

    public void updateLine(LineMap line) {
        Long id = line.getId();
        String name = line.getName();
        String color = line.getColor();
        int extraFare = line.getExtraFare();

        Line updatedLine = new Line(id, name, color, extraFare);
        lineDao.update(updatedLine);
    }

    public void deleteLine(LineMap line) {
        Long id = line.getId();
        lineDao.deleteById(id);
        sectionDao.deleteAllByLineId(id);
    }
}
