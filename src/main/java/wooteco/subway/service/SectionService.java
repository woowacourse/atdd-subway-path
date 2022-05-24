package wooteco.subway.service;

import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.dao.SectionDao;
import wooteco.subway.domain.Line;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Sections;
import wooteco.subway.dto.LineResponse;
import wooteco.subway.dto.SectionDto;
import wooteco.subway.dto.SectionRequest;

import java.util.List;

@Service
public class SectionService {

    private final SectionDao sectionDao;
    private final LineService lineService;

    public SectionService(final SectionDao sectionDao, final LineService lineService) {
        this.sectionDao = sectionDao;
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
        List<SectionDto> sectionDtos = sectionDao.findAllByLineId(lineId);
        List<Section> values = sectionDtos.stream()
                .map(sectionDto -> new Section(sectionDto.getId(), lineService.getById(lineId),
                        sectionDto.getUpStationId(), sectionDto.getDownStationId(), sectionDto.getDistance()))
                .collect(Collectors.toList());

        Sections sections = new Sections(values);
        sections.remove(stationId);
        updateSection(lineId, sections);
    }

    private void updateSection(Long lineId, Sections sections) {
        sectionDao.deleteByLineId(lineId);
        for (Section section : sections.getSections()) {
            sectionDao.save(lineId, section.getUpStationId(), section.getDownStationId(), section.getDistance());
        }
    }

    private Sections findSections(final Long lineId) {
        List<SectionDto> sectionDtos = sectionDao.findAllByLineId(lineId);

        List<Section> sections = sectionDtos.stream()
                .map(sectionDto -> new Section(sectionDto.getId(), lineService.getById(lineId),
                        sectionDto.getUpStationId(), sectionDto.getDownStationId(), sectionDto.getDistance()))
                .collect(Collectors.toList());

        return new Sections(sections);
    }
}
