package wooteco.subway.domain;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import wooteco.subway.domain.factory.SectionFactory;
import wooteco.subway.domain.factory.StationFactory;

@DisplayName("Lines 는 ")
class LinesTest {

    final Sections sections = new Sections(List.of(SectionFactory.from(SectionFactory.AB3),
            SectionFactory.from(SectionFactory.BC3)));
    final Line line = new Line(1L, "line", "bg-red-600", sections, 1L, 10);
    final Lines lines = new Lines(List.of(line));

    @Nested
    @DisplayName("경로에 포함된 라인 중에서 최대 추가 비용을 계산할 떄")
    class ExtraLineFareCalculationTest {

        @DisplayName("경로에 포함된 라인이 있으면 해당 최대 추가 비용을 가져온다.")
        @Test
        void hasExtraLineFare() {
            assertThat(lines.calculateExtraLineFare(List.of(StationFactory.from(StationFactory.A)))).isEqualTo(10);
        }

        @DisplayName("경로에 포함된 라인이 없으면 0을 반환한다.")
        void hasNoExtraLineFare() {
            assertThat(lines.calculateExtraLineFare(List.of(StationFactory.from(StationFactory.D)))).isEqualTo(0);
        }
    }
}
