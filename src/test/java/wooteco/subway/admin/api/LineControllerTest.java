package wooteco.subway.admin.api;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import wooteco.subway.admin.config.ETagHeaderFilter;
import wooteco.subway.admin.controller.LineController;
import wooteco.subway.admin.domain.Line;
import wooteco.subway.admin.domain.Station;
import wooteco.subway.admin.service.LineService;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = {LineController.class})
@Import(ETagHeaderFilter.class)
public class LineControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LineService lineService;

    @Test
    void ETag() throws Exception {
        Line line = new Line(1L, "2호선", LocalTime.of(05, 30), LocalTime.of(22, 30), 5,
            "bg-green-600");
        List<Station> stations = Arrays.asList(new Station(1L, "강남역"), new Station(2L, "선릉역"));
        given(lineService.showLines()).willReturn(Collections.singletonList(line));
        given(lineService.findLineById(anyLong())).willReturn(line);
        given(lineService.findAllStationsByIds(anyList())).willReturn(stations);

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
}
