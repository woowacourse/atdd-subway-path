package wooteco.subway.service;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.Mockito.when;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import wooteco.subway.domain.Line;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Station;
import wooteco.subway.service.dto.SectionDeleteRequest;
import wooteco.subway.service.dto.SectionSaveRequest;

class SectionServiceTest extends ServiceMockTest {

    @InjectMocks
    SectionService sectionService;

    Station 상계역;
    Station 중계역;
    Station 하계역;

    Section 상계_중계;
    Section 중계_하계;

    Line 칠호선;

    @BeforeEach
    void setUp() {
        상계역 = new Station(1L, "상계역");
        중계역 = new Station(2L, "중계역");
        하계역 = new Station(3L, "하계역");
        상계_중계 = new Section(1L, 칠호선, 상계역, 중계역, 10);
        중계_하계 = new Section(2L, 칠호선, 중계역, 하계역, 10);

        칠호선 = new Line(1L, "7호선", "red", 100L);
    }

    @Test
    @DisplayName("구간 등록하기")
    void saveSection() {
        // given
        SectionSaveRequest request = new SectionSaveRequest(1L, 1L, 2L, 1);
        when(lineRepository.findById(1L)).thenReturn(칠호선);
        when(stationService.findById(1L)).thenReturn(상계역);
        when(stationService.findById(2L)).thenReturn(중계역);

        // then
        assertThatCode(() -> sectionService.save(request))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("구간 삭제하기")
    void deleteSection() {
        // given
        SectionDeleteRequest request = new SectionDeleteRequest(1L, 2L);
        when(lineRepository.findById(1L)).thenReturn(칠호선);
        when(sectionRepository.findByLineId(1L))
                .thenReturn(List.of(상계_중계, 중계_하계));
        when(stationService.findById(2L)).thenReturn(중계역);

        // then
        assertThatCode(() -> sectionService.delete(request))
                .doesNotThrowAnyException();
    }
}
