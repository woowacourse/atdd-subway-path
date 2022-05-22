package wooteco.subway.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.dao.SectionDao;
import wooteco.subway.domain.LineSections;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.SectionUpdateResult;
import wooteco.subway.dto.request.SectionRequest;

@Service
@Transactional
public class SectionService {

    private final SectionDao sectionDao;

    public SectionService(final SectionDao sectionDao) {
        this.sectionDao = sectionDao;
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void firstSave(final Long lineId, final SectionRequest sectionRequest) {
        sectionDao.save(
                Section.createWithoutId(
                        lineId,
                        sectionRequest.getUpStationId(),
                        sectionRequest.getDownStationId(),
                        sectionRequest.getDistance(),
                        1L
                )
        );
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void save(final Long lineId, final SectionRequest sectionReq) {
        long upStationId = sectionReq.getUpStationId();
        long downStationId = sectionReq.getDownStationId();
        int distance = sectionReq.getDistance();

        final LineSections lineSections = new LineSections(sectionDao.findAllByLineId(lineId));
        lineSections.validateSection(upStationId, downStationId, distance);

        final SectionUpdateResult targetSections
                = lineSections.findOverlapSection(upStationId, downStationId, distance);
        updateSections(targetSections);
    }

    private void updateSections(final SectionUpdateResult sections) {
        updateSection(sections.getUpdatedSection());
        addSection(sections.getAddedSection());
    }

    private void updateSection(final Section section) {
        sectionDao.deleteById(section.getId());
        sectionDao.save(section);
    }

    private void addSection(final Section section) {
        sectionDao.updateLineOrderByInc(section.getLineId(), section.getLineOrder());
        sectionDao.save(section);
    }

    @Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
    public List<Long> findAllStationByLineId(final long lineId) {
        LineSections lineSections = new LineSections(sectionDao.findAllByLineId(lineId));
        return lineSections.getStationIds();
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void deleteByLineIdAndStationId(final long lineId, final long stationId) {
        final LineSections lineSections = new LineSections(sectionDao.findByLineIdAndStationId(lineId, stationId));
        if (lineSections.hasTwoSection()) {
            final Section upsideSection = lineSections.getUpsideSection();
            final Section downsideSection = lineSections.getDownsideSection();

            deleteAndUnionTwoSection(lineId, upsideSection, downsideSection);
            return;
        }
        deleteSingleSection(lineId, lineSections);
    }

    private void deleteAndUnionTwoSection(final long lineId,
                                          final Section upsideSection,
                                          final Section downsideSection) {
        sectionDao.deleteById(upsideSection.getId());
        sectionDao.deleteById(downsideSection.getId());
        sectionDao.save(
                Section.createWithoutId(
                        lineId,
                        upsideSection.getUpStationId(),
                        downsideSection.getDownStationId(),
                        upsideSection.getDistance() + downsideSection.getDistance(),
                        upsideSection.getLineOrder()
                )
        );
        sectionDao.updateLineOrderByDec(lineId, downsideSection.getLineOrder());
    }

    private void deleteSingleSection(final long lineId, final LineSections lineSections) {
        final Section section = lineSections.getSingleDeleteSection();
        sectionDao.deleteById(section.getId());

        sectionDao.updateLineOrderByDec(lineId, section.getLineOrder());
    }
}
