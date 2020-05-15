package wooteco.subway.admin.api;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import wooteco.subway.admin.config.ETagHeaderFilter;
import wooteco.subway.admin.controller.LineController;
import wooteco.subway.admin.domain.Line;
import wooteco.subway.admin.domain.Station;
import wooteco.subway.admin.dto.LineResponse;
import wooteco.subway.admin.dto.StationResponse;
import wooteco.subway.admin.dto.WholeSubwayResponse;
import wooteco.subway.admin.service.LineService;

@WebMvcTest(controllers = {LineController.class})
@Import(ETagHeaderFilter.class)
public class LineControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private LineService lineService;

	@DisplayName("수정되지 않았을 때, NotModified 반환")
	@Test
	void ETag() throws Exception {
		WholeSubwayResponse response = new WholeSubwayResponse(
			Arrays.asList(createMockResponse(), createMockResponse()));
		given(lineService.wholeLines()).willReturn(response);

		String uri = "/api/lines/detail";

		MvcResult mvcResult = mockMvc.perform(get(uri))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(header().exists("ETag"))
			.andReturn();

		String eTag = mvcResult.getResponse().getHeader("ETag");

		mockMvc.perform(get(uri).header("If-None-Match", eTag))
			.andDo(print())
			.andExpect(status().isNotModified())
			.andExpect(header().exists("ETag"))
			.andReturn();
	}

	@DisplayName("수정되었을 때, 새로운 값 반환")
	@Test
	void ETag_Modified() throws Exception {
		WholeSubwayResponse response = new WholeSubwayResponse(
			Arrays.asList(createMockResponse(), createMockResponse()));
		given(lineService.wholeLines()).willReturn(response);

		String uri = "/api/lines/detail";

		MvcResult mvcResult = mockMvc.perform(get(uri))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(header().exists("ETag"))
			.andReturn();

		String eTag = mvcResult.getResponse().getHeader("ETag");

		response = new WholeSubwayResponse(
			Arrays.asList(createMockResponse(), createMockResponse(), createMockResponse()));
		given(lineService.wholeLines()).willReturn(response);
		mockMvc.perform(get(uri).header("If-None-Match", eTag))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(header().exists("ETag"))
			.andReturn();
	}

	private LineResponse createMockResponse() {
		List<Station> stations = Arrays.asList(new Station(), new Station(), new Station());
		return LineResponse.of(new Line(), StationResponse.listOf(stations));
	}
}
