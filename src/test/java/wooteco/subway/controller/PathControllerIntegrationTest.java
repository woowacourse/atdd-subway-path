package wooteco.subway.controller;

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
import wooteco.subway.domain.Line;
import wooteco.subway.domain.Station;
import wooteco.subway.dto.LineRequest;
import wooteco.subway.dto.SectionRequest;
import wooteco.subway.service.LineService;
import wooteco.subway.service.StationService;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Sql("/testSchema.sql")
public class PathControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private StationService stationService;

    @Autowired
    private LineService lineService;

    @DisplayName("최단 경로 탐색과 요금 계산")
    @Test
    void 최단_경로_탐색() throws Exception {
        /*
        2호선 : 합정 -1- 당산 -10- 대림 / 100원
        4호선 : 삼각지 -10- 동작 -1- 사당 -10- 과천 / 200원
        6호선 : 합정 -10- 삼각지 -10- 이태원 / 300원
        9호선 : 당산 -1- 여의도 -1- 동작 / 1000원

        합정 -> 사당

        경로 : 합정 - 당산 - 여의도 - 동작 - 사당
        경유 : 2호선, 9호선, 4호선
        추가요금 : 1000원
        */
        Station 합정 = stationService.save(new Station("합정역"));
        Station 당산 = stationService.save(new Station("당산역"));
        Station 대림 = stationService.save(new Station("대림역"));
        LineRequest 이호선요청 = new LineRequest("이호선", "green", 합정.getId(), 당산.getId(), 1, 100);
        Line 이호선 = lineService.save(이호선요청);
        lineService.addSection(이호선.getId(), new SectionRequest(당산.getId(), 대림.getId(), 10));

        Station 삼각지 = stationService.save(new Station("삼각지역"));
        Station 동작 = stationService.save(new Station("동작역"));
        Station 사당 = stationService.save(new Station("사당역"));
        Station 과천 = stationService.save(new Station("과천역"));
        LineRequest 사호선요청 = new LineRequest("사호선", "skyblue", 삼각지.getId(), 동작.getId(), 1, 200);
        Line 사호선 = lineService.save(사호선요청);
        lineService.addSection(사호선.getId(), new SectionRequest(동작.getId(), 사당.getId(), 1));
        lineService.addSection(사호선.getId(), new SectionRequest(사당.getId(), 과천.getId(), 10));

        Station 이태원 = stationService.save(new Station("이태원역"));
        LineRequest 육호선요청 = new LineRequest("육호선", "brown", 합정.getId(), 삼각지.getId(), 10, 300);
        Line 육호선 = lineService.save(육호선요청);
        lineService.addSection(육호선.getId(), new SectionRequest(삼각지.getId(), 이태원.getId(), 10));

        Station 여의도 = stationService.save(new Station("여의도역"));
        LineRequest 구호선요청 = new LineRequest("구호선", "gold", 당산.getId(), 여의도.getId(), 1, 1000);
        Line 구호선 = lineService.save(구호선요청);
        lineService.addSection(구호선.getId(), new SectionRequest(여의도.getId(), 동작.getId(), 1));

        mockMvc.perform(get("/paths?source=" + 합정.getId() + "&target=" + 사당.getId() + "&age=" + 25))
                .andExpect(status().isOk())
                .andExpect(jsonPath("stations[0].name").value("합정역"))
                .andExpect(jsonPath("stations[1].name").value("당산역"))
                .andExpect(jsonPath("stations[2].name").value("여의도역"))
                .andExpect(jsonPath("stations[3].name").value("동작역"))
                .andExpect(jsonPath("stations[4].name").value("사당역"))
                .andExpect(jsonPath("distance").value(4))
                .andExpect(jsonPath("fare").value(2250))
                .andDo(print());

    }

}
