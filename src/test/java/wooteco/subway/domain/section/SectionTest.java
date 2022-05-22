package wooteco.subway.domain.section;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import wooteco.subway.domain.station.Station;

class SectionTest {

    @DisplayName("hasSameStation 메소드는 두 구간이 하나 이상의 같은 역을 포함하고 있다면 true를 반환한다.")
    @CsvSource(value = {"강남역,잠실역,강남역,잠실역", "강남역,잠실역,선릉역,잠실역", "강남역,잠실역,잠실역,선릉역"})
    @ParameterizedTest
    void hasSameStation(String stationName1, String stationName2, String stationName3, String stationName4) {
        // given

        Section section1 = new Section(new Station(stationName1), new Station(stationName2), new Distance(10));
        Section section2 = new Section(new Station(stationName3), new Station(stationName4), new Distance(10));

        // when
        boolean actual = section1.hasSameStation(section2);

        // then
        assertThat(actual).isTrue();
    }

    @DisplayName("hasSameStation 메소드는 두 구간이 하나도 같은 역이 없다면 false를 반환한다.")
    @CsvSource(value = {"강남역,선릉역,잠실역,잠실새내역", "강남역,선릉역,부평역,서울역", "강남역,선릉역,몽천토성역,삼성역"})
    @ParameterizedTest
    void hasSameStation_returnsFalse(String stationName1, String stationName2, String stationName3,
                                     String stationName4) {
        // given
        Section section1 = new Section(new Station(stationName1), new Station(stationName2), new Distance(10));
        Section section2 = new Section(new Station(stationName3), new Station(stationName4), new Distance(10));

        // when
        boolean actual = section1.hasSameStation(section2);

        // then
        assertThat(actual).isFalse();
    }
}
