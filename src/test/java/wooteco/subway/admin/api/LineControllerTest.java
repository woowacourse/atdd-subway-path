package wooteco.subway.admin.api;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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

import wooteco.subway.admin.common.filter.ETagHeaderFilter;
import wooteco.subway.admin.line.controller.LineController;
import wooteco.subway.admin.line.domain.line.Line;
import wooteco.subway.admin.line.service.LineService;
import wooteco.subway.admin.line.service.dto.line.LineDetailResponse;
import wooteco.subway.admin.path.service.dto.WholeSubwayResponse;
import wooteco.subway.admin.station.domain.Station;

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

		String uri = "/lines/detail";

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

		String uri = "/lines/detail";

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

	private LineDetailResponse createMockResponse() {
		List<Station> stations = Arrays.asList(new Station(), new Station(), new Station());
		return LineDetailResponse.of(new Line(), stations);
	}
}
