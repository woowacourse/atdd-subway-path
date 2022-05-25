package wooteco.subway.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import wooteco.subway.domain.Line;
import wooteco.subway.repository.LineRepository;
import wooteco.subway.service.dto.LineRequest;

@ExtendWith({MockitoExtension.class})
class LineServiceTest {

    @InjectMocks
    private LineService lineService;

    @Mock
    private LineRepository lineRepository;

    @Mock
    private SectionService sectionService;

    @Test
    @DisplayName("노선을 생성한다.")
    void createLine() {
        // given
        final LineRequest request = new LineRequest("7호선", "bg-red-600", 1L, 2L, 10);
        Line line = Line.withoutIdOf(request.getName(), request.getColor(), request.getExtraFare());
        when(lineRepository.save(line)).thenReturn(new Line(1L, line.getName(), line.getColor(), line.getExtraFare()));

        // when
        Line save = lineService.save(request);

        // then
        assertThat(save.getName()).isEqualTo(request.getName());
    }

    @Test
    @DisplayName("추가 요금을 가진 노선을 생성한다")
    void createLine_extraFare() {
        // given
        long extraFare = 100L;
        final LineRequest request = new LineRequest("7호선", "bg-red-600", 1L, 2L, 10, extraFare);
        Line line = Line.withoutIdOf(request.getName(), request.getColor(), request.getExtraFare());
        when(lineRepository.save(line)).thenReturn(new Line(1L, line.getName(), line.getColor(), line.getExtraFare()));

        // when
        Line saved = lineService.save(request);

        // then
        assertThat(saved.getExtraFare()).isEqualTo(extraFare);
    }

    @Test
    @DisplayName("모든 노선을 조회한다.")
    void showLines() {
        // given
        Line 일호선 = new Line(1L, "1호선", "red", 100L);
        Line 이호선 = new Line(2L, "2호선", "blue", 200L);
        when(lineRepository.findAll()).thenReturn(List.of(일호선, 이호선));

        // when
        List<Line> lines = lineService.findAll();

        // then
        assertThat(lines).hasSize(2);
    }

    @Test
    @DisplayName("id에 해당하는 노선을 조회한다.")
    void findById() {
        // given
        String color = "bg-red-600";
        String name = "7호선";
        long id = 1L;
        Line 칠호선 = new Line(id, name, color, 100L);
        when(lineRepository.findById(id)).thenReturn(칠호선);

        // when
        Line response = lineService.findById(id);

        // then
        assertThat(response.getName()).isEqualTo(name);
        assertThat(response.getColor()).isEqualTo(color);
    }
}
