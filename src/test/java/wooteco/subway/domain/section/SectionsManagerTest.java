package wooteco.subway.domain.section;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import wooteco.subway.domain.station.Station;

@SuppressWarnings("NonAsciiCharacters")
class SectionsManagerTest {

    private final Station 강남역 = new Station(1L, "강남역");
    private final Station 선릉역 = new Station(2L, "선릉역");
    private final Station 잠실역 = new Station(3L, "잠실역");
    private final Station 역삼역 = new Station(4L, "역삼역");
    private final Station 청계산입구역 = new Station(5L, "청계산입구역");

    @DisplayName("save 메서드는 특정 구간을 추가한 후 기존 구간들을 재조정한 일급컬렉션을 반환")
    @Nested
    class SaveTest {

        @Test
        void 기존_상행종점에_새로운_상행_종점을_연결하는_경우_재조정_작업_없이_추가_후_반환() {
            Section existingSection = new Section(선릉역, 잠실역, 10);
            Section newUpperSection = new Section(강남역, 선릉역, 10);
            SectionsManager sectionsManager = createSectionsManager(existingSection);

            Sections actual = sectionsManager.save(newUpperSection);
            Sections expected = new Sections(List.of(newUpperSection, existingSection));

            assertThat(actual).isEqualTo(expected);
        }

        @Test
        void 기존_하행종점에_새로운_하행_종점을_연결하는_경우_재조정_작업_없이_추가_후_반환() {
            Section existingSection = new Section(강남역, 선릉역, 10);
            Section newLowerSection = new Section(선릉역, 잠실역, 10);
            SectionsManager sectionsManager = createSectionsManager(existingSection);

            Sections actual = sectionsManager.save(newLowerSection);
            Sections expected = new Sections(List.of(existingSection, newLowerSection));

            assertThat(actual).isEqualTo(expected);
        }

        @Test
        void 기존_구간_사이에_구간_추가시_덮어써지는_구간의_거리_재조정_후_반환() {
            Section existingSection = new Section(강남역, 잠실역, 10);
            SectionsManager sectionsManager = createSectionsManager(existingSection);
            Section inBetweenSection = new Section(강남역, 선릉역, 3);

            Sections actual = sectionsManager.save(inBetweenSection);
            Sections expected = new Sections(List.of(
                    new Section(강남역, 선릉역, 3),
                    new Section(선릉역, 잠실역, 7)));

            assertThat(actual).isEqualTo(expected);
        }

