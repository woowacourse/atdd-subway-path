package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class SectionsTest {

    @Test
    @DisplayName("sections 가 정렬되었는지 확인한다.")
    void sort() {
        Line 일호선 = new Line("1호선", "bg-blue-200");
        Station 성수 = new Station(1L, "성수");
        Station 건대입구 = new Station(2L, "건대입구");
        Station 구의 = new Station(3L, "구의");
        Station 강변 = new Station(4L, "강변");
        Section section1 = new Section(일호선, 성수, 건대입구, 10);
        Section section2 = new Section(일호선, 구의, 강변, 10);
        Section section3 = new Section(일호선, 건대입구, 구의, 10);

        List<Section> sections = new ArrayList<>();
        sections.add(section3);
        sections.add(section2);
        sections.add(section1);
        Sections sections1 = new Sections(sections);

        assertThat(sections1.getStations()).containsExactly(성수, 건대입구, 구의, 강변);
    }
}
