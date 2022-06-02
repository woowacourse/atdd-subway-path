package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class SectionTest {

    @DisplayName("상행과 하행의 id값이 같은 경우 에러를 발생시킨다")
    @Test
    void validateErrorBySameStationId() {
        Station upStation = new Station(1L, "상행");

        assertThatThrownBy(() -> new Section(1L, upStation, upStation, 10))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("상행과 하행의 지하철 역이 같을 수 없습니다.");
    }

    @DisplayName("거리가 0이하인 경우 에러를 발생시킨다.")
    @Test
    void validateErrorByNonPositiveDistance() {
        Station upStation = new Station(1L, "상행");
        Station downStation = new Station(2L, "하행");

        assertThatThrownBy(() -> new Section(1L, upStation, downStation, -1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("거리는 양수여야 합니다.");
    }

    @DisplayName("입력한 Section을 기준으로 기존 Section을 나눈다 - 상행 지하철 역이 겹치는 경우")
    @Test
    void splitSectionSameUpStation() {
        //given
        Station 선릉 = new Station(1L, "선릉");
        Station 잠실 = new Station(2L, "잠실");
        Station 잠실나루 = new Station(3L, "잠실나루");

        Section 선릉_잠실 = new Section(선릉, 잠실, 10);
        Section 선릉_잠실나루 = new Section(선릉, 잠실나루, 4);
        Section 잠실나루_잠실 = new Section(잠실나루, 잠실, 6);

        //when
        Section splitSection = 선릉_잠실.splitSection(선릉_잠실나루);

        //then
        assertAll(
                () -> assertThat(splitSection.getDistance()).isEqualTo(잠실나루_잠실.getDistance()),
                () -> assertThat(splitSection.isSameUpStation(잠실나루_잠실)).isTrue(),
                () -> assertThat(splitSection.isSameDownStation(잠실나루_잠실)).isTrue()
        );
    }

    @DisplayName("입력한 Section을 기준으로 기존 Section을 나눈다 - 하행 지하철 역이 겹치는 경우")
    @Test
    void splitSectionSameDownStation() {
        //given
        Station 선릉 = new Station(1L, "선릉");
        Station 잠실 = new Station(3L, "잠실");
        Station 잠실나루 = new Station(2L, "잠실나루");

        Section 선릉_잠실나루 = new Section(선릉, 잠실나루, 10);
        Section 잠실_잠실나루 = new Section(잠실, 잠실나루, 4);
        Section 선릉_잠실 = new Section(선릉, 잠실, 6);

        //when
        Section splitSection = 선릉_잠실나루.splitSection(잠실_잠실나루);

        //then
        assertAll(
                () -> assertThat(splitSection.getDistance()).isEqualTo(선릉_잠실.getDistance()),
                () -> assertThat(splitSection.isSameUpStation(선릉_잠실)).isTrue(),
                () -> assertThat(splitSection.isSameDownStation(선릉_잠실)).isTrue()
        );
    }

    @DisplayName("나누려는 Section의 distance가 기존 값보다 크거나 같은 경우 에러를 발생시킨다.")
    @Test
    void splitSectionErrorByLongerDistance() {
        Station 선릉 = new Station(1L, "선릉");
        Station 잠실 = new Station(3L, "잠실");
        Station 잠실나루 = new Station(2L, "잠실나루");

        Section 선릉_잠실나루 = new Section(선릉, 잠실나루, 10);
        Section 선릉_잠실 = new Section(선릉, 잠실, 11);

        assertThatThrownBy(() -> 선릉_잠실나루.splitSection(선릉_잠실))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("기존 역 사이보다 긴 길이를 등록할 수 없습니다.");
    }

    @DisplayName("입력된 Section과 upStationId값이 같으면 true를 반환한다.")
    @Test
    void isSameUpStationId() {
        Station 선릉 = new Station(1L, "선릉");
        Station 잠실 = new Station(2L, "잠실");

        Section 선릉_잠실 = new Section(선릉, 잠실, 10);

        assertThat(선릉_잠실.isSameUpStationId(1L)).isTrue();
    }

    @DisplayName("입력된 Section과 downStationId값이 같으면 true를 반환한다.")
    @Test
    void isSameDownStationIdById() {
        Station 선릉 = new Station(1L, "선릉");
        Station 잠실 = new Station(2L, "잠실");

        Section 선릉_잠실 = new Section(선릉, 잠실, 10);

        assertThat(선릉_잠실.isSameDownStationId(2L)).isTrue();
    }
}