package wooteco.subway.repository;

import static java.util.stream.Collectors.groupingBy;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;
import wooteco.subway.dao.LineDao;
import wooteco.subway.dao.SectionDao;
import wooteco.subway.domain.line.Line;
import wooteco.subway.domain.line.LineExtraFare;
import wooteco.subway.domain.section.Section;
import wooteco.subway.domain.section.Sections;
import wooteco.subway.entity.LineEntity;
import wooteco.subway.exception.ExceptionType;
import wooteco.subway.exception.NotFoundException;

@Repository
public class LineRepository {

    private final LineDao lineDao;
    private final SectionDao sectionDao;

    public LineRepository(LineDao lineDao, SectionDao sectionDao) {
        this.lineDao = lineDao;
        this.sectionDao = sectionDao;
    }

    public List<Line> findAllLines() {
        Map<Long, List<Section>> sectionsMap = sectionDao.findAll()
                .stream()
                .collect(groupingBy(Section::getLineId));
        return lineDao.findAll()
                .stream()
                .map(line -> toDomain(line, new Sections(sectionsMap.get(line.getId()))))
                .collect(Collectors.toList());
    }

    public Line findExistingLine(Long id) {
        LineEntity line = lineDao.findById(id)
                .orElseThrow(() -> new NotFoundException(ExceptionType.LINE_NOT_FOUND));
        Sections sections = new Sections(sectionDao.findAllByLineId(id));
        return toDomain(line, sections);
    }

    public List<LineExtraFare> findLineExtraFaresByIds(List<Long> ids) {
        return lineDao.findAllByIds(ids)
                .stream()
                .map(line -> new LineExtraFare(line.getExtraFare()))
                .collect(Collectors.toList());
    }

    public boolean checkExistingLine(Long id) {
        return lineDao.findById(id).isPresent();
    }

    public boolean checkExistingLineName(String name) {
        return lineDao.findByName(name).isPresent();
    }

    public Line saveLine(Line line) {
        String name = line.getName();
        String color = line.getColor();
        int extraFare = line.getExtraFare();

        LineEntity savedLine = lineDao.save(new LineEntity(name, color, extraFare));
        saveSections(savedLine.getId(), line.toSectionList());
        return toDomain(savedLine, line.getSections());
    }

    private void saveSections(Long lineId, List<Section> sections) {
        List<Section> newSections = sections.stream()
                .map(section -> new Section(lineId, section))
                .collect(Collectors.toList());
        sectionDao.save(newSections);
    }

    public void updateLine(Line line) {
        Long id = line.getId();
        String name = line.getName();
        String color = line.getColor();
        int extraFare = line.getExtraFare();

        LineEntity updatedLine = new LineEntity(id, name, color, extraFare);
        lineDao.update(updatedLine);
    }

    public void deleteLine(Line line) {
        Long id = line.getId();
        lineDao.deleteById(id);
        sectionDao.deleteAllByLineId(id);
    }

    private Line toDomain(LineEntity lineEntity, Sections sections) {
        Long lineId = lineEntity.getId();
        String color = lineEntity.getColor();
        int extraFare = lineEntity.getExtraFare();
        return new Line(lineId, lineEntity.getName(), color, extraFare, sections);
    }
}
