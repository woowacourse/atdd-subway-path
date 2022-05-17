package wooteco.subway.ui;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import wooteco.subway.service.PathService;

@DisplayName("PathController 는 ")
@WebMvcTest(PathController.class)
class PathControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PathService pathService;

    @DisplayName("지하철 경로를 조회한다.")
    @Test
    void showPath() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .get("/path?source=1&target=3&age=15");

        mockMvc.perform(request)
                .andExpect(status().isOk());
    }
}
