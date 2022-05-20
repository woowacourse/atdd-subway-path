package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.exception.InvalidSectionInsertException;

class LineSectionsTest {

    @Test
    @DisplayName("상행역 id와 하행역 id, 거리를 입력받아 구간이 추가될 수 있는 지 검사시에 이미 존재하는 두 역을 사용하면 예외가 발생한다.")
    void validateSectionBothStationExist() {
        final Section section1 = new Section(1L, 1L, 1L, 2L, 10, 1L);
        final Section section2 = new Section(2L, 1L, 2L, 3L, 10, 2L);
        final Section section3 = new Section(3L, 1L, 3L, 4L, 10, 3L);

        final LineSections lineSections = new LineSections(List.of(section1, section2, section3));

        assertThatThrownBy(() -> lineSections.validateSection(1L, 3L, 5))
                .isInstanceOf(InvalidSectionInsertException.class)
                .hasMessageContaining("상행, 하행이 대상 노선에 둘 다 존재합니다.");
    }

    @Test
    @DisplayName("상행역 id와 하행역 id, 거리를 입력받아 구간이 추가될 수 있는 지 검사시에 존재하지 않는 두 역을 사용하면 예외가 발생한다.")
    void validateSectionNoneStationExist() {
        final Section section1 = new Section(1L, 1L, 1L, 2L, 10, 1L);
        final Section section2 = new Section(2L, 1L, 2L, 3L, 10, 2L);
        final Section section3 = new Section(3L, 1L, 3L, 4L, 10, 3L);

        final LineSections lineSections = new LineSections(List.of(section1, section2, section3));

        assertThatThrownBy(() -> lineSections.validateSection(5L, 6L, 5))
                .isInstanceOf(InvalidSectionInsertException.class)
                .hasMessageContaining("상행, 하행이 대상 노선에 둘 다 존재하지 않습니다.");
    }

    @Test
    @DisplayName("상행역 id와 하행역 id, 거리를 입력받아 구간이 추가될 수 있는 지 검사시에"
            + " 기존 구간 사이에 새 역이 등록될 시 기존 구간보다 거리가 길거나 같으면 예외가 발생한다.")
    void validateSectionTooLongDistance() {
        final Section section1 = new Section(1L, 1L, 1L, 2L, 10, 1L);
        final Section section2 = new Section(2L, 1L, 2L, 3L, 10, 2L);
        final Section section3 = new Section(3L, 1L, 3L, 4L, 10, 3L);

        final LineSections lineSections = new LineSections(List.of(section1, section2, section3));

        assertThatThrownBy(() -> lineSections.validateSection(1L, 5L, 10))
                .isInstanceOf(InvalidSectionInsertException.class)
                .hasMessageContaining("역 사이에 새로운 역을 등록할 경우, 기존 역 사이 길이보다 크거나 같으면 등록할 수 없습니다.");
    }

    @Test
    @DisplayName("추가되는 구간 정보를 입력받아 새로 등록될 구간 정보를 반환한다.")
    void findOverlapSection() {

    }

    @Test
    void getStationsId() {
    }

    @Test
    void hasTwoSection() {
    }

    @Test
    void getSingleDeleteSection() {
    }

    @Test
    void getUpsideSection() {
    }

    @Test
    void getDownsideSection() {
    }
}