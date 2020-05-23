package wooteco.subway.admin.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static wooteco.subway.admin.acceptance.AcceptanceTest.given;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;

import io.restassured.RestAssured;
import wooteco.subway.admin.dto.PathResponse;

@Sql(value = {"/truncate.sql", "/data.sql"})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PathAcceptanceTest {
    @LocalServerPort
    int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    public void findShortestDistancePath() {
        PathResponse pathResponse =
                given().
                        param("source", "포비").
                        param("target", "브라운").
                        param("pathType", "DISTANCE").
                        contentType(MediaType.APPLICATION_JSON_VALUE).
                        accept(MediaType.APPLICATION_JSON_VALUE).
                        when().
                        get("/paths").
                        then().
                        log().all().
                        statusCode(HttpStatus.OK.value()).
                        extract().as(PathResponse.class);

        assertThat(pathResponse.getStations().size()).isEqualTo(5);
        assertThat(pathResponse.getDuration()).isEqualTo(40);
        assertThat(pathResponse.getDistance()).isEqualTo(11);
    }

    @Test
    public void findShortestDurationPath() {
        PathResponse pathResponse =
                given().
                        param("source", "포비").
                        param("target", "브라운").
                        param("pathType", "DURATION").
                        contentType(MediaType.APPLICATION_JSON_VALUE).
                        accept(MediaType.APPLICATION_JSON_VALUE).
                        when().
                        get("/paths").
                        then().
                        log().all().
                        statusCode(HttpStatus.OK.value()).
                        extract().as(PathResponse.class);

        assertThat(pathResponse.getStations().size()).isEqualTo(4);
        assertThat(pathResponse.getDuration()).isEqualTo(30);
        assertThat(pathResponse.getDistance()).isEqualTo(15);
    }
}
