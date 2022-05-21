package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.exception.AlreadyExistSectionException;
import wooteco.subway.exception.IllegalDistanceException;
import wooteco.subway.exception.NotFoundStationException;

class LineSectionsTest {

    private LineSections lineSections;
    private Section section1;
    private Section section2;

    @BeforeEach
    void setUp(){
        section1 = new Section(1L, 1L, 1L, 2L, 10, 1L);
        section2 = new Section(2L, 1L, 2L, 3L, 10, 2L);
        lineSections = new LineSections(List.of(section1, section2));
    }
    @Test
    @DisplayName("주어진 추가할 지하철 구간에 대해서 지하철역 id에 대하여 이미 모두 존재할 때 예외 발생한다.")
    void validateSectionWithExistStations(){
        assertThatThrownBy(() -> lineSections.validateSection(1L ,3L, 10))
                .isInstanceOf(AlreadyExistSectionException.class)
                .hasMessageContaining("상행, 하행이 대상 노선에 둘 다 존재합니다.");
    }

    @Test
    @DisplayName("주어진 추가할 지하철 구간에 대해서 지하철역 id에 대하여 모두 존재하지 않을 때 예외 발생한다.")
    void validateSectionWithNotExistStations(){
        assertThatThrownBy(() -> lineSections.validateSection(4L ,5L, 10))
                .isInstanceOf(NotFoundStationException.class)
                .hasMessageContaining("해당 지하철역이 등록이 안되어 있습니다.");
    }

    @Test
    @DisplayName("주어진 추가할 지하철 구간에 대해서 거리가 너무 길거나 같을 때 예외 발생한다.")
    void validateSectionWithTooLongDistance(){
        assertThatThrownBy(() -> lineSections.validateSection(1L ,4L, 10))
                .isInstanceOf(IllegalDistanceException.class)
                .hasMessageContaining("역 사이에 새로운 역을 등록할 경우, 기존 역 사이 길이보다 크거나 같으면 등록할 수 없습니다.");
    }

    @DisplayName("상행 종점에 추가적으로 구간을 추가하면 현재 구간과 추가되는 구간이 리턴된다.")
    @Test
    void findOverlapSectionByAddingSectionInLastUpStation() {
        List<Section> overlapSections = lineSections.findOverlapSection(4L, 1L, 10);

        Section forwardSection = new Section(1L, 1L, 4L, 1L, 10, 1L);
        Section backwardSection = new Section(1L, 1L, 1L, 2L, 10, 2L);

        assertThat(overlapSections).containsExactly(forwardSection, backwardSection);
    }

    @DisplayName("하행 종점에 추가적으로 구간을 추가하면 현재 구간과 추가되는 구간이 리턴된다.")
    @Test
    void findOverlapSectionByAddingSectionInLastDownStation() {
        List<Section> overlapSections = lineSections.findOverlapSection(3L, 4L, 10);

        Section forwardSection = new Section(2L, 1L, 2L, 3L, 10, 2L);
        Section backwardSection = new Section(2L, 1L, 3L, 4L, 10, 3L);

        assertThat(overlapSections).containsExactly(forwardSection, backwardSection);
    }

    @DisplayName("등록된 구간에 새로운 구간을 상행을 기준으로 추가하면 현재 구간과 추가되는 구간이 리턴된다.")
    @Test
    void findOverlapSectionByAddingSectionInMiddleUpStation() {
        List<Section> overlapSections = lineSections.findOverlapSection(1L, 4L, 5);

        Section forwardSection = new Section(1L, 1L, 1L, 4L, 5, 1L);
        Section backwardSection = new Section(1L, 1L, 4L, 2L, 5, 2L);

        assertThat(overlapSections).containsExactly(forwardSection, backwardSection);
    }

    @DisplayName("등록된 구간에 새로운 구간을 하행을 기준으로 추가하면 현재 구간과 추가되는 구간이 리턴된다.")
    @Test
    void findOverlapSectionByAddingSectionInMiddleDownStation() {
        List<Section> overlapSections = lineSections.findOverlapSection(4L, 2L, 5);

        Section forwardSection = new Section(1L, 1L, 1L, 4L, 5, 1L);
        Section backwardSection = new Section(1L, 1L, 4L, 2L, 5, 2L);

        assertThat(overlapSections).containsExactly(forwardSection, backwardSection);
    }

    @DisplayName("등록된 구간에서 사용되는 지하철역 id를 리턴한다.")
    @Test
    void getStationsId() {
        List<Long> stationsId = lineSections.getStationsId();

        assertThat(stationsId).containsExactly(1L, 2L, 3L);
    }

    @DisplayName("현재 구간이 2개인지 검증한다.")
    @Test
    void hasTwoSection() {
        assertThat(lineSections.hasTwoSection()).isTrue();
    }

    @DisplayName("현재 가장 상행인 구간을 가져온다.")
    @Test
    void getUpsideSection() {
        assertThat(lineSections.getUpsideSection()).isEqualTo(section1);
    }

    @DisplayName("현재 가장 하행인 구간을 가져온다.")
    @Test
    void getDownsideSection() {
        assertThat(lineSections.getDownsideSection()).isEqualTo(section2);
    }
}
