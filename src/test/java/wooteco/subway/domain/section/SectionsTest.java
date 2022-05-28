package wooteco.subway.domain.section;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static wooteco.subway.Fixtures.강남역;
import static wooteco.subway.Fixtures.삼성역;
import static wooteco.subway.Fixtures.선릉역;
import static wooteco.subway.Fixtures.잠실새내역;
import static wooteco.subway.Fixtures.잠실역;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import wooteco.subway.domain.station.Station;
import wooteco.subway.exception.BothUpAndDownStationDoNotExistException;
import wooteco.subway.exception.BothUpAndDownStationExistException;
import wooteco.subway.exception.CanNotInsertSectionException;
import wooteco.subway.exception.OnlyOneSectionException;

class SectionsTest {

    private Section initialSection;
    private Sections sections;
    
    @BeforeEach
    void setUp() {
        initialSection = new Section(잠실역, 선릉역, new Distance(10));
        sections = new Sections(List.of(initialSection));
    }

    @DisplayName("상행 종점, 하행 종점, 거리를 전달받아 구간 목록 생성")
    @Test
    void constructor() {
        // given
        Station upStation = 강남역;
        Station downStation = 선릉역;
        Distance distance = new Distance(10);
        Section section = new Section(upStation, downStation, distance);

        // when
        Sections sections = new Sections(List.of(section));

        // then
        assertThat(sections).isNotNull();
    }

    @DisplayName("이미 존재하는 역과 새롭게 상행 종점이 될 역으로 구성된 구간을 추가")
    @Test
    void addSection_withNewUpStation() {
        // given
        Section newSection = new Section(잠실새내역, 잠실역, new Distance(5));

        // when
        sections.addSection(newSection);

        // then
        assertThat(sections.getValue()).containsAll(List.of(initialSection, newSection));
    }

    @DisplayName("이미 존재하는 역과 새롭게 하행 종점이 될 역으로 구성된 구간을 추가")
    @Test
    void addSection_withNewDownStation() {
        // given
        Section newSection = new Section(선릉역, 삼성역, new Distance(5));

        // when
        sections.addSection(newSection);

        // then
        assertThat(sections.getValue()).containsAll(List.of(initialSection, newSection));
    }

    @DisplayName("상행선 기준으로 새로운 구간을 생성할 때 갈래길이 생기지 않는다.")
    @Test
    void addSection_insertingUpStation() {
        // given
        Section newSection = new Section(잠실역, 잠실새내역, new Distance(5));

        // when
        sections.addSection(newSection);
        List<Section> actual = sections.getValue();

        // then
        List<Section> expected = List.of(new Section(잠실역, 잠실새내역, new Distance(5)),
                new Section(잠실새내역, 선릉역, new Distance(5)));
        assertThat(actual).containsAll(expected);
    }

    @DisplayName("하행선 기준으로 새로운 구간을 생성할 때 갈래길이 생기지 않는다.")
    @Test
    void addSection_insertingDownStation() {
        // given
        Section newSection = new Section(잠실새내역, 선릉역, new Distance(5));

        // when
        sections.addSection(newSection);
        List<Section> actual = sections.getValue();

        // then
        List<Section> expected = List.of(new Section(잠실역, 잠실새내역, new Distance(5)),
                new Section(잠실새내역, 선릉역, new Distance(5)));
        assertThat(actual).containsAll(expected);
    }

    @DisplayName("구간 삽입시 기존 구간의 길이보다 더 길거나 같은 길이의 구간을 삽입할 시 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(ints = {10, 15, 100})
    void addSection_throwsExceptionOnTryingToInsertLongerSection(int distance) {
        // given
        Section newSection = new Section(잠실새내역, 선릉역, new Distance(distance));

        // when & then
        assertThatThrownBy(() -> sections.addSection(newSection))
                .isInstanceOf(CanNotInsertSectionException.class);
    }

    @DisplayName("구간 추가시 새로운 구간의 상행역과 하행역이 모두 이미 구간 목록에 등록되어 있다면 예외가 발생한다.")
    @Test
    void addSection_throwsExceptionIfBothUpAndDownStationAlreadyExistsInSections() {
        // given
        Section newSection = new Section(잠실역, 선릉역, new Distance(5));

        // when & then
        assertThatThrownBy(() -> sections.addSection(newSection))
                .isInstanceOf(BothUpAndDownStationExistException.class);
    }

    @DisplayName("구간 추가시 새로운 구간의 상행역과 하행역이 모두 구간 목록에 등록되어 있지 않다면 예외가 발생한다.")
    @Test
    void addSection_throwsExceptionIfBothUpAndDownStationDoNotExistInSections() {
        // given
        Section newSection = new Section(잠실새내역, 삼성역, new Distance(5));

        // when & then
        assertThatThrownBy(() -> sections.addSection(newSection))
                .isInstanceOf(BothUpAndDownStationDoNotExistException.class);
    }

    @DisplayName("구간 목록의 상행역을 제거할 수 있다.")
    @Test
    void deleteStation_upStation() {
        // given
        sections.addSection(new Section(선릉역, 잠실새내역, new Distance(10)));
        sections.addSection(new Section(잠실새내역, 삼성역, new Distance(10)));

        // when
        sections.deleteStation(잠실역);

        // then
        assertThat(sections.getValue()).containsAll(List.of(
                new Section(선릉역, 잠실새내역, new Distance(10)),
                new Section(잠실새내역, 삼성역, new Distance(10))
        ));
    }


    @DisplayName("구간 목록의 하행역을 제거할 수 있다.")
    @Test
    void deleteStation_downStation() {
        // given
        sections.addSection(new Section(선릉역, 잠실새내역, new Distance(10)));
        sections.addSection(new Section(잠실새내역, 삼성역, new Distance(10)));

        // when
        sections.deleteStation(삼성역);

        // then
        assertThat(sections.getValue()).containsAll(List.of(
                new Section(잠실역, 선릉역, new Distance(10)),
                new Section(선릉역, 잠실새내역, new Distance(10))
        ));
    }

    @DisplayName("구간 목록의 중간역을 제거할 수 있다.")
    @Test
    void deleteStation_betweenStation() {
        // given
        sections.addSection(new Section(선릉역, 잠실새내역, new Distance(10)));
        sections.addSection(new Section(잠실새내역, 삼성역, new Distance(10)));

        // when
        sections.deleteStation(잠실새내역);

        // then
        assertThat(sections.getValue()).containsAll(List.of(
                new Section(잠실역, 선릉역, new Distance(10)),
                new Section(선릉역, 삼성역, new Distance(20))
        ));
    }

    @DisplayName("구간이 단 하나인 구간 목록에서 구간 제거를 하면 예외가 발생한다.")
    @Test
    void deleteStation_throwsExceptionIfSectionsSizeIsOne() {
        assertThatThrownBy(() -> sections.deleteStation(잠실역))
                .isInstanceOf(OnlyOneSectionException.class);
    }
}
