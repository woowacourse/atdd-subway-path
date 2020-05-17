package wooteco.subway.acceptance;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;

import wooteco.subway.dto.LineDetailResponse;
import wooteco.subway.dto.LineResponse;
import wooteco.subway.dto.StationResponse;
import wooteco.subway.dto.WholeSubwayResponse;

@Sql("/truncate.sql")
public class MapAcceptanceTest extends AcceptanceTest {
    @DisplayName("지하철 노선도 전체 정보 조회")
    @Test
    public void wholeSubway() {
        //given
        LineResponse line2 = createLine("2호선");
        StationResponse gangnam = createStation("강남역");
        StationResponse yeoksam = createStation("역삼역");
        StationResponse samsung = createStation("삼성역");
        addLineStation(line2.getId(), null, gangnam.getId());
        addLineStation(line2.getId(), gangnam.getId(), yeoksam.getId());
        addLineStation(line2.getId(), yeoksam.getId(), samsung.getId());

        LineResponse lineSinbundang = createLine("신분당선");
        StationResponse yangjae = createStation("양재역");
        StationResponse yangjaeCitizenForest = createStation("양재시민의숲역");
        addLineStation(lineSinbundang.getId(), null, gangnam.getId());
        addLineStation(lineSinbundang.getId(), gangnam.getId(), yangjae.getId());
        addLineStation(lineSinbundang.getId(), yangjae.getId(), yangjaeCitizenForest.getId());

        //when
        List<LineDetailResponse> response = retrieveWholeSubway().getLineDetailResponse();

        //then
        assertThat(response.size()).isEqualTo(2);
        assertThat(response.get(0).getStations().size()).isEqualTo(3);
        assertThat(response.get(1).getStations().size()).isEqualTo(3);
    }

    private WholeSubwayResponse retrieveWholeSubway() {
        return given()
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .get("/map")
            .then()
            .extract().as(WholeSubwayResponse.class);
    }
}
