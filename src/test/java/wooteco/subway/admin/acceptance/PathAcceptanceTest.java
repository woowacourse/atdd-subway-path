package wooteco.subway.admin.acceptance;

import org.junit.jupiter.api.BeforeEach;
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

    /**
     *
     * Feature : 지하철 경로 조회
     *
     * Scenario : 자하철 경로를 조회한다.
     * When : 출발역, 도착역 값을 입력받는다.
     * Then : 출발역부터 도착역까지의 경로와 최단시간을 반환한다.
     */
    @Test
    void getPath() throws Exception {
        // 같은 라인에 역이 2개 이상 등록되어있고
        // 출발역과 도착역을 입력했을때
        // 최단거리를 구해서
        // 경로와 소요시간, 거리 반환
        //given
        LineResponse line = createLine("2호선");
        StationResponse jamsil = createStation("잠실");
        StationResponse hongik = createStation("홍대입구");
        StationResponse sinchon = createStation("신촌");

        addEdge(line.getId(), null, jamsil.getId(), 10, 10);
        addEdge(line.getId(), jamsil.getId(), hongik.getId(), 11, 11);
        addEdge(line.getId(), hongik.getId(), sinchon.getId(), 12, 12);

        //when
        Long sourceId = jamsil.getId();
        Long targetId = sinchon.getId();

        MvcResult mvcResult = mockMvc.perform(
                get("/paths")
                        .param("source", String.valueOf(sourceId))
                        .param("target", String.valueOf(targetId))
                        .accept(MediaType.APPLICATION_JSON_VALUE)
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = mvcResult.getResponse().getContentAsString();
        PathResponse pathResponse = objectMapper.readValue(responseBody, PathResponse.class);

        //then
        assertThat(pathResponse.getEdges()).hasSize(3);
        assertThat(pathResponse.getTotalDistance()).isEqualTo(33);
        //TODO 추후 최소시간 검증 필요
    }
}
