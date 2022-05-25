package wooteco.subway.infrastructure.jdbc;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import wooteco.subway.domain.line.Line;
import wooteco.subway.domain.line.LineRepository;
import wooteco.subway.domain.line.section.Section;
import wooteco.subway.exception.NoSuchLineException;
import wooteco.subway.infrastructure.jdbc.dao.LineDao;
import wooteco.subway.infrastructure.jdbc.dao.SectionDao;
import wooteco.subway.infrastructure.jdbc.dao.entity.EntityAssembler;
import wooteco.subway.infrastructure.jdbc.dao.entity.LineEntity;
import wooteco.subway.infrastructure.jdbc.dao.entity.SectionEntity;

@Repository
public class LineJdbcRepository implements LineRepository {

    private final LineDao lineDao;
    private final SectionDao sectionDao;

    public LineJdbcRepository(LineDao lineDao, SectionDao sectionDao) {
        this.lineDao = lineDao;
        this.sectionDao = sectionDao;
    }

    @Override
    public Line save(Line line) {
        long lineId = saveLine(line);
        saveSections(lineId, line);
        return getById(lineId);
    }
    
    private long saveLine(Line line) {
        LineEntity lineEntity = EntityAssembler.lineEntity(line);
        return lineDao.save(lineEntity);
    }
    
    private void saveSections(long lineId, Line line) {
        List<SectionEntity> sectionEntities = EntityAssembler.sectionEntities(lineId, line);
        sectionEntities.forEach(sectionDao::save);
    }

    @Override
    public List<Line> getAll() {
        return lineDao.findAll()
                .stream()
                .map(lineEntity -> EntityAssembler.line(lineEntity, findSectionsByLineId(lineEntity.getId())))
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public Line getById(long lineId) {
        return lineDao.findById(lineId)
                .map(lineEntity -> EntityAssembler.line(lineEntity, findSectionsByLineId(lineEntity.getId())))
                .orElseThrow(() -> new NoSuchLineException(lineId));
    }

    private List<Section> findSectionsByLineId(long lineId) {
        return sectionDao.findAllByLineId(lineId)
                .stream()
                .map(EntityAssembler::section)
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public Line update(Line line) {
        lineDao.update(EntityAssembler.lineEntity(line));
        return getById(line.getId());
    }

    @Override
    public void updateSections(Line line) {
        removeNonExistentSections(line);
        saveNewSections(line);
    }

    private void removeNonExistentSections(Line line) {
        List<Long> updatedSectionIds = line.getSections()
                .stream()
                .map(Section::getId)
                .collect(Collectors.toUnmodifiableList());

        sectionDao.findAllIdByLineId(line.getId())
                .stream()
                .filter(sectionId -> !updatedSectionIds.contains(sectionId))
                .forEach(sectionDao::remove);
    }

    private void saveNewSections(Line line) {
        line.getSections()
                .stream()
                .filter(Section::isTemporary)
                .forEach(section -> sectionDao.save(EntityAssembler.sectionEntity(line.getId(), section)));
    }

    @Override
    public void remove(long lineId) {
        Line line = getById(lineId);
        removeSections(line.getSections());
        lineDao.remove(lineId);
    }

    private void removeSections(List<Section> sections) {
        sections.stream()
                .map(Section::getId)
                .forEach(sectionDao::remove);
    }

    @Override
    public boolean existsByName(String name) {
        return lineDao.existsByName(name);
    }

    @Override
    public boolean existsByColor(String color) {
        return lineDao.existsByColor(color);
    }

    @Override
    public boolean existsSectionByStationId(long stationId) {
        return sectionDao.existsByStationId(stationId);
    }
}
