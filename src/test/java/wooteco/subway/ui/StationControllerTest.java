package wooteco.subway.ui;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import wooteco.subway.domain.Station;
import wooteco.subway.domain.factory.StationFactory;
import wooteco.subway.service.StationService;

@WebMvcTest(StationController.class)
@DisplayName("StationController 는")
class StationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private StationService stationService;

    @DisplayName("역을 생성할 때")
    @Nested
    class CreateStationTest {

        @Test
        @DisplayName("요쳥에 역 이름이 존재하면 역을 추가한다.")
        void createStationTest() throws Exception {
            given(stationService.saveStation(any()))
                    .willReturn(StationFactory.from(StationFactory.A));

            mockMvc.perform(post("/stations")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(Map.of("name", "a"))))
                    .andExpect(status().isCreated());

        }

        @Test
        @DisplayName("요청에 역 이름이 존재하지 않으면 역을 추가하지 않는다.")
        void createStationWithBlankName() throws Exception {
            mockMvc.perform(post("/stations")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(Map.of("name", ""))))
                    .andExpect(status().isBadRequest());

        }
    }

    @DisplayName("역들을 반환한다.")
    @Test
    void showStations() throws Exception {
        final Station station1 = StationFactory.from(StationFactory.A);
        final Station station2 = StationFactory.from(StationFactory.B);
        given(stationService.findAll())
                .willReturn(List.of(station1, station2));

        mockMvc.perform(get("/stations"))
                .andExpect(status().isOk());
    }

    @DisplayName("특정 역을 지울 때 역 식별자가")
    @Nested
    class DeleteStationTest {

        @DisplayName("1보다 크면 특정 역을 지운다.")
        @Test
        void deleteStationSuccess() throws Exception {
            mockMvc.perform(delete("/stations/1"))
                    .andExpect(status().isNoContent());
        }

        @DisplayName("0이하면 역을 지우지 않는다.")
        @Test
        void deleteStationFailed() throws Exception {
            mockMvc.perform(delete("/stations/0"))
                    .andExpect(status().isBadRequest());
        }
    }
}
