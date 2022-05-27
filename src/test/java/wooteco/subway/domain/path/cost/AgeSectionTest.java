package wooteco.subway.domain.path.cost;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

public class AgeSectionTest {

    @Test
    void 여섯살_이하인_경우_예외_발생() {
        assertThatThrownBy(() -> AgeSection.calculateByAge(3000, 5)).
                isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 최대_나이인_200세_초과인_경우_예외_발생() {
        assertThatThrownBy(() -> AgeSection.calculateByAge(3000, 201)).
                isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 어린이_나이인_6세_이상_13세_미만인_경우() {
        assertThat(AgeSection.calculateByAge(1350, 6)).isEqualTo(500);
    }

    @Test
    void 청소년_나이인_13세_이상_19세_미만인_경우() {
        assertThat(AgeSection.calculateByAge(1350, 13)).isEqualTo(800);
    }

    @Test
    void 성인_나이인_19세_이상인_경우() {
        assertThat(AgeSection.calculateByAge(1350, 19)).isEqualTo(1350);
    }
}
