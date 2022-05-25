package wooteco.subway.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.test.context.jdbc.Sql;
import wooteco.subway.ui.dto.response.PathResponse;

@DisplayName("경로 조회 E2E")
@Sql({"/truncate.sql", "/path-e2e-test.sql"})
class PathAcceptanceTest extends AcceptanceTest {

    private static final List<String> 내방에서_답십리까지 = List.of("내방", "고속터미널", "강남구청", "청담", "뚝섬유원지", "건대입구", "어린이대공원", "군자",
            "장한평",
            "답십리");
    private static final List<String> 내방에서_어린이대공원까지 = List.of("내방", "고속터미널", "강남구청", "청담", "뚝섬유원지", "건대입구", "어린이대공원");
    private static final List<String> 답십리에서_선릉까지 = List.of("답십리", "장한평", "군자", "어린이대공원", "건대입구", "뚝섬유원지", "청담", "강남구청",
            "선정릉",
            "선릉");

    /**
     * 2번의 환승이 발생하는 답십리 -> 선릉 경로에 대한 연령별 금액 계산 테스트
     * <p>
     * 5호선 500원, 7호선 700원, 수인분당선 800원의 추가요금이 발생. 결과적으로 수인분당선의 800원의 추가요금만 가산되어야함.
     * <p>
     * 이동거리는 10km 이므로, 거리에 따른 추가운임은 발생하지 않음.
     * <p>
     * 영유아 : 6세 미만. 무임.
     * <p>
     * 어린이 : 6세 ~ 12세. 운임에서 350원을 차감한 뒤 50% 할인. (1250 + 800 - 350) * 0.5 => 850
     * <p>
     * 청소년 : 13 ~ 18세. 운임에서 350원을 차감한 뒤 20% 할인. (1250 + 800 - 350) * 0.8 => 1360
     * <p>
     * 성인 : 19세 ~ 64세. 계산된 운임 그대로 적용. (1250 + 800 - 0) * 1.0 => 2050
     * <p>
     * 연장자 : 65세 이상. 무임.
     *
     * @param age      요금 계산이 달라지는 구간별 엣지케이스
     * @param expected 예상 운임
     */
    @ParameterizedTest(name = "나이 : {0}, 이동거리 : 10km, 노선추가금 : 800원, 기대요금 : {1}")
    @CsvSource(value = {"1, 0", "5, 0",
            "6, 850", "12, 850",
            "13, 1360", "18, 1360",
            "19, 2050", "64, 2050",
            "65, 0", "66, 0", "70, 0"})
    @DisplayName("연령별 답십리 -> 선릉 요금 테스트")
    void dapSipLeeToSunNeung(long age, long expected) {
        // given
        final String pathURI = String.format("/paths?source=1&target=10&age=%d", age);

        // when
        final PathResponse response = get(pathURI).as(PathResponse.class);

        // then
        assertAll(
                () -> assertThat(response.getStations())
                        .extracting("name")
                        .isEqualTo(답십리에서_선릉까지),
                () -> assertThat(response.getDistance()).isEqualTo(10),
                () -> assertThat(response.getFare()).isEqualTo(expected)
        );
    }

    /**
     * 2번의 환승이 발생하는 내방 -> 선릉 경로에 대한 연령별 금액 계산 테스트
     * <p>
     * 7호선 700원, 3호선 300원, 2호선 200원의 추가요금이 발생. 결과적으로 7호선의 700원의 추가요금만 가산되어야함.
     * <p>
     * 이동거리는 5km 이므로, 거리에 따른 추가운임은 발생하지 않음.
     * <p>
     * 영유아 : 6세 미만. 무임.
     * <p>
     * 어린이 : 6세 ~ 12세. 운임에서 350원을 차감한 뒤 50% 할인. (1250 + 700 - 350) * 0.5 => 800
     * <p>
     * 청소년 : 13 ~ 18세. 운임에서 350원을 차감한 뒤 20% 할인. (1250 + 700 - 350) * 0.8 => 1280
     * <p>
     * 성인 : 19세 ~ 64세. 계산된 운임 그대로 적용. (1250 + 700 - 0) * 1.0 => 1950
     * <p>
     * 연장자 : 65세 이상. 무임.
     *
     * @param age      요금 계산이 달라지는 구간별 엣지케이스
     * @param expected 예상 운임
     */
    @ParameterizedTest(name = "나이 : {0}, 이동거리 : 5km, 노선추가금 : 700원, 기대요금 : {1}")
    @CsvSource(value = {"1, 0", "5, 0",
            "6, 800", "12, 800",
            "13, 1280", "18, 1280",
            "19, 1950", "64, 1950",
            "65, 0", "66, 0", "70, 0"})
    @DisplayName("연령별 내방 -> 선릉 요금 테스트")
    void naeBangToSunNeung(long age, long expected) {
        // given
        final String pathURI = String.format("/paths?source=15&target=10&age=%d", age);

        // when
        final PathResponse response = get(pathURI).as(PathResponse.class);

        // then
        assertAll(
                () -> assertThat(response.getStations())
                        .extracting("name")
                        .containsExactly("내방", "고속터미널", "교대", "강남", "역삼", "선릉"),
                () -> assertThat(response.getDistance()).isEqualTo(5),
                () -> assertThat(response.getFare()).isEqualTo(expected)
        );
    }

