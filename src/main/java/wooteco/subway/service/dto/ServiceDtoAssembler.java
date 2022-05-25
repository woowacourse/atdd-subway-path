package wooteco.subway.service.dto;

import wooteco.subway.domain.Section;
import wooteco.subway.service.dto.section.SectionRequestDto;

public class ServiceDtoAssembler {

    private ServiceDtoAssembler() {
    }

    public static Section section(SectionRequestDto sectionRequestDto) {
        return new Section(sectionRequestDto.getLineId(), sectionRequestDto.getUpStationId(), sectionRequestDto.getDownStationId(), sectionRequestDto.getDistance());
    }
}
