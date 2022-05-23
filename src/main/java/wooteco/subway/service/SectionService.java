package wooteco.subway.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.domain.Line;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Sections;
import wooteco.subway.repository.LineRepository;
import wooteco.subway.repository.SectionRepository;
import wooteco.subway.service.dto.SectionServiceDeleteRequest;
import wooteco.subway.service.dto.SectionServiceRequest;

@Service
@Transactional(readOnly = true)
public class SectionService {

    private final SectionRepository sectionRepository;
    private final LineRepository lineRepository;

    public SectionService(SectionRepository sectionRepository, LineRepository lineRepository) {
        this.sectionRepository = sectionRepository;
        this.lineRepository = lineRepository;
    }

    @Transactional
    public void save(SectionServiceRequest sectionServiceRequest, Long lineId) {
        Section section = toSection(sectionServiceRequest, lineId);
        Sections sections = new Sections(sectionRepository.findByLineId(lineId));
        List<Section> insertSections = sections.insert(section);
        sectionRepository.updateAll(insertSections);
    }

    private Section toSection(SectionServiceRequest sectionRequest, Long lineId) {
        Line line = lineRepository.findById(lineId);
        return new Section(line, sectionRequest.getUpStationId(),
            sectionRequest.getDownStationId(), sectionRequest.getDistance());
    }

    @Transactional
    public void removeSection(SectionServiceDeleteRequest sectionServiceDeleteRequest) {
        Sections sections = new Sections(
            sectionRepository.findByLineId(sectionServiceDeleteRequest.getLineId()));

        Section removeSection = new Section(sectionServiceDeleteRequest.getStationId(),
            sectionServiceDeleteRequest.getStationId());

        List<Section> insertSections = sections.removeSection(removeSection);
        sectionRepository.updateAll(insertSections);
    }
}
