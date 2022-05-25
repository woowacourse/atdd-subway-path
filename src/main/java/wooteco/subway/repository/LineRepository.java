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
import wooteco.subway.domain.line.LineMap;
import wooteco.subway.domain.section.Section;
import wooteco.subway.domain.section.Sections;
import wooteco.subway.domain.station.Station;
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

    public List<LineMap> findAllLines() {
        Map<Long, List<Section>> sectionsMap = sectionDao.findAll()
                .stream()
                .collect(groupingBy(Section::getLineId));
        return lineDao.findAll()
                .stream()
                .map(lineEntity -> new LineMap(lineEntity, new Sections(sectionsMap.get(lineEntity.getId()))))
                .collect(Collectors.toList());
    }

    public LineMap findExistingLine(Long id) {
        Line line = lineDao.findById(id)
                .orElseThrow(() -> new NotFoundException(ExceptionType.LINE_NOT_FOUND));
        Sections sections = new Sections(sectionDao.findAllByLineId(id));
        return new LineMap(line, sections);
    }

    public List<LineExtraFare> findLineExtraFaresByIds(List<Long> ids) {
        return lineDao.findAllByIds(ids)
                .stream()
                .map(Line::getLineExtraFare)
                .collect(Collectors.toList());
    }

    public boolean checkExistingLine(Long id) {
        return lineDao.findById(id).isPresent();
    }

    public boolean checkExistingLineName(String name) {
        return lineDao.findByName(name).isPresent();
    }

    public LineMap saveLine(LineMap line) {
        String name = line.getName();
        String color = line.getColor();
        int extraFare = line.getExtraFare();

        Line savedLine = lineDao.save(new Line(name, color, extraFare));
        saveSections(savedLine.getId(), line.toSectionList());
        return new LineMap(savedLine, line.getSections());
    }

    private void saveSections(Long lineId, List<Section> sections) {
        for (Section section : sections) {
            Station upStation = section.getUpStation();
            Station downStation = section.getDownStation();
            int distance = section.getDistance();
            sectionDao.save(new Section(lineId, upStation, downStation, distance));
        }
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
