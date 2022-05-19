package wooteco.subway.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import wooteco.subway.dao.LineDao;
import wooteco.subway.dao.SectionDao;
import wooteco.subway.dao.StationDao;
import wooteco.subway.domain.Line;
import wooteco.subway.domain.Path;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Sections;
import wooteco.subway.entity.LineEntity;
import wooteco.subway.entity.SectionEntity;

@Service
public class DomainCreatorService {
    private static final String ERROR_MESSAGE_NOT_EXISTS_ID = "존재하지 않는 지하철 노선 id입니다.";

    private final LineDao lineDao;
    private final SectionDao sectionDao;
    private final StationDao stationDao;

    DomainCreatorService(LineDao lineDao, SectionDao sectionDao, StationDao stationDao) {
        this.lineDao = lineDao;
        this.sectionDao = sectionDao;
        this.stationDao = stationDao;
    }

    Path createPath() {
        List<Section> sections = sectionDao.findAll().stream()
                .map(this::findSection)
                .collect(Collectors.toList());
        return new Path(new Sections(sections));
    }

    Line createLine(Long lineId) {
        validateExists(lineId);
        LineEntity lineEntity = lineDao.find(lineId);
        return new Line(
                lineEntity.getId(),
                lineEntity.getName(),
                lineEntity.getColor(),
                lineEntity.getExtraFare(),
                new Sections(findSections(lineEntity.getId())));
    }

    private List<Section> findSections(Long lineId) {
        List<SectionEntity> sectionEntities = sectionDao.findByLine(lineId);
        return sectionEntities.stream()
                .map(this::findSection)
                .collect(Collectors.toList());
    }

    private Section findSection(SectionEntity sectionEntity) {
        return new Section(
                sectionEntity.getId(),
                stationDao.getStation(sectionEntity.getUpStationId()),
                stationDao.getStation(sectionEntity.getDownStationId()),
                sectionEntity.getDistance());
    }

    private void validateExists(Long id) {
        if (!lineDao.existById(id)) {
            throw new IllegalArgumentException(ERROR_MESSAGE_NOT_EXISTS_ID);
        }
    }
}
