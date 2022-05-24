package wooteco.subway.domain.path.section;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.domain.section.Section;

class SectionTest {

    @Test
    @DisplayName("lineId와 stationId에 null값이 들어오면 예외를 반환한다.")
    void validate_null() {
        assertThatThrownBy(() -> new Section(null, null, null, 5))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("노선 아이디와 역 아이디들에는 빈 값이 올 수 없습니다.");
    }

    @Test
    @DisplayName("거리에 0미만의 값이 들어오면 예외를 반환한다.")
    void validate_distanceMinimum() {
        assertThatThrownBy(() -> new Section(1L, 1L, 2L, -1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("거리에는 0이상 값만 올 수 있습니다.");
    }

    @DisplayName("두 구간이 연결된 구간이 아니면 예외를 반환한다.")
    @Test
    void revisedBy_exception() {
        Section _1L_3L_7 = new Section(1L, 1L, 1L, 3L, 7);
        Section _5L_6L_5 = new Section(2L, 1L, 5L, 6L, 5);

        assertThatThrownBy(() -> _1L_3L_7.revisedBy(_5L_6L_5))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("기존 노선과 연결된 구간이 아닙니다.");
    }

    @DisplayName("같은 상행역을 가지고 있으면 수정된 기존역을 반환한다.")
    @Test
    void revisedBy_upStation() {
        Section _2L_3L_7 = new Section(1L, 1L, 2L, 3L, 7);
        Section _2L_4L_5 = new Section(2L, 1L, 2L, 4L, 5);

        Section _4L_3L_2 = _2L_3L_7.revisedBy(_2L_4L_5);

        assertThat(_4L_3L_2.getUpStationId()).isEqualTo(4L);
        assertThat(_4L_3L_2.getDownStationId()).isEqualTo(3L);
        assertThat(_4L_3L_2.getDistance()).isEqualTo(2);
    }

    @DisplayName("같은 하행역을 가지고 있으면 수정된 기존역을 반환한다.")
    @Test
    void revisedBy_downStation() {
        Section _1L_3L_7 = new Section(1L, 1L, 1L, 3L, 7);
        Section _2L_3L_5 = new Section(2L, 1L, 2L, 3L, 5);

        Section _1L_2L_2 = _1L_3L_7.revisedBy(_2L_3L_5);

        assertThat(_1L_2L_2.getUpStationId()).isEqualTo(1L);
        assertThat(_1L_2L_2.getDownStationId()).isEqualTo(2L);
        assertThat(_1L_2L_2.getDistance()).isEqualTo(2);
    }

    @DisplayName("두 구간의 거리를 비교한다.")
    @Test
    void isLongerThan_true() {
        Section _1L_3L_5 = new Section(1L, 1L, 1L, 3L, 5);
        Section _2L_3L_3 = new Section(1L, 1L, 2L, 3L, 3);

        assertThat(_1L_3L_5.isLongerThan(_2L_3L_3)).isTrue();
    }

    @DisplayName("두 구간의 거리를 비교한다.")
    @Test
    void isLongerThan() {
        Section _1L_3L_5 = new Section(1L, 1L, 1L, 3L, 5);
        Section _2L_3L_7 = new Section(1L, 1L, 2L, 3L, 7);

        assertThat(_1L_3L_5.isLongerThan(_2L_3L_7)).isFalse();
    }

    @DisplayName("두 구간에 같은 역이 존재하는지 확인한다. / 참")
    @Test
    void isConnectedTo_true() {
        Section _1L_3L_5 = new Section(1L, 1L, 1L, 3L, 5);
        Section _2L_3L_5 = new Section(1L, 1L, 2L, 3L, 5);

        assertThat(_1L_3L_5.isConnectedTo(_2L_3L_5)).isTrue();
    }

    @DisplayName("두 구간에 같은 역이 존재하는지 확인한다. / 거짓")
    @Test
    void isConnectedTo_false() {
        Section _1L_3L_5 = new Section(1L, 1L, 1L, 3L, 5);
        Section _4L_5L_5 = new Section(1L, 1L, 4L, 5L, 5);

        assertThat(_1L_3L_5.isConnectedTo(_4L_5L_5)).isFalse();
    }

    @DisplayName("두 구간이 갈래길을 생성하는지 확인한다.")
    @Test
    void isOverlappedWith_true() {
        Section _1L_3L_5 = new Section(1L, 1L, 1L, 3L, 5);
        Section _2L_5L_5 = new Section(1L, 1L, 2L, 3L, 5);

        assertThat(_1L_3L_5.isOverLappedWith(_2L_5L_5)).isTrue();
    }

    @DisplayName("두 구간이 갈래길을 생성하는지 확인한다.")
    @Test
    void isOverlappedWith_false() {
        Section _1L_3L_5 = new Section(1L, 1L, 1L, 3L, 5);
        Section _4L_5L_5 = new Section(1L, 1L, 4L, 5L, 5);

        assertThat(_1L_3L_5.isOverLappedWith(_4L_5L_5)).isFalse();
    }

    @DisplayName("구간에 입력과 같은 역 id가 존재하는지 확인한다.")
    @Test
    void hasStation_true() {
        Section _1L_3L_5 = new Section(1L, 1L, 1L, 3L, 5);

        assertThat(_1L_3L_5.hasStation(1L)).isTrue();
    }

    @DisplayName("구간에 입력과 같은 역이 존재하는지 확인한다.")
    @Test
    void hasStation_false() {
        Section _1L_3L_5 = new Section(1L, 1L, 1L, 3L, 5);

        assertThat(_1L_3L_5.hasStation(4L)).isFalse();
    }

    @DisplayName("같은 값을 갖는지 비교한다.")
    @Test
    void hasSameValue_true() {
        Section _1L_3L_5_one = new Section(1L, 1L, 1L, 3L, 5);
        Section _1L_3L_5_two = new Section(1L, 1L, 1L, 3L, 5);

        assertThat(_1L_3L_5_one.hasSameValue(_1L_3L_5_two)).isTrue();
    }
}
