package wooteco.subway.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import wooteco.subway.dto.StationResponse;
import wooteco.subway.service.StationService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {StationController.class})
class StationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private StationService stationService;

    @Test
    @DisplayName("지하철 역 등록")
    void createStation() throws Exception {
        StationResponse station = new StationResponse(1L, "강남역");
        String content = objectMapper.writeValueAsString(station);

        given(stationService.save(any())).willReturn(station);

        mockMvc.perform(post("/stations")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(print());
    }

    @Test
    @DisplayName("지하철 역 목록 조회")
    void findStation() throws Exception {
        List<StationResponse> stationResponses = List.of(new StationResponse(1L, "강남역"), new StationResponse(2L, "역삼역"));

        given(stationService.findAll()).willReturn(stationResponses);

        mockMvc.perform(get("/stations"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("지하철 역 삭제")
    void deleteStation() throws Exception {
        given(stationService.delete(1L)).willReturn(1);

        mockMvc.perform(delete("/stations/" + 1))
                .andExpect(status().isNoContent())
                .andDo(print());
    }
}
