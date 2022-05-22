package wooteco.subway.domain.path.cost;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.Test;

public class CostManagerTest {

    @Test
    void 구간이_3개이고_운행거리가_1번째_구간에_속할_때() {
        CostManager costManager = new CostManager(List.of(new CostSection(50, 8, 100),
                new CostSection(10, 5, 100)));
        int result = costManager.calculateFare(9, 0);
        assertThat(result).isEqualTo(1250);
    }

    @Test
    void 구간이_3개이고_운행거리가_0일_때() {
        CostManager costManager = new CostManager(List.of(new CostSection(50, 8, 100),
                new CostSection(10, 5, 100)));
        int result = costManager.calculateFare(0, 0);
        assertThat(result).isEqualTo(0);
    }

    @Test
    void 구간이_3개이고_운행거리가_2번째_구간의_경계값에_속할_때() {
        CostManager costManager = new CostManager(List.of(new CostSection(50, 8, 100),
                new CostSection(10, 5, 100)));
        int result = costManager.calculateFare(49, 0);
        assertThat(result).isEqualTo(2050);
    }

    @Test
    void 구간이_3개이고_운행거리가_2번째_구간에_속할_때() {
        CostManager costManager = new CostManager(List.of(new CostSection(50, 8, 100),
                new CostSection(10, 5, 100)));
        int result = costManager.calculateFare(27, 0);
        assertThat(result).isEqualTo(1650);
    }

    @Test
    void 구간이_3개이고_운행거리가_3번째_구간의_경계값에_속할_때() {
        CostManager costManager = new CostManager(List.of(new CostSection(50, 8, 100),
                new CostSection(10, 5, 100)));
        int result = costManager.calculateFare(51, 0);
        assertThat(result).isEqualTo(2150);
    }

    @Test
    void 구간이_3개이고_운행거리가_3번째_구간에_속할_때() {
        CostManager costManager = new CostManager(List.of(new CostSection(50, 8, 100),
                new CostSection(10, 5, 100)));
        int result = costManager.calculateFare(60, 0);
        assertThat(result).isEqualTo(2250);
    }

}
