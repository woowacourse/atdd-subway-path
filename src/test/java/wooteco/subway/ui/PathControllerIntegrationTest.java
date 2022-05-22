package wooteco.subway.ui;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static wooteco.subway.Fixtures.STATION_3;
import static wooteco.subway.Fixtures.STATION_1;
import static wooteco.subway.Fixtures.LINE_1;
import static wooteco.subway.Fixtures.RED;
import static wooteco.subway.Fixtures.STATION_2;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Objects;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import wooteco.subway.dto.request.CreateLineRequest;
import wooteco.subway.dto.request.CreateSectionRequest;
import wooteco.subway.dto.request.CreateStationRequest;

@AutoConfigureMockMvc
@SpringBootTest
@Sql("/truncate.sql")
public class PathControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("경로 조회를 요청한다.")
    void find() throws Exception {
        // given
        final Long stationId1 = createStation(STATION_1);
        final Long stationId2 = createStation(STATION_2);
        final Long stationId3 = createStation(STATION_3);
        final Long lineId = createLine(LINE_1, RED, stationId1, stationId2, 10);
        createSection(lineId, stationId2, stationId3, 10);

        // when
        final ResultActions response = mockMvc.perform(
                        get("/paths?source=" + stationId1 + "&target=" + stationId3 + "&age=" + 29))
                .andDo(print());

        // then
        response.andExpect(status().isOk())
                .andExpect(jsonPath("stations[0].id").value(stationId1))
                .andExpect(jsonPath("stations[0].name").value(STATION_1))
                .andExpect(jsonPath("stations[1].id").value(stationId2))
                .andExpect(jsonPath("stations[1].name").value(STATION_2))
                .andExpect(jsonPath("stations[2].id").value(stationId3))
                .andExpect(jsonPath("stations[2].name").value(STATION_3))
                .andExpect(jsonPath("distance").value(20))
                .andExpect(jsonPath("fare").value(1450));
    }

    @Test
    @DisplayName("경로 조회를 실패한다.")
    void find_fail() throws Exception {
        // given
        final Long stationId1 = createStation(STATION_1);
        final Long stationId2 = createStation(STATION_2);
        final Long stationId3 = createStation(STATION_3);
        createLine(LINE_1, RED, stationId1, stationId2, 10);

        // when
        final ResultActions response = mockMvc.perform(
                        get("/paths?source=" + stationId1 + "&target=" + stationId3 + "&age=" + 29))
                .andDo(print());

        // then
        response.andExpect(status().isBadRequest())
                .andExpect(jsonPath("message").value("경로를 찾을 수 없습니다."));
    }

    private Long createStation(final String name) throws Exception {
        final CreateStationRequest request = new CreateStationRequest(name);
        final String requestContent = objectMapper.writeValueAsString(request);

        return Long.parseLong(Objects.requireNonNull(mockMvc.perform(post("/stations")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestContent))
                        .andReturn()
                        .getResponse()
                        .getHeader("Location"))
                .split("/")[2]);
    }

    private Long createLine(final String name, final String color, final Long upStationId, final Long downStationId,
                            final int distance) throws Exception {
        final CreateLineRequest request = new CreateLineRequest(name, color, upStationId, downStationId, distance);
        final String requestContent = objectMapper.writeValueAsString(request);

        return Long.parseLong(Objects.requireNonNull(mockMvc.perform(post("/lines")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestContent))
                        .andReturn()
                        .getResponse()
                        .getHeader("Location"))
                .split("/")[2]);
    }

    private void createSection(final Long lineId, final Long upStationId, final Long downStationId, final int distance)
            throws Exception {
        final CreateSectionRequest request = new CreateSectionRequest(upStationId, downStationId, distance);
        final String requestContent = objectMapper.writeValueAsString(request);

        // when
        mockMvc.perform(post("/lines/" + lineId + "/sections")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestContent));
    }
}