        @Test
        void 구간들에_등록되지_않은_지하철역들로_구성된_구간_추가_시도시_예외발생() {
            SectionsManager sectionsManager = createSectionsManager(new Section(강남역, 역삼역, 10));
            Section noneRegisteredStationsSection = new Section(선릉역, 잠실역, 5);

            assertThatThrownBy(() -> sectionsManager.save(noneRegisteredStationsSection))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 이미_등록된_지하철역들로만_구성된_구간_추가_시도시_예외발생() {
            Section section1 = new Section(강남역, 선릉역, 10);
            Section section2 = new Section(선릉역, 잠실역, 10);
            SectionsManager sectionsManager = createSectionsManager(section1, section2);
            Section bothRegisteredStationsSection = new Section(강남역, 잠실역, 15);

            assertThatThrownBy(() -> sectionsManager.save(bothRegisteredStationsSection))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("거리 유효성 검증")
        @Nested
        class DistanceValidationTest {

            @Test
            void 상행종점에_새로운_상행_종점을_연결하는_경우_거리는_1이상이면_무조건_허용() {
                Section existingSection = new Section(선릉역, 잠실역, 10);
                Section newUpperSection = new Section(강남역, 선릉역, 9999999);
                SectionsManager sectionsManager = createSectionsManager(existingSection);

                assertThatNoException()
                        .isThrownBy(() -> sectionsManager.save(newUpperSection));
            }

            @Test
            void 하행종점에_새로운_하행_종점을_연결하는_경우_거리는_1이상이면_무조건_허용() {
                Section existingSection = new Section(강남역, 선릉역, 10);
                Section newLowerSection = new Section(선릉역, 잠실역, 9999999);
                SectionsManager sectionsManager = createSectionsManager(existingSection);

                assertThatNoException()
                        .isThrownBy(() -> sectionsManager.save(newLowerSection));
            }

            @Test
            void 기존_구간_위에_다른_구간_등록시_기존_구간과_거리가_동일하면_예외() {
                Section existingSection = new Section(강남역, 잠실역, 10);
                Section newCoveringSection = new Section(강남역, 선릉역, 10);
                SectionsManager sectionsManager = createSectionsManager(existingSection);

                assertThatThrownBy(() -> sectionsManager.save(newCoveringSection))
                        .isInstanceOf(IllegalArgumentException.class);
            }

            @Test
            void 기존_구간_위에_다른_구간_등록시_기존_구간보다_거리가_크면_예외() {
                Section existingSection = new Section(강남역, 잠실역, 10);
                Section newCoveringSection = new Section(강남역, 선릉역, 11);
                SectionsManager sectionsManager = createSectionsManager(existingSection);

                assertThatThrownBy(() -> sectionsManager.save(newCoveringSection))
                        .isInstanceOf(IllegalArgumentException.class);
            }
        }
    }

    @DisplayName("delete 메서드는 특정 지하철역을 제거한 후 구간들을 재조정한 일급컬렉션을 반환")
    @Nested
    class DeleteTest {

        @Test
        void 상행_종점_제거시_재조정_작업_없이_연결된_구간만_제거() {
            Section 강남_선릉_구간 = new Section(강남역, 선릉역, 10);
            Section 선릉_잠실_구간 = new Section(선릉역, 잠실역, 10);
            SectionsManager sectionsManager = createSectionsManager(강남_선릉_구간, 선릉_잠실_구간);

            Sections actual = sectionsManager.delete(강남역);
            Sections expected = new Sections(List.of(선릉_잠실_구간));

            assertThat(actual).isEqualTo(expected);
        }

        @Test
        void 하행_종점_제거시_재조정_작업_없이_연결된_구간만_제거() {
            Section 강남_선릉_구간 = new Section(강남역, 선릉역, 10);
            Section 선릉_잠실_구간 = new Section(선릉역, 잠실역, 10);
            SectionsManager sectionsManager = createSectionsManager(강남_선릉_구간, 선릉_잠실_구간);

            Sections actual = sectionsManager.delete(잠실역);
            Sections expected = new Sections(List.of(강남_선릉_구간));

            assertThat(actual).isEqualTo(expected);
        }

        @Test
        void 구간들_중앙의_지하철역_제거시_인접한_두_구간을_합친_후_반환() {
            Section 강남_선릉_구간 = new Section(강남역, 선릉역, 10);
            Section 선릉_잠실_구간 = new Section(선릉역, 잠실역, 5);
            SectionsManager sectionsManager = createSectionsManager(강남_선릉_구간, 선릉_잠실_구간);

            Sections actual = sectionsManager.delete(선릉역);
            Sections expected = new Sections(List.of(new Section(강남역, 잠실역, 15)));

            assertThat(actual).isEqualTo(expected);
        }

        @Test
        void 구간들에_등록되지_않은_지하철역_제거_시도시_예외발생() {
            Section 강남_선릉_구간 = new Section(강남역, 선릉역, 10);
            Section 선릉_잠실_구간 = new Section(선릉역, 잠실역, 5);
            SectionsManager sectionsManager = createSectionsManager(강남_선릉_구간, 선릉_잠실_구간);

            assertThatThrownBy(() -> sectionsManager.delete(역삼역))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 마지막_구간_제거_시도시_예외발생() {
            Section 유일하게_등록된_구간 = new Section(강남역, 선릉역, 10);
            SectionsManager sectionsManager = createSectionsManager(유일하게_등록된_구간);

            assertThatThrownBy(() -> sectionsManager.delete(강남역))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @DisplayName("compareDifference 메서드는 수정된 구간 정보를 받아, 생겨난 구간들과 없어진 구간들의 정보를 반환")
    @Nested
    class CompareDifferenceTest {

        private final Section 강남_선릉_구간 = new Section(강남역, 선릉역, 10);
        private final Section 선릉_청계산_구간 = new Section(선릉역, 청계산입구역, 10);
        private final Section 청계산_역삼_구간 = new Section(청계산입구역, 역삼역, 10);

        @Test
        void 인자로_들어온_구간들에서_새로_생겨났거나_없어진_구간들의_정보를_반환() {
            SectionsManager sectionsManager = createSectionsManager(강남_선릉_구간, 선릉_청계산_구간);
            Section 선릉_잠실_신규_구간 = new Section(선릉역, 잠실역, 5);
            Section 잠실_청계산_신규_구간 = new Section(잠실역, 청계산입구역, 5);
            Sections updatedSections = new Sections(List.of(강남_선릉_구간, 선릉_잠실_신규_구간, 잠실_청계산_신규_구간));

            SectionUpdates actual = sectionsManager.compareDifference(updatedSections);
            List<Section> expectedNewSections = List.of(선릉_잠실_신규_구간, 잠실_청계산_신규_구간);
            List<Section> expectedOldSections = List.of(선릉_청계산_구간);
            SectionUpdates expected = new SectionUpdates(expectedNewSections, expectedOldSections);

            assertThat(actual).isEqualTo(expected);
        }

        @Test
        void 생성된_구간만_존재하는_경우_제거된_구간들의_정보에는_빈_리스트_반환() {
            SectionsManager sectionsManager = createSectionsManager(강남_선릉_구간, 선릉_청계산_구간);
            Sections 종점이_추가된_구간들 = new Sections(List.of(강남_선릉_구간, 선릉_청계산_구간, 청계산_역삼_구간));

            SectionUpdates actual = sectionsManager.compareDifference(종점이_추가된_구간들);
            List<Section> expectedNewSections = List.of(청계산_역삼_구간);
            List<Section> expectedOldSections = List.of();
            SectionUpdates expected = new SectionUpdates(expectedNewSections, expectedOldSections);

            assertThat(actual).isEqualTo(expected);
        }

        @Test
        void 제거된_구간만_존재하는_경우_새로운_구간들의_정보에는_빈_리스트_반환() {
            SectionsManager sectionsManager = createSectionsManager(강남_선릉_구간, 선릉_청계산_구간, 청계산_역삼_구간);
            Sections 종점이_제거된_구간들 = new Sections(List.of(강남_선릉_구간, 선릉_청계산_구간));

            SectionUpdates actual = sectionsManager.compareDifference(종점이_제거된_구간들);
            List<Section> expectedNewSections = List.of();
            List<Section> expectedOldSections = List.of(청계산_역삼_구간);
            SectionUpdates expected = new SectionUpdates(expectedNewSections, expectedOldSections);

            assertThat(actual).isEqualTo(expected);
        }

        @Test
        void 현재와_동일한_구간들이_들어오더라도_예외는_발생하지_않으며_빈_리스트들을_반환() {
            List<Section> sameSectionList = List.of(강남_선릉_구간, 선릉_청계산_구간, 청계산_역삼_구간);
            SectionsManager sectionsManager = new SectionsManager(sameSectionList);
            Sections sections  = new Sections(sameSectionList);

            SectionUpdates actual = sectionsManager.compareDifference(sections);
            SectionUpdates expected = new SectionUpdates(List.of(), List.of());

            assertThat(actual).isEqualTo(expected);
        }
    }

    private SectionsManager createSectionsManager(Section... sections){
        return new SectionsManager(List.of(sections));
    }
}
