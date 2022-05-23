package wooteco.subway.helper;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import wooteco.subway.domain.Station;
import wooteco.subway.dto.StationResponse;

public class StationAddAndRequest extends Request {

    private final Station station;

    public StationAddAndRequest(TStation station) {
        this.station = station.역을등록한다();
    }

    public List<StationResponse> 역을조회한다(int status) {
        ExtractableResponse<Response> response = get("/stations");
        assertThat(response.statusCode()).isEqualTo(status);

        return response.jsonPath().getList(".", StationResponse.class);
    }

    public void 역을제거한다(int status) {
        ExtractableResponse<Response> response = delete(String.format("/stations/%s", station.getId()));
        assertThat(response.statusCode()).isEqualTo(status);
    }
}
