package wooteco.subway.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import wooteco.subway.domain.Section;
import wooteco.subway.dto.LineRequest;
import wooteco.subway.dto.LineResponse;
import wooteco.subway.dto.SectionRequest;
import wooteco.subway.dto.SectionsResponse;
import wooteco.subway.service.LineService;
import wooteco.subway.service.SectionService;

import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {LineController.class})
class LineControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private LineService lineService;

    @MockBean
    private SectionService sectionService;

    @Test
    @DisplayName("지하철 노선 등록")
    void createLine() throws Exception {
        LineResponse lineResponse = new LineResponse(1L, "2호선", "green", 10, Set.of());
        LineRequest lineRequest = new LineRequest("2호선", "green", 1L, 2L, 10, 10);
        String content = objectMapper.writeValueAsString(lineRequest);

        given(lineService.save(any())).willReturn(lineResponse);

        mockMvc.perform(post("/lines")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(print());
    }

    @Test
    @DisplayName("지하철 노선 목록 조회")
    void showLines() throws Exception {
        List<LineResponse> lineResponses = List.of(new LineResponse(1L, "2호선", "green", 10, Set.of()));

        given(lineService.findAll()).willReturn(lineResponses);

        mockMvc.perform(get("/lines"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("지하철 노선 조회")
    void showLine() throws Exception {
        given(lineService.findById(1L)).willReturn(new LineResponse(1L, "2호선", "green", 10, Set.of()));

        mockMvc.perform(get("/lines/" + 1))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("지하철 노선 수정")
    void modifyLine() throws Exception {
        LineRequest lineRequest = new LineRequest("2호선", "green",1L, 2L, 10, 10);
        String content = objectMapper.writeValueAsString(lineRequest);

        given(lineService.update(1L, lineRequest)).willReturn(1);

        mockMvc.perform(put("/lines/" + 1)
                .content(content)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("지하철 노선 삭제")
    void deleteLine() throws Exception {
        given(lineService.delete(1L)).willReturn(1);

        mockMvc.perform(delete("/lines/" + 1))
                .andExpect(status().isNoContent())
                .andDo(print());
    }

    @Test
    @DisplayName("지하철 구간 생성")
    void saveSection() throws Exception {
        LineResponse lineResponse = new LineResponse(1L, "2호선", "green", 10, Set.of());
        SectionsResponse sectionsResponse = new SectionsResponse(List.of(new Section(1L, 1L, 1L, 2L, 10)));
        SectionRequest sectionRequest = new SectionRequest(1L, 2L, 10);

        doNothing().when(sectionService).save(1L, sectionRequest, sectionsResponse, lineResponse);

        String content = objectMapper.writeValueAsString(sectionRequest);
        mockMvc.perform(post("/lines/"+ 1 + "/sections")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(print());
    }

    @Test
    @DisplayName("지하철 구간 제거")
    void deleteSection() throws Exception {
        doNothing().when(sectionService).delete(1L, 1L);

        mockMvc.perform(delete("/lines/"+ 1 + "/sections")
                .param("stationId", String.valueOf(1L)))
                .andExpect(status().isOk())
                .andDo(print());
    }
}
