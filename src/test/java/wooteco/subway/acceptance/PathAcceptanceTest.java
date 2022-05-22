package wooteco.subway.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import wooteco.subway.utils.RestAssuredUtil;

public class PathAcceptanceTest extends AcceptanceTest {

    @DisplayName("경로를 조회한다.")
    @Test
    void searchPath() {
        //given
        String url = "/paths?source=1&target=2&age=15";

        //when
        ExtractableResponse<Response> response = RestAssuredUtil.get(url);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }
}
