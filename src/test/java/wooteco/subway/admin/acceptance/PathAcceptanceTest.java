package wooteco.subway.admin.acceptance;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import wooteco.subway.admin.dto.LineResponse;
import wooteco.subway.admin.dto.PathResponse;
import wooteco.subway.admin.dto.StationResponse;

public class PathAcceptanceTest extends AcceptanceTest {
	@Autowired
	private WebApplicationContext webApplicationContext;
	private MockMvc mockMvc;

	@Override
	@BeforeEach
	void setUp() {
		super.setUp();
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
				.addFilter(new CharacterEncodingFilter("UTF-8", true))
				.build();
	}

	@DisplayName("지하철 경로 조회")
	@Test
	void getPath() throws Exception {
		//given
		LineResponse line = createLine("2호선");
		StationResponse jamsil = createStation("잠실");
		StationResponse hongik = createStation("홍대입구");
		StationResponse sinchon = createStation("신촌");

		addEdge(line.getId(), null, jamsil.getId(), 10, 10);
		addEdge(line.getId(), jamsil.getId(), hongik.getId(), 11, 11);
		addEdge(line.getId(), hongik.getId(), sinchon.getId(), 12, 12);

		//when
		MvcResult mvcResult = mockMvc.perform(
				MockMvcRequestBuilders.get("/paths")
						.param("source", "잠실")
						.param("target", "신촌")
						.param("key", "distance")
						.accept(MediaType.APPLICATION_JSON_VALUE)
		)
				.andDo(print())
				.andExpect(status().isOk())
				.andReturn();

		String responseBody = mvcResult.getResponse().getContentAsString();
		PathResponse pathResponse = objectMapper.readValue(responseBody, PathResponse.class);

		//then
		assertThat(pathResponse.getStations()).hasSize(3);
		assertThat(pathResponse.getTotalDistance()).isEqualTo(23);
		assertThat(pathResponse.getTotalDuration()).isEqualTo(23);
	}

	StationResponse createStation(String name) {
		Map<String, String> params = new HashMap<>();
		params.put("name", name);

		return post(
				"/stations",
				params,
				StationResponse.class
		);
	}

	LineResponse createLine(String name) {
		Map<String, String> params = new HashMap<>();
		params.put("name", name);
		params.put("startTime", LocalTime.of(5, 30).format(DateTimeFormatter.ISO_LOCAL_TIME));
		params.put("endTime", LocalTime.of(23, 30).format(DateTimeFormatter.ISO_LOCAL_TIME));
		params.put("intervalTime", "10");

		return post(
				"/lines",
				params,
				LineResponse.class
		);
	}

	void addEdge(Long lineId, Long preStationId, Long stationId, Integer distance,
			Integer duration) {
		Map<String, String> params = new HashMap<>();
		params.put("preStationId", preStationId == null ? "" : preStationId.toString());
		params.put("stationId", stationId.toString());
		params.put("distance", distance.toString());
		params.put("duration", duration.toString());

		post(
				"/lines/" + lineId + "/stations",
				params,
				LineResponse.class
		);
	}
}
