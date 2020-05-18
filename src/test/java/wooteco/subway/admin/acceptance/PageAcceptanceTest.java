package wooteco.subway.admin.acceptance;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class PageAcceptanceTest extends AcceptanceTest {
    @Test
    void linePage() {
        createLine("신분당선");

        given().
                accept(MediaType.TEXT_HTML_VALUE).
                when().
                get("/lines").
                then().
                log().all().
                statusCode(HttpStatus.OK.value());
    }

    @Test
    void stationPage() {
        createStation("강남역");

        given().
                accept(MediaType.TEXT_HTML_VALUE).
                when().
                get("/stations").
                then().
                log().all().
                statusCode(HttpStatus.OK.value());
    }
}
