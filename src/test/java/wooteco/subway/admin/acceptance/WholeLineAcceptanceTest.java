package wooteco.subway.admin.acceptance;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.admin.dto.response.LineDetailResponse;
import wooteco.subway.admin.dto.response.LineResponse;
import wooteco.subway.admin.dto.response.StationResponse;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class WholeLineAcceptanceTest extends AcceptanceTest {
	@DisplayName("지하철 노선도 전체 정보 조회한다.")
	@Test
	public void wholeSubway() {

		// given 지하철역이 여러 개 추가되어있다.
		LineResponse lineResponse1 = createLine("2호선");

		// and 지하철 노선이 여러 개 추가되어있다.
		StationResponse stationResponse1 = createStation("강남역");
		StationResponse stationResponse2 = createStation("역삼역");
		StationResponse stationResponse3 = createStation("삼성역");

		// and 지하철 노선에 지하철역이 여러 개 추가되어있다.
		addLineStation(lineResponse1.getId(), null, stationResponse1.getId());
		addLineStation(lineResponse1.getId(), stationResponse1.getId(), stationResponse2.getId());
		addLineStation(lineResponse1.getId(), stationResponse2.getId(), stationResponse3.getId());

		// given 지하철역이 여러 개 추가되어있다.
		LineResponse lineResponse2 = createLine("신분당선");

		// and 지하철 노선이 여러 개 추가되어있다.
		StationResponse stationResponse5 = createStation("양재역");
		StationResponse stationResponse6 = createStation("양재시민의숲역");

		// and 지하철 노선에 지하철역이 여러 개 추가되어있다.
		addLineStation(lineResponse2.getId(), null, stationResponse1.getId());
		addLineStation(lineResponse2.getId(), stationResponse1.getId(), stationResponse5.getId());
		addLineStation(lineResponse2.getId(), stationResponse5.getId(), stationResponse6.getId());

		// when 지하철 노선도 전체 조회 요청을 한다.
		// then 지하철 노선도 전체를 응답 받는다.
		List<LineDetailResponse> response = retrieveWholeSubway().getLineDetailResponses();
		assertThat(response.size()).isEqualTo(2);
		assertThat(response.get(0).getStations().size()).isEqualTo(3);
		assertThat(response.get(1).getStations().size()).isEqualTo(3);
	}
}
