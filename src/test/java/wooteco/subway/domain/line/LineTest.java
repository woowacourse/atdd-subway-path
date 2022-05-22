package wooteco.subway.domain.line;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static wooteco.subway.domain.TestFixture.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import wooteco.subway.domain.section.Section;
import wooteco.subway.domain.station.Station;

@DisplayName("지하철노선")
class LineTest {

    private static final Long LINE_ID = 1L;
    private static final String LINE_NAME = "2호선";
    private static final String LINE_COLOR = "blue";

    private Line line;

    @BeforeEach
    void setUp() {
        this.line = new Line(LINE_ID, 강남_역삼_선릉_삼성, LINE_NAME, LINE_COLOR);
    }

    @DisplayName("이름과 색상을 변경한다.")
    @ParameterizedTest
    @CsvSource(value = {"1호선,red", "신분당선,yellow"})
    void update(String name, String color) {
        line.update(name, color);

        assertAll(
                () -> assertThat(line.getName()).isEqualTo(name),
                () -> assertThat(line.getColor()).isEqualTo(color)
        );
    }

    @DisplayName("구간을 추가한다")
    @Test
    void appendSection() {
        int expected = line.getSections().size();
        line.appendSection(new Section(10L, 광교역, 강남역, 10));

        List<Section> actual = line.getSections();
        assertThat(actual).hasSize(expected + 1);
    }

    @DisplayName("역을 제거한다")
    @Test
    void removeStation() {
        int expected = line.getSections().size();
        line.removeStation(역삼역);

        List<Section> actual = line.getSections();
        assertThat(actual).hasSize(expected - 1);
    }

    @DisplayName("노선의 지하철구간들을 정렬하여 반환한다.")
    @Test
    void getSections() {
        List<Section> sections = line.getSections();
        assertThat(sections).containsExactly(강남_역삼, 역삼_선릉, 선릉_삼성);
    }

    @DisplayName("노선의 지하철역들을 정렬하여 반환한다.")
    @Test
    void getStations() {
        List<Station> stations = line.getStations();
        assertThat(stations).containsExactly(강남역, 역삼역, 선릉역, 삼성역);
    }
}
