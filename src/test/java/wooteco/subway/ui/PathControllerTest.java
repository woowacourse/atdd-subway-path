package wooteco.subway.ui;

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
import wooteco.subway.domain.Station;
import wooteco.subway.service.StationService;

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
}
