package wooteco.subway.admin.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.admin.dto.LineDetailResponse;
import wooteco.subway.admin.dto.LineResponse;
import wooteco.subway.admin.dto.StationResponse;

public class LineAcceptanceTest extends AcceptanceTest {

	@DisplayName("지하철 노선을 관리한다")
	@Test
	void manageLine() {
		// when
		createLine(LINE_NAME_SINBUNDANG);
		createLine(LINE_NAME_BUNDANG);
		createLine(LINE_NAME_2);
		createLine(LINE_NAME_3);
		// then
		List<LineResponse> lines = getLines();
		assertThat(lines.size()).isEqualTo(4);

		// when
		LineDetailResponse line = getLine(lines.get(0).getId());
		// then
		assertThat(line.getId()).isNotNull();
		assertThat(line.getName()).isNotNull();
		assertThat(line.getStartTime()).isNotNull();
		assertThat(line.getEndTime()).isNotNull();
		assertThat(line.getIntervalTime()).isNotNull();

		// when
		LocalTime startTime = LocalTime.of(8, 0);
		LocalTime endTime = LocalTime.of(22, 0);
		updateLine(line.getId(), startTime, endTime);
		//then
		LineDetailResponse updatedLine = getLine(line.getId());
		assertThat(updatedLine.getStartTime()).isEqualTo(startTime);
		assertThat(updatedLine.getEndTime()).isEqualTo(endTime);

		// when
		deleteLine(line.getId());
		// then
		List<LineResponse> linesAfterDelete = getLines();
		assertThat(linesAfterDelete.size()).isEqualTo(3);
	}

	@DisplayName("지하철 노선도 전체 정보 조회")
	@Test
	public void wholeSubway() {
		LineResponse lineResponse1 = createLine("2호선");
		StationResponse stationResponse1 = createStation("강남역");
		StationResponse stationResponse2 = createStation("역삼역");
		StationResponse stationResponse3 = createStation("삼성역");
		addLineStation(lineResponse1.getId(), null, stationResponse1.getId());
		addLineStation(lineResponse1.getId(), stationResponse1.getId(), stationResponse2.getId());
		addLineStation(lineResponse1.getId(), stationResponse2.getId(), stationResponse3.getId());

		LineResponse lineResponse2 = createLine("신분당선");
		StationResponse stationResponse4 = createStation("양재역");
		StationResponse stationResponse5 = createStation("양재시민의숲역");
		addLineStation(lineResponse2.getId(), null, stationResponse1.getId());
		addLineStation(lineResponse2.getId(), stationResponse1.getId(), stationResponse4.getId());
		addLineStation(lineResponse2.getId(), stationResponse4.getId(), stationResponse5.getId());

		List<LineResponse> response = retrieveWholeSubway().getLineDetails();
		assertThat(response.size()).isEqualTo(2);
		assertThat(response.get(0).getStations().size()).isEqualTo(3);
		assertThat(response.get(1).getStations().size()).isEqualTo(3);
	}
}
