package wooteco.subway.repository;

import org.springframework.stereotype.Repository;
import wooteco.subway.dao.LineDao;
import wooteco.subway.dao.SectionDao;
import wooteco.subway.dao.StationDao;
import wooteco.subway.domain.line.Line;
import wooteco.subway.domain.line.LineEntity;
import wooteco.subway.domain.section.Section;
import wooteco.subway.domain.section.SectionEntity;
import wooteco.subway.domain.section.Sections;
import wooteco.subway.domain.section.creationStrategy.ConcreteCreationStrategy;
import wooteco.subway.domain.section.deletionStrategy.ConcreteDeletionStrategy;
import wooteco.subway.domain.section.sortStrategy.ConcreteSortStrategy;
import wooteco.subway.domain.station.Station;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class LineRepository {
    private final LineDao lineDao;
    private final StationDao stationDao;
    private final SectionDao sectionDao;

    public LineRepository(LineDao lineDao, StationDao stationDao, SectionDao sectionDao) {
        this.lineDao = lineDao;
        this.stationDao = stationDao;
        this.sectionDao = sectionDao;
    }

    public Line saveLine(LineEntity lineEntity) {
        LineEntity newLineEntity = lineDao.insert(lineEntity);

        return assembleLine(newLineEntity);
    }

    public Section saveSection(SectionEntity sectionEntity) {
        SectionEntity newSectionEntity = sectionDao.insert(sectionEntity);

        return assembleSection(newSectionEntity);
    }

    private Line assembleLine(LineEntity lineEntity) {
        List<SectionEntity> sectionEntities = sectionDao.findByLineId(lineEntity.getId());

        List<Section> sectionList = sectionEntities.stream()
                .map(this::assembleSection)
                .collect(Collectors.toList());

        Sections sections = new Sections(sectionList, new ConcreteCreationStrategy(), new ConcreteDeletionStrategy(), new ConcreteSortStrategy());
        return new Line(lineEntity.getId(), lineEntity.getName(), lineEntity.getColor(), lineEntity.getExtraFare(), sections);
    }

    private Section assembleSection(SectionEntity sectionEntity) {
        Station upStation = stationDao.findById(sectionEntity.getUpStationId());
        Station downStation = stationDao.findById(sectionEntity.getDownStationId());

        return new Section(sectionEntity.getId(), sectionEntity.getLineId(),
                upStation, downStation, sectionEntity.getDistance());
    }

    public List<Line> findAll() {
        List<LineEntity> lineEntities = lineDao.findAll();

        return lineEntities.stream()
                .map(this::assembleLine)
                .collect(Collectors.toList());
    }

    public Line findById(Long id) {
        LineEntity lineEntity = lineDao.findById(id);
        return assembleLine(lineEntity);
    }

    public void edit(Long id, String name, String color, int extraFare) {
        lineDao.update(new LineEntity(id, name, color, extraFare));
    }

    public void delete(Long id) {
        lineDao.delete(id);
    }

    public boolean existByName(String name) {
        return lineDao.existByName(name);
    }
}
