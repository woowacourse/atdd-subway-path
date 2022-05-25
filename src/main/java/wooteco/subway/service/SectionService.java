package wooteco.subway.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.dao.LineDao;
import wooteco.subway.dao.SectionDao;
import wooteco.subway.dao.StationDao;
import wooteco.subway.domain.section.Section;
import wooteco.subway.domain.section.Sections;
import wooteco.subway.ui.dto.SectionRequest;

@Service
@Transactional
public class SectionService {

    private static final String NO_EXISTS_LINE_ERROR = "존재하지 않는 노선에 등록할 수 없습니다. -> %d";

    private final LineDao lineDao;
    private final SectionDao sectionDao;
    private final StationDao stationDao;

    public SectionService(LineDao lineDao, SectionDao sectionDao, StationDao stationDao) {
        this.lineDao = lineDao;
        this.sectionDao = sectionDao;
        this.stationDao = stationDao;
    }

    public void create(Long lineId, SectionRequest sectionRequest) {
        validLineId(lineId);
        Sections sections = new Sections(sectionDao.findByLineId(lineId));

        Section newSection = sectionRequest.toEntity(lineId
                , stationDao.findById(sectionRequest.getUpStationId())
                , stationDao.findById(sectionRequest.getDownStationId()));
        sections.add(newSection);

        sectionDao.deleteByLineId(lineId);
        sections.getValue()
                .forEach(sectionDao::save);
    }

    private void validLineId(Long lineId) {
        if (!lineDao.existsById(lineId)) {
            throw new IllegalArgumentException(
                    String.format(NO_EXISTS_LINE_ERROR, lineId));
        }
    }

    public void deleteById(Long lineId, Long stationId) {
        List<Section> sections = sectionDao.findByLineIdAndStationId(lineId, stationId);
        delete(lineId, new Sections(sections), stationId);
    }

    public void delete(Long lineId, Sections sections, Long stationId) {
        sections.remove(stationId);
        sectionDao.deleteByLineId(lineId);
        sections.getValue()
                .forEach(sectionDao::save);
    }
}
