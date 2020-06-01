package wooteco.subway.admin.api;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import wooteco.subway.admin.controller.PathController;
import wooteco.subway.admin.dto.PathResponse;
import wooteco.subway.admin.service.PathService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = PathController.class)
public class PathControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PathService pathService;

    @DisplayName("최단경로 요청을 보내면 ok응답이 오는지 테스트")
    @Test
    void calculatePathTest() throws Exception {
        given(pathService.calculatePath(any())).willReturn(new PathResponse());

        this.mockMvc.perform(get("/paths?source={source}&target={target}&type={type}", "1", "2", "DISTANCE"))
                .andExpect(status().isOk())
                .andDo(print());
    }
}
