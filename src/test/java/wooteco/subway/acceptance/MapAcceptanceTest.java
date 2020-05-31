package wooteco.subway.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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
		StationResponse 강남역 = createStation("강남역");
		StationResponse 역삼역 = createStation("역삼역");
		StationResponse 삼성역 = createStation("삼성역");
		addLineStation(line2.getId(), null, 강남역.getId());
		addLineStation(line2.getId(), 강남역.getId(), 역삼역.getId());
		addLineStation(line2.getId(), 역삼역.getId(), 삼성역.getId());

		LineResponse lineSinbundang = createLine("신분당선");
		StationResponse 양재역 = createStation("양재역");
		StationResponse 양재시민의숲역 = createStation("양재시민의숲역");
		addLineStation(lineSinbundang.getId(), null, 강남역.getId());
		addLineStation(lineSinbundang.getId(), 강남역.getId(), 양재역.getId());
		addLineStation(lineSinbundang.getId(), 양재역.getId(), 양재시민의숲역.getId());

		//when
		List<LineDetailResponse> response = retrieveWholeSubway().getLineDetailResponse();

		//then
		assertThat(response.size()).isEqualTo(2);
		assertThat(response.get(0).getStations().size()).isEqualTo(3);
		assertThat(response.get(1).getStations().size()).isEqualTo(3);
	}

	private WholeSubwayResponse retrieveWholeSubway() {
		return get("/paths/detail", WholeSubwayResponse.class);
	}
}
