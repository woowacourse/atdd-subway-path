package wooteco.subway.domain.section;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import wooteco.subway.domain.station.Station;
import wooteco.subway.exception.NotFoundException;

@SuppressWarnings("NonAsciiCharacters")
class SectionsTest {

    private final Station 강남역 = new Station(1L, "강남역");
    private final Station 역삼역 = new Station(2L, "역삼역");
    private final Station 잠실역 = new Station(3L, "잠실역");
    private final Station 선릉역 = new Station(4L, "선릉역");
    private final Station 양재역 = new Station(5L, "양재역");

    @DisplayName("생성자는 상행종점부터 하행종점까지 지하철역들 순서로 구간들을 정렬한 일급컬렉션을 생성")
    @Nested
    class InitTest {

        @Test
        void 인자로_들어오는_구간들의_순서와_무관하게_언제나_동일한_순서로_구간들을_정렬하여_인스턴스_생성() {
            Section 강남_잠실 = new Section(강남역, 잠실역, 10);
            Section 잠실_선릉 = new Section(잠실역, 선릉역, 10);
            Section 선릉_양재 = new Section(선릉역, 양재역, 10);
            List<Section> 임의의_순서로_섞인_구간들 = List.of(잠실_선릉, 선릉_양재, 강남_잠실);
            List<Section> 임의의_순서로_섞인_구간들2 = List.of(강남_잠실, 선릉_양재, 잠실_선릉);

            Sections section1 = new Sections(임의의_순서로_섞인_구간들);
            Sections section2 = new Sections(임의의_순서로_섞인_구간들2);

            assertThat(section1).isEqualTo(section2);
        }

        @Test
        void 빈_구간_리스트로_생성하려는_경우_예외_발생() {
            assertThatThrownBy(() -> new Sections(List.of()))
                    .isInstanceOf(NotFoundException.class);
        }

