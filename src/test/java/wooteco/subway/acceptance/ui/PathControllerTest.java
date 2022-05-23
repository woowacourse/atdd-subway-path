package wooteco.subway.acceptance.ui;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.application.StationService;
import wooteco.subway.domain.Station;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Sql({"/schema.sql", "/test-data.sql"})
public class PathControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private StationService stationService;

    @DisplayName("출발역부터 도착역까지의 최단 경로와 요금 및 거리를 조회한다.")
    @Test
    void getPath() throws Exception {
        final Station departure = stationService.findStationById(1L);
        final Station arrival = stationService.findStationById(3L);

        mockMvc.perform(get("/paths?source=" + departure.getId() + "&target=" + arrival.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("distance").value(10))
                .andExpect(jsonPath("fare").value(1250))
                .andExpect(jsonPath("stations[0].name").value(departure.getName()))
                .andExpect(jsonPath("stations[1].name").value(arrival.getName()))
                .andDo(print());
    }

    @DisplayName("추가 요금이 있는 노선 여러개를 지나면 가장 높은 추가요금을 적용한다.")
    @Test
    void getPathHavingExtraFareLine() throws Exception {
        final Station departure = stationService.findStationById(4L);
        final Station arrival = stationService.findStationById(6L);
        final int expectedFare = 1750;

        mockMvc.perform(get("/paths?source=" + departure.getId() + "&target=" + arrival.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("distance").value(10))
                .andExpect(jsonPath("fare").value(expectedFare))
                .andExpect(jsonPath("stations[0].name").value(departure.getName()))
                .andExpect(jsonPath("stations[1].name").value("공덕역"))
                .andExpect(jsonPath("stations[2].name").value(arrival.getName()))
                .andDo(print());
    }
}
