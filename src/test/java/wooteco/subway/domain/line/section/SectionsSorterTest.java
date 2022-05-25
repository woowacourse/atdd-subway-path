package wooteco.subway.domain.line.section;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static wooteco.subway.TestFixture.강남_역삼;
import static wooteco.subway.TestFixture.선릉_삼성;
import static wooteco.subway.TestFixture.역삼_선릉;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class SectionsSorterTest {

    private SectionSorter sectionSorter;

    @BeforeEach
    void setUp() {
        this.sectionSorter = new SectionSorter();
    }

    @DisplayName("구간을 정렬한다.")
    @Test
    void create() {
        List<Section> sections = List.of(역삼_선릉, 강남_역삼);

        List<Section> actual = sectionSorter.sort(sections);
        assertThat(actual).containsExactly(강남_역삼, 역삼_선릉);
    }

    @DisplayName("구간이 하나도 없다.")
    @Test
    void createWithEmptySections() {
        assertThatThrownBy(() -> sectionSorter.sort(Collections.emptyList()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("지하철구간은 하나 이상이어야 합니다.");
    }

    @DisplayName("정렬되지 않은 구간이 존재한다.")
    @Test
    void createWithNonConnectedSections() {
        List<Section> sections = List.of(강남_역삼, 선릉_삼성);
        assertThatThrownBy(() -> sectionSorter.sort(sections))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("정렬되지 않은 구간이 존재합니다.");
    }
}