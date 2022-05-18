package wooteco.subway.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.dao.SectionDao;
import wooteco.subway.domain.LineSections;
import wooteco.subway.domain.Section;
import wooteco.subway.dto.SectionRequest;

@Service
@Transactional
public class SectionService {

    private final SectionDao sectionDao;

    public SectionService(SectionDao sectionDao) {
        this.sectionDao = sectionDao;
    }

    public void firstSave(Long lineId, SectionRequest sectionRequest) {
        sectionDao.save(new Section(
                null, lineId,
                sectionRequest.getUpStationId(),
                sectionRequest.getDownStationId(),
                sectionRequest.getDistance(),
                1L));
    }

    public void save(Long lineId, SectionRequest sectionReq) {
        long upStationId = sectionReq.getUpStationId();
        long downStationId = sectionReq.getDownStationId();
        int distance = sectionReq.getDistance();

        LineSections lineSections = new LineSections(sectionDao.findAllByLineId(lineId));
        lineSections.validateSection(upStationId, downStationId, distance);

        List<Section> targetSections = lineSections.findOverlapSection(upStationId, downStationId,
                distance);
        updateSections(targetSections);
    }

    private void updateSections(List<Section> sections) {
        updateFrontSection(sections.get(0));
        updateBackSection(sections.get(1));
    }

    private void updateFrontSection(Section section) {
        sectionDao.deleteById(section.getId());
        sectionDao.save(section);
    }

    private void updateBackSection(Section section) {
        sectionDao.updateLineOrderByInc(section.getLineId(), section.getLineOrder());
        sectionDao.save(section);
    }

    public List<Long> findAllStationByLineId(long lineId) {
        LineSections lineSections = new LineSections(sectionDao.findAllByLineId(lineId));
        return lineSections.getStationsId();
    }

    public void deleteByLineIdAndStationId(long lineId, long stationId) {
        LineSections lineSections = new LineSections(sectionDao.findByLineIdAndStationId(lineId, stationId));
        if (lineSections.hasTwoSection()) {
            Section upsideSection = lineSections.getUpsideSection();
            Section downsideSection = lineSections.getDownsideSection();

            deleteAndUnionTwoSection(lineId, upsideSection, downsideSection);
            return;
        }
        deleteSingleSection(lineId, lineSections);
    }

    private void deleteAndUnionTwoSection(long lineId, Section upsideSection,
                                          Section downsideSection) {
        sectionDao.deleteById(upsideSection.getId());
        sectionDao.deleteById(downsideSection.getId());
        sectionDao.save(new Section(null, lineId,
                upsideSection.getUpStationId(), downsideSection.getDownStationId(),
                upsideSection.getDistance() + downsideSection.getDistance(),
                upsideSection.getLineOrder()));

        sectionDao.updateLineOrderByDec(lineId, downsideSection.getLineOrder());
    }

    private void deleteSingleSection(long lineId, LineSections lineSections) {
        Section section = lineSections.getSingleDeleteSection();
        sectionDao.deleteById(section.getId());

        sectionDao.updateLineOrderByDec(lineId, section.getLineOrder());
    }
}
