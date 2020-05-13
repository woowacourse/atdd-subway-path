package wooteco.subway.admin.acceptance;

import static org.assertj.core.api.Assertions.*;

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
		LocalTime startTime = LocalTime.of(8, 00);
		LocalTime endTime = LocalTime.of(22, 00);
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
	void wholeSubway() {
		LineResponse lineTwoResponse = createLine(LINE_NAME_2);
		StationResponse stationKangnamResponse = createStation(STATION_NAME_KANGNAM);
		StationResponse stationYeoksamResponse = createStation(STATION_NAME_YEOKSAM);
		StationResponse stationSamsungResponse = createStation(STATION_NAME_SAMSUNG);

		addLineStation(lineTwoResponse.getId(), null, stationKangnamResponse.getId());
		addLineStation(lineTwoResponse.getId(), stationKangnamResponse.getId(), stationYeoksamResponse.getId());
		addLineStation(lineTwoResponse.getId(), stationYeoksamResponse.getId(), stationSamsungResponse.getId());

		LineResponse lineSinbundangResponse = createLine(LINE_NAME_SINBUNDANG);
		StationResponse stationYangjaeResponse = createStation(STATION_NAME_YANGJAE);
		StationResponse stationYangjaeForestResponse = createStation(STATION_NAME_YANGJAE_FOREST);
		addLineStation(lineSinbundangResponse.getId(), null, stationKangnamResponse.getId());
		addLineStation(lineSinbundangResponse.getId(), stationKangnamResponse.getId(), stationYangjaeResponse.getId());
		addLineStation(lineSinbundangResponse.getId(), stationYangjaeResponse.getId(),
			stationYangjaeForestResponse.getId());

		List<LineDetailResponse> lineDetailResponses = retrieveWholeSubway().getLineDetailResponses();
		assertThat(lineDetailResponses.size()).isEqualTo(2);
		assertThat(lineDetailResponses.get(0).getStations().size()).isEqualTo(3);
		assertThat(lineDetailResponses.get(1).getStations().size()).isEqualTo(3);
	}
}