        @Test
        void 연결되지_않은_구간들의_리스트가_들어오면_예외발생() {
            Section 강남_역삼 = new Section(강남역, 역삼역, 10);
            Section 잠실_선릉 = new Section(잠실역, 선릉역, 10);
            List<Section> disConnectedSections = List.of(강남_역삼, 잠실_선릉);

            assertThatThrownBy(() -> new Sections(disConnectedSections))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @DisplayName("isNewEndSection 메서드는 특정 구간을 해당 노선의 종점에 연결할 수 있는지의 여부를 반환")
    @Nested
    class IsNewEndSectionTest {

        @Test
        void 해당_구간을_상행_종점의_끝에_연결할_수_있는_경우_참_반환() {
            Section 강남_역삼 = new Section(강남역, 역삼역, 10);
            Section 역삼_잠실 = new Section(역삼역, 잠실역, 10);
            Section 잠실_선릉 = new Section(잠실역, 선릉역, 10);
            Sections sections = new Sections(List.of(역삼_잠실, 잠실_선릉));

            boolean actual = sections.isNewEndSection(강남_역삼);

            assertThat(actual).isTrue();
        }

        @Test
        void 해당_구간을_하행_종점의_끝에_연결할_수_있는_경우_참_반환() {
            Section 강남_역삼 = new Section(강남역, 역삼역, 10);
            Section 역삼_잠실 = new Section(역삼역, 잠실역, 10);
            Section 잠실_선릉 = new Section(잠실역, 선릉역, 10);
            Sections sections = new Sections(List.of(강남_역삼, 역삼_잠실));

            boolean actual = sections.isNewEndSection(잠실_선릉);

            assertThat(actual).isTrue();
        }

        @Test
        void 노선의_끝에_등록할_수_없는_구간인_경우_거짓_반환() {
            Section 강남_잠실 = new Section(강남역, 잠실역, 10);
            Section 잠실_양재 = new Section(잠실역, 양재역, 10);
            Sections sections = new Sections(List.of(강남_잠실, 잠실_양재));
            Section 역삼_잠실 = new Section(역삼역, 잠실역, 5);

            boolean actual = sections.isNewEndSection(역삼_잠실);

            assertThat(actual).isFalse();
        }
    }

    @DisplayName("findUpperSectionOfStation 메서드는 특정 지하철역이 하행역으로 등록된 상행 노선을 조회한다")
    @Nested
    class FindUpperSectionOfStationTest {

        @Test
        void 특정_지하철이_등록된_상행_노선이_존재하면_조회하여_반환() {
            Section 강남_역삼 = new Section(강남역, 역삼역, 10);
            Section 역삼_잠실 = new Section(역삼역, 잠실역, 10);
            Sections sections = new Sections(List.of(강남_역삼, 역삼_잠실));

            Section actual = sections.findUpperSectionOfStation(역삼역);

            assertThat(actual).isEqualTo(강남_역삼);
        }

        @Test
        void 데이터가_존재하지_않는_경우_예외발생() {
            Sections sections = new Sections(List.of(new Section(강남역, 역삼역, 10)));

            assertThatThrownBy(() -> sections.findUpperSectionOfStation(강남역))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @DisplayName("findLowerSectionOfStation 메서드는 특정 지하철역이 상행역으로 등록된 하행 노선을 조회한다")
    @Nested
    class FindLowerSectionOfStationTest {

        @Test
        void 특정_지하철이_등록된_하행_노선이_존재하면_조회하여_반환() {
            Section 강남_역삼 = new Section(강남역, 역삼역, 10);
            Section 역삼_잠실 = new Section(역삼역, 잠실역, 10);
            Sections sections = new Sections(List.of(강남_역삼, 역삼_잠실));

            Section actual = sections.findLowerSectionOfStation(역삼역);

            assertThat(actual).isEqualTo(역삼_잠실);
        }

        @Test
        void 데이터가_존재하지_않는_경우_예외발생() {
            Sections sections = new Sections(List.of(new Section(강남역, 역삼역, 10)));

            assertThatThrownBy(() -> sections.findLowerSectionOfStation(역삼역))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @DisplayName("isRegistered 메서드는 특정 지하철역이 노선에 등록되었는지를 체크하여 반환")
    @Nested
    class IsRegisteredTest {

        @Test
        void 등록된_지하철역인_경우_참_반환() {
            Sections sections = new Sections(List.of(new Section(강남역, 잠실역, 10)));

            boolean actual = sections.isRegistered(잠실역);

            assertThat(actual).isTrue();
        }

        @Test
        void 등록되지_않은_지하철역인_경우_거짓_반환() {
            Sections sections = new Sections(List.of(new Section(강남역, 잠실역, 10)));

            boolean actual = sections.checkMiddleStation(역삼역);

            assertThat(actual).isFalse();
        }
    }

    @DisplayName("checkMiddleStation 메서드는 구간들 사이에 존재하는 지하철역인지의 여부를 반환")
    @Nested
    class CheckMiddleStationTest {

        @Test
        void 등록되었으나_종점은_아닌_지하철역인_경우_참_반환() {
            Sections sections = new Sections(List.of(
                    new Section(강남역, 잠실역, 10),
                    new Section(잠실역, 선릉역, 10)));

           boolean actual = sections.checkMiddleStation(잠실역);

            assertThat(actual).isTrue();
        }

        @Test
        void 종점으로_등록된_경우_거짓_반환() {
            Sections sections = new Sections(List.of(new Section(강남역, 잠실역, 10)));

            boolean actual = sections.checkMiddleStation(강남역);

            assertThat(actual).isFalse();
        }

        @Test
        void 등록되지_않은_경우_예외가_발생하지_않고_거짓_반환() {
            Sections sections = new Sections(List.of(new Section(강남역, 잠실역, 10)));

            boolean actual = sections.checkMiddleStation(역삼역);

            assertThat(actual).isFalse();
        }
    }

    @Test
    void toSortedStations_메서드는_상행종점부터_하행종점까지_정렬된_지하철역들의_리스트를_반환() {
        Sections sections = new Sections(List.of(
                new Section(강남역, 잠실역, 10),
                new Section(잠실역, 선릉역, 10),
                new Section(선릉역, 양재역, 10)));

        List<Station> actual = sections.toSortedStations();
        List<Station> expected = List.of(강남역, 잠실역, 선릉역, 양재역);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void toSortedList_메서드는_상행종점부터_하행종점까지_정렬된_구간들의_리스트를_반환() {
        Section 강남_잠실 = new Section(강남역, 잠실역, 10);
        Section 잠실_선릉 = new Section(잠실역, 선릉역, 10);
        Section 선릉_양재 = new Section(선릉역, 양재역, 10);
        List<Section> sectionsInRandomOrder = List.of(선릉_양재, 강남_잠실, 잠실_선릉);
        Sections sections = new Sections(sectionsInRandomOrder);

        List<Section> actual = sections.toSortedList();
        List<Section> expected = List.of(강남_잠실, 잠실_선릉, 선릉_양재);

        assertThat(actual).isEqualTo(expected);
    }
}
