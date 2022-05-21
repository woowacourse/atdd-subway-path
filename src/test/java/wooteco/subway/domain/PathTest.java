package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PathTest {

    @DisplayName("강남 - 역삼 - 삼성 - 양재 일 때 (강남- 삼성) 의 최단 경로는 강남, 역삼, 삼성이다.")
    @Test
    void createShortestPath() {
        // given
        Line 일호선 = new Line(1L, "1호선", "blue");
        Station 강남역 = new Station(1L, "강남역");
        Station 역삼역 = new Station(2L, "역삼역");
        Station 삼성역 = new Station(3L, "삼성역");
        Station 양재역 = new Station(4L, "양재역");
        List<Section> list = List.of(new Section(1L, 일호선, 강남역, 역삼역, 10),
                new Section(1L, 일호선, 역삼역, 삼성역, 10), new Section(1L, 일호선, 삼성역, 양재역, 10));
        // when
        Path path = new Path(list);
        // then
        List<Station> result = path.createShortestPath(강남역, 삼성역);
        assertThat(result).containsExactly(강남역, 역삼역, 삼성역);
    }

    @DisplayName("구간이 없는 역에 대해 최단 경로를 조회시 에러가 발생한다.")
    @Test
    void createShortestPathFalse() {
        // given
        Line 일호선 = new Line(1L, "1호선", "blue");
        Station 강남역 = new Station(1L, "강남역");
        Station 역삼역 = new Station(2L, "역삼역");
        Station 삼성역 = new Station(3L, "삼성역");
        Station 양재역 = new Station(4L, "양재역");
        List<Section> list = List.of(new Section(1L, 일호선, 강남역, 역삼역, 10),
                new Section(1L, 일호선, 역삼역, 삼성역, 10));
        // when
        Path path = new Path(list);
        // then
        Assertions.assertThatIllegalArgumentException()
                .isThrownBy(() -> path.createShortestPath(강남역, 양재역))
                .withMessage("최단 경로를 요청하신 역이 구간에 존재하지 않습니다.");
    }

    @DisplayName("강남 - 역삼 - 삼성 - 양재 일 때 (강남- 삼성) 의 최단 거리는 20이다.")
    @Test
    void calculateDistance() {
        // given
        Line 일호선 = new Line(1L, "1호선", "blue");
        Station 강남역 = new Station(1L, "강남역");
        Station 역삼역 = new Station(2L, "역삼역");
        Station 삼성역 = new Station(3L, "삼성역");
        Station 양재역 = new Station(4L, "양재역");
        List<Section> list = List.of(new Section(1L, 일호선, 강남역, 역삼역, 10),
                new Section(1L, 일호선, 역삼역, 삼성역, 10), new Section(1L, 일호선, 삼성역, 양재역, 10));
        // when
        Path path = new Path(list);
        // then
        int result = path.calculateDistance(강남역, 삼성역);
        assertThat(result).isEqualTo(20);
    }

}
