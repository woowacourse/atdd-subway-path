package wooteco.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;
import wooteco.subway.utils.PathFixtureUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static wooteco.subway.utils.FixtureUtils.PATH_BY_LINE_ID;
import static wooteco.subway.utils.FixtureUtils.get;

@DisplayName("지하철 경로 E2E")
@Sql("/init.sql")
class PathAcceptanceTest extends AcceptanceTest {

    @DisplayName("올바른 경로와 요금을 가져오는지 테스트한다.")
    @Test
    void findPath() {
        PathFixtureUtils pathFixtureUtils = new PathFixtureUtils();
        ExtractableResponse<Response> 경로_조회_응답 = get(PATH_BY_LINE_ID(pathFixtureUtils.기흥역_ID, pathFixtureUtils.강남역_ID));
        assertThat(경로_조회_응답.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @DisplayName("존재하지 않는 역의 경로를 찾을때는 400 코드가 반환된다.")
    @Test
    void findPathWithNotExistStation() {
        PathFixtureUtils pathFixtureUtils = new PathFixtureUtils();
        long 존재하지_않는_역_ID = 100L;
        ExtractableResponse<Response> 경로_조회_응답 = get(PATH_BY_LINE_ID(pathFixtureUtils.기흥역_ID, 존재하지_않는_역_ID));

        assertThat(경로_조회_응답.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }
}
