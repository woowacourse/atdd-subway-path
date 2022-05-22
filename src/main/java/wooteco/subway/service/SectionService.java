package wooteco.subway.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.dao.SectionDao;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Sections;
import wooteco.subway.dto.section.SectionRequest;

@Service
public class SectionService {

    private SectionDao sectionDao;

    public SectionService(SectionDao sectionDao) {
        this.sectionDao = sectionDao;
    }

    @Transactional
    public void addSection(Long lineId, SectionRequest sectionRequest) {
        var upStationId = sectionRequest.getUpStationId();
        var downStationId = sectionRequest.getDownStationId();
        var distance = sectionRequest.getDistance();

        var sections = new Sections(sectionDao.findByLineId(lineId));

        sections.createSection(new Section(upStationId, downStationId, distance))
                .ifPresent(sectionDao::update);

        sectionDao.create(lineId, sectionRequest);
    }

    @Transactional
    public void deleteSection(Long lineId, Long stationId) {
        var allSections = sectionDao.findByLineId(lineId);

        var sections = Sections.createByStationId(allSections, stationId);

        if (sections.hasOnlyOneSection()) {
            sectionDao.delete(sections.getFirstSection());
            return;
        }

        sectionDao.update(sections.createSection());
        sectionDao.delete(sections.getSecondSection());
    }
}
