package wooteco.subway.admin.api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import wooteco.subway.admin.config.ETagHeaderFilter;
import wooteco.subway.admin.controller.LineController;
import wooteco.subway.admin.domain.Line;
import wooteco.subway.admin.domain.Station;
import wooteco.subway.admin.dto.LineDetailResponse;
import wooteco.subway.admin.dto.WholeSubwayResponse;
import wooteco.subway.admin.service.LineService;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyObject;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {LineController.class})
@Import(ETagHeaderFilter.class)
public class LineControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LineService lineService;

    @Test
    void ETag() throws Exception {
        WholeSubwayResponse response = WholeSubwayResponse.of(Arrays.asList(createMockResponse(), createMockResponse()));
        when(lineService.wholeLines()).thenReturn(response);
        when(lineService.save(any())).thenReturn(new Line(1L, "1호선", LocalTime.NOON, LocalTime.MIDNIGHT, 10));

        String getDetailUri = "/lines/detail";
        String getUri = "/lines";
        String postUri = "/lines";

        MvcResult mvcResult = mockMvc.perform(get(getDetailUri))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().exists("ETag"))
                .andReturn();

        String eTag = mvcResult.getResponse().getHeader("ETag");

        mockMvc.perform(get(getDetailUri).header("If-None-Match", eTag))
                .andDo(print())
                .andExpect(status().isNotModified())
                .andExpect(header().exists("ETag"))
                .andReturn();

        WholeSubwayResponse updatedResponse = WholeSubwayResponse.of(Collections.singletonList(LineDetailResponse.of(new Line(1L, "1호선", LocalTime.NOON, LocalTime.MIDNIGHT, 10), new ArrayList<>())));
        when(lineService.wholeLines()).thenReturn(updatedResponse);
        mockMvc.perform(post(postUri)
                .contentType(new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), StandardCharsets.UTF_8))
                .content("{\n" +
                        "    \"name\": \"1호선\",\n" +
                        "    \"startTime\": \"14:00:00\",\n" +
                        "    \"endTime\": \"15:00:00\",\n" +
                        "    \"intervalTime\": 30\n" +
                        "}"))
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn();

        MvcResult result = mockMvc.perform(get(getDetailUri).header("If-None-Match", eTag))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().exists("ETag"))
                .andReturn();

        String updatedETag = result.getResponse().getHeader("ETag");

        MvcResult otherResult = mockMvc.perform(get(getUri).header("If-None-Match", updatedETag))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().exists("ETag"))
                .andReturn();

        String otherETag = otherResult.getResponse().getHeader("ETag");

        assertThat(updatedETag).isNotEqualTo(otherETag);
    }

    private LineDetailResponse createMockResponse() {
        List<Station> stations = Arrays.asList(new Station(), new Station(), new Station());
        return LineDetailResponse.of(new Line(), stations);
    }
}
