package wooteco.subway.service.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import wooteco.subway.dao.LineDao;
import wooteco.subway.dao.SectionDao;
import wooteco.subway.dao.StationDao;
import wooteco.subway.domain.line.Line;
import wooteco.subway.domain.line.LineRepository;
import wooteco.subway.domain.section.Section;
import wooteco.subway.domain.section.Sections;
import wooteco.subway.dto.LineEntity;
import wooteco.subway.dto.SectionEntity;

@Repository
public class LineRepositoryWithDao implements LineRepository {
    private static final String ERROR_MESSAGE_NOT_EXISTS_ID = "존재하지 않는 지하철 노선 id입니다.";
    private static final String ERROR_MESSAGE_DUPLICATE_NAME = "중복된 지하철 노선 이름입니다.";

    private final LineDao lineDao;
    private final SectionDao sectionDao;
    private final StationDao stationDao;

    public LineRepositoryWithDao(LineDao lineDao, SectionDao sectionDao, StationDao stationDao) {
        this.lineDao = lineDao;
        this.sectionDao = sectionDao;
        this.stationDao = stationDao;
    }

    private Line createLine(Long lineId) {
        LineEntity lineEntity = lineDao.find(lineId);
        return new Line(lineEntity.getId(), lineEntity.getName(), lineEntity.getColor(),
            lineEntity.getExtraFare(), new Sections(findSections(lineEntity.getId())));
    }

    private List<Section> findSections(Long lineId) {
        List<SectionEntity> sectionEntities = sectionDao.findByLine(lineId);
        return sectionEntities.stream()
            .map(this::findSection)
            .collect(Collectors.toList());
    }

    private Section findSection(SectionEntity sectionEntity) {
        return new Section(sectionEntity.getId(), stationDao.getStation(sectionEntity.getUpStationId())
            , stationDao.getStation(sectionEntity.getDownStationId()), sectionEntity.getDistance());
    }

    private void validateNotExists(Long id) {
        if (!lineDao.existById(id)) {
            throw new IllegalArgumentException(ERROR_MESSAGE_NOT_EXISTS_ID);
        }
    }

    @Override
    public List<Line> findAll() {
        List<Line> lines = new ArrayList<>();
        List<LineEntity> lineEntities = lineDao.findAll();
        for (LineEntity lineEntity : lineEntities) {
            lines.add(createLine(lineEntity.getId()));
        }
        return lines;
    }

    @Override
    public Line save(Line line, long upStationId, long downStationId, int distance) {
        validateNameDuplication(line.getName());
        LineEntity lineEntity = lineDao.save(line);
        saveFirstSection(lineEntity.getId(), upStationId, downStationId, distance);
        return createLine(lineEntity.getId());
    }

    private void saveFirstSection(long lineId, long upStationId, long downStationId, int distance) {
        Section section = new Section(stationDao.getStation(upStationId), stationDao.getStation(downStationId),
            distance);
        sectionDao.save(lineId, section);
    }

    @Override
    public Line find(long id) {
        validateNotExists(id);
        LineEntity lineEntity = lineDao.find(id);
        return createLine(lineEntity.getId());
    }

    @Override
    public void update(Line line) {
        validateNotExists(line.getId());
        validateNameDuplication(line.getName());
        lineDao.update(line);
    }

    private void validateNameDuplication(String name) {
        if (lineDao.existByName(name)) {
            throw new IllegalArgumentException(ERROR_MESSAGE_DUPLICATE_NAME);
        }
    }

    @Override
    public void delete(long id) {
        validateNotExists(id);
        sectionDao.deleteAll(id);
        lineDao.delete(id);
    }
}
