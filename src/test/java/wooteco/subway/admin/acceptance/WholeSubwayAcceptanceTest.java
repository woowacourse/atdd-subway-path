package wooteco.subway.admin.acceptance;

import static org.assertj.core.api.Assertions.*;
import static wooteco.subway.admin.acceptance.AcceptanceTest.*;
import static wooteco.subway.admin.acceptance.PageAcceptanceTest.*;

import java.util.List;

import org.junit.jupiter.api.Test;

import io.restassured.RestAssured;
import wooteco.subway.admin.dto.LineDetailResponse;
import wooteco.subway.admin.dto.LineResponse;
import wooteco.subway.admin.dto.StationResponse;
import wooteco.subway.admin.dto.WholeSubwayResponse;

public class WholeSubwayAcceptanceTest {

	@Test
	public void wholeSubway() {
		LineResponse line2 = createLine("2호선");
		StationResponse firstStation = createStation("수영");
		StationResponse secondStation = createStation("서면");
		StationResponse thirdStation = createStation("덕천");

		addLineStation(line2.getId(), null, firstStation.getId());
		addLineStation(line2.getId(), firstStation.getId(), secondStation.getId());
		addLineStation(line2.getId(), secondStation.getId(), thirdStation.getId());

		LineResponse line5 = createLine("5호선");
		StationResponse forthStation = createStation("목동");
		StationResponse fifthStation = createStation("오목");
		StationResponse sixthStation = createStation("신정");

		addLineStation(line5.getId(), null, forthStation.getId());
		addLineStation(line5.getId(), forthStation.getId(), fifthStation.getId());
		addLineStation(line5.getId(), fifthStation.getId(), sixthStation.getId());

		List<LineDetailResponse> response = retrieveWholeSubway().getLineDetailResponses();
		assertThat(response.size()).isEqualTo(2);
		assertThat(response.get(0).getStations().size()).isEqualTo(3);
	}

	public static WholeSubwayResponse retrieveWholeSubway() {
		return RestAssured.given().when().
			get("/line-details").
			then().
			log().all().
			extract().
			as(WholeSubwayResponse.class);
	}
}
