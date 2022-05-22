package wooteco.subway.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import wooteco.subway.dto.request.LineCreateRequest;
import wooteco.subway.dto.request.SectionRequest;
import wooteco.subway.dto.request.StationRequest;

public class SectionAcceptanceTest extends AcceptanceTest {

    private Long gangnamId;
    private Long yeoksamId;
    private Long seolleungId;

    @BeforeEach
    void setStations() {
        gangnamId = requestPostStationAndReturnId(new StationRequest("강남역"));
        yeoksamId = requestPostStationAndReturnId(new StationRequest("역삼역"));
        seolleungId = requestPostStationAndReturnId(new StationRequest("선릉역"));
    }

    @DisplayName("구간을 추가로 등록한다.")
    @Test
    void createSection() {
        LineCreateRequest lineCreateRequest = new LineCreateRequest("2호선", "초록색", gangnamId, yeoksamId, 1);
        ExtractableResponse<Response> lineCreateResponse = requestPostLine(lineCreateRequest);
        Long lineId = Long.parseLong(lineCreateResponse.header("Location").split("/")[2]);
        SectionRequest sectionRequest = new SectionRequest(yeoksamId, seolleungId, 1);

        ExtractableResponse<Response> response = requestPostSection(lineId, sectionRequest);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @DisplayName("등록된 구간을 삭제한다.")
    @Test
    void deleteSection() {
        LineCreateRequest lineCreateRequest = new LineCreateRequest("2호선", "초록색", gangnamId, yeoksamId, 1);
        ExtractableResponse<Response> lineCreateResponse = requestPostLine(lineCreateRequest);
        Long lineId = Long.parseLong(lineCreateResponse.header("Location").split("/")[2]);

        ExtractableResponse<Response> response = requestDeleteSection(lineId, yeoksamId);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

}
