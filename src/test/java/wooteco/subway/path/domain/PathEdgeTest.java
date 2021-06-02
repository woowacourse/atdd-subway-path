package wooteco.subway.path.domain;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.exception.application.ValidationFailureException;
import wooteco.subway.line.domain.Line;
import wooteco.subway.line.domain.Section;
import wooteco.subway.line.domain.Sections;
import wooteco.subway.station.domain.Station;

class PathEdgeTest {

    private Station 서초, 교대, 강남;
    private Section 서초_교대, 교대_강남;
    private Line 이호선;

    @BeforeEach
    void setUp() {
        서초 = new Station(1L, "서초역");
        교대 = new Station(2L, "교대역");
        강남 = new Station(3L, "강남역");
        서초_교대 = new Section(1L, 서초, 교대, 5);
        교대_강남 = new Section(2L, 교대, 강남, 3);
        Sections sections = new Sections(Arrays.asList(서초_교대, 교대_강남));
        이호선 = new Line(1L, "2호선", "green lighten-1", sections);
    }

    @DisplayName("생성에 성공한다.")
    @Test
    void creationSuccessful() {
        // when
        PathEdge pathEdge = new PathEdge(서초_교대, 이호선);

        // then
        assertAll(
            () -> assertThat(pathEdge).extracting("section")
                .isEqualTo(서초_교대),

            () -> assertThat(pathEdge).extracting("lineName")
                .isEqualTo(이호선.getName())
        );
    }

    @DisplayName("노선에 구간이 없으면 생성에 실패한다.")
    @Test
    void creationFailed() {
        // given
        Section 서초_강남 = new Section(3L, 서초, 강남, 7);

        // when, then
        assertThatThrownBy(() -> new PathEdge(서초_강남, 이호선))
            .isInstanceOf(ValidationFailureException.class)
            .hasMessageContaining("노선에 해당 구간이 없습니다");
    }
}