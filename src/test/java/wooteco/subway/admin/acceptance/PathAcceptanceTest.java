package wooteco.subway.admin.acceptance;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import wooteco.subway.admin.dto.LineResponse;
import wooteco.subway.admin.dto.PathResponse;
import wooteco.subway.admin.dto.StationResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class PathAcceptanceTest extends AcceptanceTest {
    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;

    @Override
    @BeforeEach
    void setUp() {
        super.setUp();
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .addFilter(new CharacterEncodingFilter("UTF-8", true))
                .build();
    }

    @DisplayName("지하철 경로 조회")
    @Test
    void getPath() throws Exception {
        //given
        LineResponse line = createLine("2호선");
        StationResponse jamsil = createStation("잠실");
        StationResponse hongik = createStation("홍대입구");
        StationResponse sinchon = createStation("신촌");

        addEdge(line.getId(), null, jamsil.getId(), 10, 10);
        addEdge(line.getId(), jamsil.getId(), hongik.getId(), 11, 11);
        addEdge(line.getId(), hongik.getId(), sinchon.getId(), 12, 12);

        //when
        MvcResult mvcResult = mockMvc.perform(
                get("/paths")
                        .param("source", "잠실")
                        .param("target", "신촌")
                        .param("key", "distance")
                        .accept(MediaType.APPLICATION_JSON_VALUE)
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = mvcResult.getResponse().getContentAsString();
        PathResponse pathResponse = objectMapper.readValue(responseBody, PathResponse.class);

        //then
        assertThat(pathResponse.getStations()).hasSize(3);
        assertThat(pathResponse.getTotalDistance()).isEqualTo(23);
        assertThat(pathResponse.getTotalDuration()).isEqualTo(23);
    }
}
