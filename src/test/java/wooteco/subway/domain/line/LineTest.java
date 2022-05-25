package wooteco.subway.domain.line;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static wooteco.subway.TestFixture.강남_역삼;
import static wooteco.subway.TestFixture.선릉_삼성;
import static wooteco.subway.TestFixture.역삼_선릉;
import static wooteco.subway.TestFixture.역삼역_ID;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import wooteco.subway.domain.line.section.Section;

@DisplayName("지하철노선")
class LineTest {

    private static final long LINE_ID = 1L;
    private static final String LINE_NAME = "2호선";
    private static final String LINE_COLOR = "green";
    private static final long LINE_EXTRA_FARE = 0L;

    @DisplayName("이름과 색상, 추가요금을 변경한다.")
    @ParameterizedTest
    @CsvSource(value = {"1호선,red,100", "신분당선,yellow,300"})
    void update(String name, String color, long extraFare) {
        Line line = new Line(LINE_ID, List.of(강남_역삼), LINE_NAME, LINE_COLOR, LINE_EXTRA_FARE);
        line.update(name, color, extraFare);

        assertAll(
                () -> assertThat(line.getName()).isEqualTo(name),
                () -> assertThat(line.getColor()).isEqualTo(color),
                () -> assertThat(line.getExtraFare()).isEqualTo(extraFare)
        );
    }

    @DisplayName("구간을 추가한다")
    @Test
    void appendSection() {
        Line line = new Line(LINE_ID, List.of(강남_역삼), LINE_NAME, LINE_COLOR, LINE_EXTRA_FARE);
        line.appendSection(역삼_선릉);

        List<Section> actual = line.getSections();
        assertThat(actual).containsExactly(강남_역삼, 역삼_선릉);
    }

    @DisplayName("역을 제거한다")
    @Test
    void removeStation() {
        Line line = new Line(LINE_ID, List.of(강남_역삼, 역삼_선릉), LINE_NAME, LINE_COLOR, LINE_EXTRA_FARE);
        line.removeStation(역삼역_ID);

        List<Section> actual = line.getSections();
        assertThat(actual).hasSize(1);
    }

    @DisplayName("노선의 지하철구간들을 정렬하여 반환한다.")
    @Test
    void getSections() {
        Line line = new Line(LINE_ID, List.of(선릉_삼성, 역삼_선릉, 강남_역삼), LINE_NAME, LINE_COLOR, LINE_EXTRA_FARE);

        List<Section> actual = line.getSections();
        assertThat(actual).containsExactly(강남_역삼, 역삼_선릉, 선릉_삼성);
    }
}
