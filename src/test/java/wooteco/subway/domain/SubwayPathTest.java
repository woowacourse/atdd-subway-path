package wooteco.subway.domain;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SubwayPathTest {
    private static Station 고속터미널역;
    private static Station 교대역;
    private static Station 강남역;
    private static Station 양재역;
    private static Station 남부터미널역;
    private static Section 고속터미널_교대;
    private static Section 교대_남부터미널;
    private static Section 남부터미널_양재;
    private static Section 교대_강남;
    private static Section 강남_양재;
    private static SubwayPath subwayPath;

    @BeforeAll
    static void setUp() {
        /**
         * 고속터미널역
         * |
         * 5 *3호선*
         * |          *2호선*
         * 교대역     ---  5   ---   강남역
         * |                          |
         * 10 *3호선*                 10  *신분당선*
         * |                          |
         * 남부터미널역  --- 15 ---    양재
         *             *3호선*
         */
        고속터미널역 = new Station(1L, "고속터미널역");
        교대역 = new Station(2L, "교대역");
        강남역 = new Station(3L, "강남역");
        양재역 = new Station(4L, "양재역");
        남부터미널역 = new Station(5L, "남부터미널역");

        고속터미널_교대 = new Section(1L, 고속터미널역, 교대역, 5);
        교대_남부터미널 = new Section(2L, 교대역, 남부터미널역, 10);
        남부터미널_양재 = new Section(3L, 남부터미널역, 양재역, 15);
        교대_강남 = new Section(4L, 교대역, 강남역, 5);
        강남_양재 = new Section(5L, 강남역, 양재역, 10);

        subwayPath = SubwayPath.of(Arrays.asList(
                고속터미널_교대, 교대_남부터미널, 남부터미널_양재, 교대_강남, 강남_양재
        ));
    }

    Stream<Arguments> findShortestPath() {
        return Stream.of(
                Arguments.of(
                        고속터미널역, 양재역,
                        Arrays.asList(고속터미널역, 교대역, 강남역, 양재역),
                        "고속터미널 -> 양재역의 경로를 찾는다."
                ),
                Arguments.of(
                        강남역, 남부터미널역,
                        Arrays.asList(강남역, 교대역, 남부터미널역),
                        "강남역 -> 남부터미널역의 경로를 찾는다."
                ),
                Arguments.of(
                        양재역, 교대역,
                        Arrays.asList(양재역, 강남역, 교대역),
                        "양재역 -> 교대역의 경로를 찾는다."
                )
        );
    }

    @DisplayName("구간 정보를 통해 SubwayPath를 만들고, 최단 경로를 찾는다.")
    @ParameterizedTest(name = "{3}")
    @MethodSource
    void findShortestPath(Station source, Station target, List<Station> expected, String testCaseName) {
        List<Station> shortestPath = subwayPath.findShortestPath(source, target);
        assertThat(shortestPath).isEqualTo(expected);
    }

    Stream<Arguments> findDistanceOfShortestPath() {
        return Stream.of(
                Arguments.of(
                        고속터미널역, 양재역,
                        20,
                        "고속터미널 -> 양재역 최단 경로의 거리를 구한다."
                ),
                Arguments.of(
                        강남역, 남부터미널역,
                        15,
                        "강남역 -> 남부터미널역 최단 경로의 거리를 구한다."
                ),
                Arguments.of(
                        양재역, 교대역,
                        15,
                        "양재역 -> 교대역 최단 경로의 거리를 구한다."
                )
        );
    }

    @DisplayName("구간 정보를 통해 SubwayPath를 만들고, 최단 경로의 거리를 찾는다.")
    @ParameterizedTest(name = "{3}")
    @MethodSource
    void findDistanceOfShortestPath(Station source, Station target, Integer expected, String testCaseName) {
        Integer totalDistance = subwayPath.getTotalDistance(source, target);
        assertThat(totalDistance).isEqualTo(expected);
    }
}