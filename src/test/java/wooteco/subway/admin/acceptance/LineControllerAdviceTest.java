package wooteco.subway.admin.acceptance;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import wooteco.subway.admin.controller.ErrorMessage;
import wooteco.subway.admin.domain.station.Station;
import wooteco.subway.admin.dto.PathRequest;

class LineControllerAdviceTest extends AcceptanceTest {

    @Test
    void getIllegalArgumentException() {
        String notExistDeparture = "NOT_EXIST_DEPARTURE";
        String notExistArrival = "NOT_EXIST_ARRIVAL";

        PathRequest pathRequest = new PathRequest(notExistDeparture, notExistArrival);

        assertThat(getPath(notExistDeparture, notExistArrival, pathRequest)).isInstanceOf(ErrorMessage.class);
    }

    private ErrorMessage getPath(String departure, String arrival, PathRequest pathRequest) {
        return given().
            when().
            accept(MediaType.APPLICATION_JSON_VALUE).
            contentType(MediaType.APPLICATION_JSON_VALUE).
            body(pathRequest).
            get("/lines/path/" + departure + "/" + arrival).
            then().
            log().all().
            extract().as(ErrorMessage.class);
    }

    @Test
    void getNoPathException() {
        Station departure = new Station("a");
        Station arrival = new Station("b");

        PathRequest pathRequest = new PathRequest(departure.getName(), arrival.getName());

        createStation(departure.getName());
        createStation(arrival.getName());

        assertThat(getPath(departure.getName(), arrival.getName(), pathRequest)).isInstanceOf(ErrorMessage.class);
    }
}