package wooteco.subway.domain.section;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ConcreteCreationStrategyTest {

    private final ConcreteCreationStrategy concreteCreationStrategy = new ConcreteCreationStrategy();

    private final Section _1L_2L_10 = new Section(1L, 1L, 1L, 2L, 10);
    private final Section _2L_3L_10 = new Section(2L, 1L, 2L, 3L, 10);

    private List<Section> sections;

    @BeforeEach
    void setUp() {
        sections = new ArrayList<>(List.of(_1L_2L_10, _2L_3L_10));
    }

    @Test
    @DisplayName("구간의 상행역이 하행종착역이고, 하행역이 존재하지 않는 경우 구간생성 성공")
    void save_success1() {
        Section _3L_4L_10 = new Section(1L, 3L, 4L, 10);

        concreteCreationStrategy.save(sections, _3L_4L_10);

        assertThat(sections).containsExactly(_1L_2L_10, _2L_3L_10, _3L_4L_10);
    }

    @Test
    @DisplayName("구간의 하행역이 상행종착역이고, 상행역이 존재하지 않는 경우 구간생성 성공")
    void save_success2() {
        Section _4L_1L_10 = new Section(1L, 4L, 1L, 10);

        concreteCreationStrategy.save(sections, _4L_1L_10);

        assertThat(sections).containsExactly(_1L_2L_10, _2L_3L_10, _4L_1L_10);
    }

    @Test
    @DisplayName("상행역과 하행역이 이미 존재하는 경우 예외를 반환")
    void save_existException() {
        assertThatThrownBy(() -> concreteCreationStrategy.save(sections, _1L_2L_10))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("상행역과 하행역이 이미 모두 존재합니다.");
    }

    @Test
    @DisplayName("기존 노선과 연결된 구간이 아니면 예외 반환")
    void save_notConnectedException() {
        Section _5L_6L_10 = new Section(1L, 5L, 6L, 10);

        assertThatThrownBy(() -> concreteCreationStrategy.save(sections, _5L_6L_10))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("기존 노선과 연결된 구간이 아닙니다.");
    }

    @Test
    @DisplayName("새로운 구간의 거리가 추가할 수 없는 거리면 예외 반환")
    void save_distanceException() {
        Section _1L_4L_10 = new Section(1L, 1L, 4L, 10);

        assertThatThrownBy(() -> concreteCreationStrategy.save(sections, _1L_4L_10))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("적절한 거리가 아닙니다.");
    }

    @Test
    @DisplayName("갈래길이 발생하면 거리가 수정된 기존 구간을 반환")
    void fixOverLappedSection_revisedSection() {
        Section _1L_4L_4 = new Section(3L, 1L, 1L, 4L, 4);

        Section revisedSection = concreteCreationStrategy.fixOverLappedSection(sections, _1L_4L_4)
                .orElseThrow();

        assertThat(revisedSection.getDistance()).isEqualTo(6);
    }

    @Test
    @DisplayName("갈래길이 발생하면 수정된 거리를 포함한 모든 구간이 변환된다. ")
    void fixOverLappedSection_section() {
        Section _1L_4L_4 = new Section(3L, 1L, 1L, 4L, 4);

        concreteCreationStrategy.save(sections, _1L_4L_4);
        Section revisedSection = concreteCreationStrategy.fixOverLappedSection(sections, _1L_4L_4)
                .orElseThrow();

        assertThat(sections).containsExactly(_2L_3L_10, _1L_4L_4, revisedSection);
    }

    @Test
    @DisplayName("갈래길이 발생하지 않으면 Optional.empty를 반환")
    void fixOverLappedSection_empty() {
        Section _3L_4L_4 = new Section(2L, 1L, 3L, 4L, 4);

        Optional<Section> revisedSection = concreteCreationStrategy.fixOverLappedSection(sections, _3L_4L_4);
        assertThat(revisedSection.isPresent()).isFalse();
    }
}