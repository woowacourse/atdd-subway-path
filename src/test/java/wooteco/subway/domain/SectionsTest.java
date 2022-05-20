package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.exception.DomainException;

class SectionsTest {

    Station 상계 = new Station(1L, "상계");
    Station 중계 = new Station(2L, "중계");
    Station 하계 = new Station(3L, "하계");
    Station 노원 = new Station(4L, "노원");

    Section 상계_중계 = new Section(1L, 1L, 상계, 중계, 10);
    Section 중계_하계 = new Section(2L, 1L, 중계, 하계, 10);
    Section 상계_노원 = new Section(3L, 1L, 상계, 노원, 5);
    Sections sections = new Sections(List.of(상계_중계, 중계_하계));
    Sections invalidSection = new Sections(List.of(상계_노원, 중계_하계));

    @BeforeEach
    void setUp() {
        상계 = new Station(1L, "상계");
        중계 = new Station(2L, "중계");
        하계 = new Station(3L, "하계");
        노원 = new Station(4L, "노원");

        중계_하계 = new Section(1L, 1L, 중계, 하계, 10);
        상계_중계 = new Section(2L, 1L, 상계, 중계, 10);
        상계_노원 = new Section(3L, 1L, 상계, 노원, 5);
        sections = new Sections(List.of(상계_중계, 중계_하계));
    }

    @Test
    @DisplayName("상계-중계-하계에 상계-노원 구간 추가하기")
    void add() {
        sections.add(상계_노원);

        assertThat(sections.getValue()).hasSize(3);
    }

    @Test
    @DisplayName("상계-중계-하계 순서대로 역 반환")
    void getSortedStation() {
        List<Station> sorted = sections.getSortedStation();

        assertThat(sorted).containsExactly(상계, 중계, 하계);
    }

    @Test
    @DisplayName("상계-중계, 중계-하계 에서 순서대로 역반환 시도시 예외")
    void getSortedStation_unconnected() {
        // given
        assertThatThrownBy(() -> invalidSection.getSortedStation())
                .isInstanceOf(DomainException.class);
    }

    @Test
    @DisplayName("상계-중계-하계에서 중계 제거")
    void deleteNearBy() {
        sections.deleteNearBy(중계);

        assertThat(sections.getValue()).hasSize(1);
        assertThat(sections.getValue().get(0).getDistance())
                .isEqualTo(중계_하계.getDistance() + 상계_중계.getDistance());
    }
}
