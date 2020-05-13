package wooteco.subway.admin.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import wooteco.subway.admin.dto.PathResponse;

public class PathAcceptanceTest extends AcceptanceTest {
    @Test
    public void findShortestPath() {
        PathResponse pathResponse =
                given().
                        param("source", "양재시민의숲역").
                        param("target", "선릉역").
                        contentType(MediaType.APPLICATION_JSON_VALUE).
                        accept(MediaType.APPLICATION_JSON_VALUE).
                        when().
                        get("/paths").
                        then().
                        log().all().
                        statusCode(HttpStatus.OK.value()).
                        extract().as(PathResponse.class);

        assertThat(pathResponse.getStations().size()).isEqualTo(5);
        assertThat(pathResponse.getDistance()).isEqualTo(40);
        assertThat(pathResponse.getDuration()).isEqualTo(40);
    }
}