    /**
     * 1번의 환승, 1개의 노선추가금이 있을 때, 거리 추가요금이 없거나 발생하는 상황에 대한 연령별 운임 테스트.
     * <p>
     * 7호선 700원, 5호선 500원 추가요금이 발생. 결과적으로 7호선의 700원의 추가요금만 가산되어야함.
     * <p>
     * 이동거리는 각각 10km, 13km 이므로, 내방 -> 어린이대공원은 거리 추가요금 없음, 내방 -> 답십리는 거리 추가요금 100원 발생.
     * <p>
     * 영유아 : 6세 미만. 무임.
     * <p>
     * 어린이 : 6세 ~ 12세. 운임에서 350원을 차감한 뒤 50% 할인. (1250 + 700 - 350) * 0.5 => 800, (1250 + 100 + 700 - 350) * 0.5 => 850
     * <p>
     * 청소년 : 13 ~ 18세. 운임에서 350원을 차감한 뒤 20% 할인. (1250 + 700 - 350) * 0.8 => 1280, (1250 + 100 + 700 - 350) * 0.8 =>
     * 1360
     * <p>
     * 성인 : 19세 ~ 64세. 계산된 운임 그대로 적용. (1250 + 700 - 0) * 1.0 => 1950, (1250 + 100 + 700 - 0) * 1.0 => 2050
     * <p>
     * 연장자 : 65세 이상. 무임.
     *
     * @param age      요금 계산이 달라지는 구간별 엣지케이스
     * @param expected 예상 운임
     */
    @ParameterizedTest(name = "나이 : {0}, 이동거리 : 10km or 13km, 노선추가금 : 700원, 기대요금 : {2}")
    @CsvSource(value = {"1, 4, 0", "1, 1, 0", "5, 4, 0", "5, 1, 0",
            "6, 4, 800", "6, 1, 850", "12, 4, 800", "12, 1, 850",
            "13, 4, 1280", "13, 1, 1360", "18, 4, 1280", "18, 1, 1360",
            "19, 4, 1950", "19, 1, 2050", "64, 4, 1950", "64, 1, 2050",
            "65, 4, 0", "65, 1, 0", "66, 4, 0", "66, 1, 0", "70, 4, 0", "70, 1, 0"})
    @DisplayName("연령별 내방 -> 뚝섬유원지(8km, 7호선), 내방 -> 답십리(13km, 7호선->5호선 환승) 요금 테스트")
    void naeBangToDdookSumOrDapSipLee(long age, long target, long expected) {
        // given
        final String pathURI = String.format("/paths?source=15&target=%d&age=%d", target, age);
        final List<String> expectedStations = getExpectedStations(target);
        final long expectedDistance = getExpectedDistance(target);

        // when
        final PathResponse response = get(pathURI).as(PathResponse.class);

        // then
        assertAll(
                () -> assertThat(response.getStations()).extracting("name").isEqualTo(expectedStations),
                () -> assertThat(response.getDistance()).isEqualTo(expectedDistance),
                () -> assertThat(response.getFare()).isEqualTo(expected)
        );
    }

    private List<String> getExpectedStations(long target) {
        if (target == 4) {
            return 내방에서_어린이대공원까지;
        }

        return 내방에서_답십리까지;
    }

    private long getExpectedDistance(long target) {
        if (target == 4) {
            return 10L;
        }

        return 13L;
    }
}
