package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import wooteco.subway.domain.path.Path;

@SuppressWarnings("NonAsciiCharacters")
class PathTest {

    @ParameterizedTest(name = "거리가 {0}일 때, 요금이 {1}원이다.")
    @CsvSource(value = {"10,1250", "50,2050", "90,2550"})
    void 요금을_계산한다(int distance, int fare) {
        Station 강남역 = new Station(1L, "강남역");
        Station 선릉역 = new Station(2L, "선릉역");
        Line 신분당선 = new Line(1L, "신분당선", "red");
        신분당선.addSection(new Section(1L, 강남역, 선릉역, distance));

        Path path = Path.of(List.of(신분당선), 강남역, 선릉역);

        assertThat(path.calculateFare()).isEqualTo(fare);
    }

    @Test
    @DisplayName("해당 경로가 존재하지 않으면 예외가 발생한다.")
    void validatePath() {
        Station 강남역 = new Station(1L, "강남역");
        Station 선릉역 = new Station(2L, "선릉역");
        Station 잠실역 = new Station(3L, "잠실역");
        Station 신촌역 = new Station(4L, "신촌역");
        Line 신분당선 = new Line(1L, "신분당선", "red");
        Line 분당선 = new Line(2L, "분당선", "blue");
        신분당선.addSection(new Section(1L, 강남역, 선릉역, 10));
        분당선.addSection(new Section(2L, 신촌역, 잠실역, 10));

        assertThatThrownBy(() -> Path.of(List.of(신분당선, 분당선), 강남역, 잠실역))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("해당 경로가 존재하지 않습니다.");
    }
}
