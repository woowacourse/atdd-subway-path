package wooteco.subway.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.domain.Line;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.SectionRepository;
import wooteco.subway.domain.Sections;
import wooteco.subway.dto.SectionRequest;

@Service
public class SectionService {

    private final SectionRepository sectionRepository;
    private final LineService lineService;

    public SectionService(final SectionRepository sectionRepository,
                            final LineService lineService) {
        this.sectionRepository = sectionRepository;
        this.lineService = lineService;
    }

    @Transactional
    public void save(final Long lineId, final SectionRequest sectionRequest) {
        Sections sections = findSections(lineId);

        Line line = lineService.getById(lineId);

        sections.add(new Section(
                new Line(line.getId(), line.getName(), line.getColor(), line.getExtraFare()),
                sectionRequest.getUpStationId(),
                sectionRequest.getDownStationId(),
                sectionRequest.getDistance())
        );

        updateSection(lineId, sections);
    }

    @Transactional
    public void deleteById(final Long lineId, final Long stationId) {
        List<Section> values = sectionRepository.getSectionsByLineId(lineId);
        Sections sections = new Sections(values);
        sections.remove(stationId);
        updateSection(lineId, sections);
    }

    private void updateSection(Long lineId, Sections sections) {
        sectionRepository.deleteByLineId(lineId);
        for (Section section : sections.getSections()) {
            sectionRepository.saveSection(section);
        }
    }

    private Sections findSections(final Long lineId) {
        List<Section> sections = sectionRepository.getSectionsByLineId(lineId);
        return new Sections(sections);
    }
}
