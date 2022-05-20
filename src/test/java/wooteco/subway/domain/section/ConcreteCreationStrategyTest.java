package wooteco.subway.domain.section;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ConcreteCreationStrategyTest {

    private final ConcreteCreationStrategy concreteCreationStrategy = new ConcreteCreationStrategy();
    private List<Section> sections;

    @BeforeEach
    void setUp() {
        sections = new ArrayList<>(List.of(new Section(1L, 1L, 1L, 2L, 10),
                new Section(2L, 1L, 2L, 3L, 5)));
    }

    @Test
    @DisplayName("상행역과 하행역이 이미 존재하는 경우 예외를 반환")
    void save() {
        Section section = new Section(1L, 1L, 2L, 10);

        assertThatThrownBy(() -> concreteCreationStrategy.save(sections, section))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("상행역과 하행역이 이미 모두 존재합니다.");
    }

    @Test
    @DisplayName("상행역과 하행역이 이미 존재하는 경우 예외 반환")
    void save_existenceException() {
        Section section = new Section(1L, 1L, 2L, 10);

        assertThatThrownBy(() -> concreteCreationStrategy.save(sections, section))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("상행역과 하행역이 이미 모두 존재합니다.");
    }

    @Test
    @DisplayName("기존 노선과 연결된 구간이 아니면 예외 반환")
    void save_notConnectedException() {
        Section section = new Section(1L, 5L, 6L, 10);

        assertThatThrownBy(() -> concreteCreationStrategy.save(sections, section))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("기존 노선과 연결된 구간이 아닙니다.");
    }

    @Test
    @DisplayName("새로운 구간의 거리가 추가할 수 없는 거리면 예외 반환")
    void save_distanceException() {
        Section section = new Section(1L, 1L, 4L, 10);

        assertThatThrownBy(() -> concreteCreationStrategy.save(sections, section))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("적절한 거리가 아닙니다.");
    }

    @Test
    @DisplayName("갈래길이 발생하면 거리가 수정된 기존 구간을 반환")
    void fixOverLappedSection() {
        Section section = new Section(2L, 1L, 1L, 4L, 4);

        Section revisedSection = concreteCreationStrategy.fixOverLappedSection(sections, section)
                .orElseThrow();

        assertThat(revisedSection.getDistance()).isEqualTo(6);
    }

    @Test
    @DisplayName("갈래길이 발생하지 않으면 Optional.empty를 반환")
    void fixOverLappedSection_empty() {
        Section section = new Section(2L, 1L, 3L, 4L, 4);

        Optional<Section> revisedSection = concreteCreationStrategy.fixOverLappedSection(sections, section);
        assertThat(revisedSection.isPresent()).isFalse();
    }
}