package wooteco.subway.admin.api;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import wooteco.subway.admin.config.ETagHeaderFilter;
import wooteco.subway.admin.controller.restcontroller.PathController;
import wooteco.subway.admin.domain.Line;
import wooteco.subway.admin.domain.Station;
import wooteco.subway.admin.dto.response.LineDetailResponse;
import wooteco.subway.admin.dto.response.WholeSubwayResponse;
import wooteco.subway.admin.service.PathService;

@WebMvcTest(controllers = {PathController.class})
@Import(ETagHeaderFilter.class)
public class PathControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PathService pathService;

    @Test
    void ETag() throws Exception {
        WholeSubwayResponse response = WholeSubwayResponse.of(
            Arrays.asList(createMockResponse(), createMockResponse()));
        given(pathService.wholeLines()).willReturn(response);

        String uri = "/paths/lines/detail";

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

    private LineDetailResponse createMockResponse() {
        Line mockLine = Line.of("lineName", "bg-green-500", LocalTime.now(), LocalTime.now(), 1);
        Station mockStation = Station.of("stationName");
        List<Station> stations = Arrays.asList(mockStation, mockStation, mockStation);
        return LineDetailResponse.of(mockLine, stations);
    }
}
