package wooteco.subway.service;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.dao.LineDao;
import wooteco.subway.dao.SectionDao;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Sections;
import wooteco.subway.ui.dto.SectionRequest;

@Service
@Transactional
public class SectionService {

    private static final String NO_EXISTS_LINE_ERROR = "존재하지 않는 노선에 등록할 수 없습니다. -> %d";

    private final LineDao lineDao;
    private final SectionDao sectionDao;

    public SectionService(LineDao lineDao, SectionDao sectionDao) {
        this.lineDao = lineDao;
        this.sectionDao = sectionDao;
    }

    public void create(Long lineId, SectionRequest sectionRequest) {
        validLineId(lineId);
        Sections sections = new Sections(sectionDao.findByLineId(lineId));

        Section newSection = sectionRequest.toEntity(lineId);
        Optional<Section> updateSection = sections.findUpdateWhenAdd(newSection);

        sectionDao.save(newSection);
        updateSection.ifPresent(sectionDao::update);
    }

    private void validLineId(Long lineId) {
        if (!lineDao.existsById(lineId)) {
            throw new IllegalArgumentException(
                    String.format(NO_EXISTS_LINE_ERROR, lineId));
        }
    }

    public void deleteById(Long lineId, Long stationId) {
        List<Section> sections = sectionDao.findByLineIdAndStationId(lineId, stationId);
        delete(new Sections(sections), stationId);
    }

    public void delete(Sections sections, Long stationId) {
        Optional<Section> updatedSection = sections.findUpdateWhenRemove(stationId);
        updatedSection.ifPresent(sectionDao::update);

        Section section = sections.findByDownStationId(stationId);
        sectionDao.deleteById(section.getId());
    }
}
