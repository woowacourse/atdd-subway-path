package wooteco.subway.ui;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static wooteco.subway.Fixtures.GANGNAM;
import static wooteco.subway.Fixtures.HYEHWA;
import static wooteco.subway.Fixtures.LINE_2;
import static wooteco.subway.Fixtures.SKY_BLUE;
import static wooteco.subway.Fixtures.SUNGSHIN;

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
        final Long stationId1 = createStation(HYEHWA);
        final Long stationId2 = createStation(SUNGSHIN);
        final Long stationId3 = createStation(GANGNAM);
        final Long lineId = createLine(LINE_2, SKY_BLUE, stationId1, stationId2, 10, 900);
        createSection(lineId, stationId2, stationId3, 10);

        // when
        final ResultActions response = mockMvc.perform(
                        get("/paths?source=" + stationId1 + "&target=" + stationId3 + "&age=" + 29))
                .andDo(print());

        // then
        response.andExpect(status().isOk())
                .andExpect(jsonPath("stations[0].id").value(stationId1))
                .andExpect(jsonPath("stations[0].name").value(HYEHWA))
                .andExpect(jsonPath("stations[1].id").value(stationId2))
                .andExpect(jsonPath("stations[1].name").value(SUNGSHIN))
                .andExpect(jsonPath("stations[2].id").value(stationId3))
                .andExpect(jsonPath("stations[2].name").value(GANGNAM))
                .andExpect(jsonPath("distance").value(20))
                .andExpect(jsonPath("fare").value(1450));
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
                            final int distance, final int extraFare) throws Exception {
        final CreateLineRequest request = new CreateLineRequest(name, color, upStationId, downStationId, distance,
                extraFare);
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
