package wooteco.subway.domain.line;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.exception.SubwayException;

class LinesTest {

    private static final Lines LINES = new Lines(List.of(
            new Line(1L, "일호선", "bg-red-600", 200),
            new Line(2L, "이호선", "bg-green-600", 400),
            new Line(3L, "삼호선", "bg-blue-600", 100),
            new Line(4L, "사호선", "bg-yellow-600", 300)
    ));

    @DisplayName("중복되는 노선 이름이 존재하면 예외가 발생한다.")
    @Test
    void validateDuplicationName() {
        Line line = new Line("일호선", "bg-black-600", 200);

        assertThatThrownBy(() -> LINES.validateDuplication(line))
                .isInstanceOf(SubwayException.class)
                .hasMessage("지하철 노선 이름이 중복됩니다.");
    }

    @DisplayName("중복되는 노선 색상이 존재하면 예외가 발생한다.")
    @Test
    void validateDuplicationColor() {
        Line line = new Line("오호선", "bg-red-600", 200);

        assertThatThrownBy(() -> LINES.validateDuplication(line))
                .isInstanceOf(SubwayException.class)
                .hasMessage("지하철 노선 색상이 중복됩니다.");
    }

    @DisplayName("특정 노선들 중 추가 요금의 최대값을 구한다.")
    @Test
    void findMaxExtraFare() {
        Set<Long> lineIds = Set.of(1L, 2L, 4L);

        assertThat(LINES.findMaxExtraFare(lineIds)).isEqualTo(400);
    }
}
