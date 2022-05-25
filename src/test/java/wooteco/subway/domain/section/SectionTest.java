package wooteco.subway.domain.section;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import wooteco.subway.domain.station.Station;

@SuppressWarnings("NonAsciiCharacters")
class SectionTest {

    private static final Station 강남역 = new Station(1L, "강남역");
    private static final Station 역삼역 = new Station(2L, "역삼역");
    private static final Station 선릉역 = new Station(3L, "선릉역");
    private static final Station 잠실역 = new Station(4L, "잠실역");

    @DisplayName("생성자 유효성 검정 테스트")
    @Nested
    class InitTest {

        @Test
        void 동일한_지하철역_두개로_구간을_생성하려는_경우_예외_발생() {
            assertThatThrownBy(() -> new Section(강남역, 강남역, 10))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 두_역_사이의_거리가_1미만인_구간을_생성하려는_경우_예외_발생() {
            assertThatThrownBy(() -> new Section(강남역, 역삼역, 0))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @DisplayName("hasStationOf 메서드는 구간에 특정 지하철역이 포함되었는지를 반환")
    @Nested
    class HasStationOfTest {

        @Test
        void 포함된_지하철역인_경우_참() {
            Section section = new Section(강남역, 역삼역, 10);

            boolean actual = section.hasStationOf(강남역);

            assertThat(actual).isTrue();
        }

        @Test
        void 포함되지_않은_지하철역인_경우_거짓() {
            Section section = new Section(강남역, 역삼역, 10);

            boolean actual = section.hasStationOf(선릉역);

            assertThat(actual).isFalse();
        }
    }

    @Test
    void toStations_메서드는_상행역과_하행역을_순서대로_나열한_리스트를_반환() {
        Section section = new Section(강남역, 역삼역, 10);

        List<Station> actual = section.toStations();
        List<Station> expected = List.of(강남역, 역삼역);

        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("toConnectedDistance 메서드는 인접한 두 구간 연결한 거리를 반환")
    @Nested
    class ToConnectedDistanceTest {

        @Test
        void 인접한_구간인_경우_두_구간의_거리합을_반환() {
            Section 강남_역삼 = new Section(강남역, 역삼역, 10);
            Section 역삼_선릉 = new Section(역삼역, 선릉역, 5);

            int actual = 강남_역삼.toConnectedDistance(역삼_선릉);
            int expected = 10 + 5;

            assertThat(actual).isEqualTo(expected);
        }

        @Test
        void 두_구간이_인접할_수_없는_경우_예외발생() {
            Section 강남_역삼 = new Section(강남역, 역삼역, 10);
            Section 선릉_잠실 = new Section(선릉역, 잠실역, 5);

            assertThatThrownBy(() -> 강남_역삼.toConnectedDistance(선릉_잠실))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @DisplayName("toRemainderDistance 메서드는 자신의 위에 덮어지는 짧은 구간과의 거리 차이를 반환")
    @Nested
    class ToRemainderDistanceTest {

        @Test
        void 상행역_혹은_하행역을_공유하는_구간인_경우_현재_구간에서의_거리_차이를_반환() {
            Section 강남_선릉 = new Section(강남역, 선릉역, 10);
            Section 역삼_선릉 = new Section(역삼역, 선릉역, 2);

            int actual = 강남_선릉.toRemainderDistance(역삼_선릉);
            int expected = 10 - 2;

            assertThat(actual).isEqualTo(expected);
        }

        @Test
        void 두_구간이_겹쳐질_수_없는_경우_예외발생() {
            Section 강남_역삼 = new Section(강남역, 역삼역, 10);
            Section 선릉_잠실 = new Section(선릉역, 잠실역, 5);

            assertThatThrownBy(() -> 강남_역삼.toRemainderDistance(선릉_잠실))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 두_구간의_거리가_동일한_경우_예외발생() {
            Section 강남_선릉 = new Section(강남역, 선릉역, 10);
            Section 역삼_선릉 = new Section(역삼역, 선릉역, 10);

            assertThatThrownBy(() -> 강남_선릉.toRemainderDistance(역삼_선릉))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 현재_구간보다_더_긴_구간을_대입한_경우_예외발생() {
            Section 강남_선릉 = new Section(강남역, 선릉역, 10);
            Section 역삼_선릉 = new Section(역삼역, 선릉역, 15);

            assertThatThrownBy(() -> 강남_선릉.toRemainderDistance(역삼_선릉))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @DisplayName("isRegisteredAtLine 메서드는 특정 노선에 등록되었는지를 반환")
    @Nested
    class IsRegisteredAtLineTest {

        @Test
        void 동일한_노선_id_값이_들어온_경우_참_반환() {
            Section section = new Section(1L, 강남역, 선릉역, 10);

            boolean actual = section.isRegisteredAtLine(1L);

            assertThat(actual).isTrue();
        }

        @Test
        void 다른_노선_id_값이_들어온_경우_거짓_반환() {
            Section section = new Section(1L, 강남역, 선릉역, 10);

            boolean actual = section.isRegisteredAtLine(999L);

            assertThat(actual).isFalse();
        }

        @Test
        void 해당_노선의_id가_null인_경우_예외는_발생하지_않으며_거짓_반환() {
            Section section = new Section(null, 강남역, 선릉역, 10);

            boolean actual = section.isRegisteredAtLine(1L);

            assertThat(actual).isFalse();
        }
    }
}
