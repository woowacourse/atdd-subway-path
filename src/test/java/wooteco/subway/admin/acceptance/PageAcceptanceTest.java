package wooteco.subway.admin.acceptance;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PageAcceptanceTest extends AcceptanceTest {
    @Test
    void linePage() {
        given().
                accept(MediaType.TEXT_HTML_VALUE).
        when().
                get("/admin/lines").
        then().
                log().all().
                statusCode(HttpStatus.OK.value());
    }
}
