package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class SectionsTest {

    @Test
    @DisplayName("상행과 하행이 이미 등록된 경우 에러를 발생시킨다")
    void checkSectionErrorByAlreadyExist() {
        //given
        Station 잠실 = new Station(1L, "잠실");
        Station 선릉 = new Station(2L, "선릉");
        Station 서울숲 = new Station(3L, "서울숲");

        Section 잠실_선릉 = new Section(잠실, 선릉, 10);
        Section 선릉_서울숲 = new Section(선릉, 서울숲, 10);

        Sections sections = new Sections(List.of(잠실_선릉, 선릉_서울숲));

        //then
        assertThatThrownBy(() -> sections.checkSections(잠실_선릉))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("상행역과 하행역이 이미 모두 노선에 등록되어 있습니다.");
    }

    @Test
    @DisplayName("상행과 하행이 존재하지 않는 경우 에러를 발생시킨다")
    void checkSectionErrorByNotExist() {
        //given
        Station 잠실 = new Station(1L, "잠실");
        Station 선릉 = new Station(2L, "선릉");
        Station 서울숲 = new Station(3L, "서울숲");
        Station 강남구청 = new Station(4L, "강남구청");

        Section 잠실_선릉 = new Section(잠실, 선릉, 10);
        Section 서울숲_강남구청 = new Section(서울숲, 강남구청, 10);

        Sections sections = new Sections(List.of(잠실_선릉));

        //then
        assertThatThrownBy(() -> sections.checkSections(서울숲_강남구청))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("상행역과 하행역이 모두 노선에 등록되어 있지 않습니다.");
    }

    @Test
    @DisplayName("해당하는 Section과 겹치는 Section이 없는 경우 null을 return 시킨다.")
    void getTargetSectionToInsertNull() {
        //given
        Station 잠실 = new Station(1L, "잠실");
        Station 선릉 = new Station(2L, "선릉");
        Station 서울숲 = new Station(3L, "서울숲");
        Station 강남구청 = new Station(4L, "강남구청");

        Section 잠실_선릉 = new Section(잠실, 선릉, 10);
        Section 서울숲_강남구청 = new Section(서울숲, 강남구청, 10);

        Sections sections = new Sections(List.of(잠실_선릉));

        //when
        Optional<Section> targetSectionBySection = sections.getTargetSectionToInsert(서울숲_강남구청);

        //then
        assertThat(targetSectionBySection).isEmpty();
    }

    @Test
    @DisplayName("해당하는 Section과 겹치는 Section이 있는 경우 해당 Section을 return한다.")
    void getTargetSectionToInsert() {
        //given
        Station 잠실 = new Station(1L, "잠실");
        Station 선릉 = new Station(2L, "선릉");
        Station 서울숲 = new Station(3L, "서울숲");
        Station 강남구청 = new Station(4L, "강남구청");

        Section 잠실_선릉 = new Section(잠실, 선릉, 10);
        Section 선릉_서울숲 = new Section(선릉, 서울숲, 10);
        Section 선릉_강남구청 = new Section(선릉, 강남구청, 5);

        Sections sections = new Sections(List.of(잠실_선릉, 선릉_서울숲));

        //when
        Optional<Section> targetSectionBySection = sections.getTargetSectionToInsert(선릉_강남구청);

        //then
        assertThat(targetSectionBySection).isEqualTo(Optional.of(선릉_서울숲));
    }

    @Test
    @DisplayName("상하행의 순서에 맞게 id list로 반환한다.")
    void convertToStationIds() {
        //given
        Station 잠실 = new Station(1L, "잠실");
        Station 선릉 = new Station(2L, "선릉");
        Station 서울숲 = new Station(3L, "서울숲");

        Section 잠실_선릉 = new Section(잠실, 선릉, 10);
        Section 선릉_서울숲 = new Section(선릉, 서울숲, 10);

        Sections sections = new Sections(List.of(잠실_선릉, 선릉_서울숲));

        //when
        List<Station> stations = sections.convertToStationIds();

        assertThat(stations).containsOnly(잠실, 선릉, 서울숲);
    }

    @Test
    @DisplayName("sections의 길이가 1인 경우 에러를 발생시킨다.")
    void checkCanDeleteErrorBySizeOne() {
        //given
        Station 잠실 = new Station(1L, "잠실");
        Station 선릉 = new Station(2L, "선릉");

        Section 잠실_선릉 = new Section(잠실, 선릉, 10);

        Sections sections = new Sections(List.of(잠실_선릉));
        //then
        assertThatThrownBy(sections::checkCanDelete)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("해당 노선은 더 삭제할 수 없습니다.");
    }

    @Test
    @DisplayName("삭제하려는 TargetSection을 찾을 때, 대상 section의 개수가 2개가 아니라면 에러가 발생한다.")
    void getMergedTargetSectionToDeleteErrorByWrongSectionSize() {
        //given
        Station 잠실 = new Station(1L, "잠실");
        Station 선릉 = new Station(2L, "선릉");
        Station 서울숲 = new Station(3L, "서울숲");

        Section 잠실_선릉 = new Section(잠실, 선릉, 10);
        Section 선릉_서울숲 = new Section(선릉, 서울숲, 10);

        Sections sections = new Sections(List.of(잠실_선릉, 선릉_서울숲));

        //then
        assertThatThrownBy(() -> sections.getMergedTargetSectionToDelete(1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("대상 Sections의 크기가 올바르지 않습니다.");
    }

    @Test
    @DisplayName("삭제하려는 stationId 값을 통해서 노선에서 해당 id값을 가진 두 노선의 병합된 값을 가질 수 있다.")
    void getMergedTargetSectionToDelete() {
        //given
        Station 잠실 = new Station(1L, "잠실");
        Station 선릉 = new Station(2L, "선릉");
        Station 서울숲 = new Station(3L, "서울숲");

        Section 잠실_선릉 = new Section(잠실, 선릉, 10);
        Section 선릉_서울숲 = new Section(선릉, 서울숲, 10);
        Section 잠실_서울숲 = new Section(잠실, 서울숲, 20);

        Sections sections = new Sections(List.of(잠실_선릉, 선릉_서울숲));

        //when
        Section section = sections.getMergedTargetSectionToDelete(2L);

        //then
        assertAll(
                () -> assertThat(section.getDistance()).isEqualTo(잠실_서울숲.getDistance()),
                () -> assertThat(section.isSameUpStationId(잠실_서울숲.getUpStation().getId())).isTrue(),
                () -> assertThat(section.isSameDownStationId(잠실_서울숲.getDownStation().getId())).isTrue()
        );
    }
}