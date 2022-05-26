package wooteco.subway.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import wooteco.subway.service.PathService;
import wooteco.subway.ui.PathController;

@WebMvcTest(PathController.class)
class PathControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PathService pathService;

    @DisplayName("경로 탐색시 출발역이 공백일 경우 예외를 발생한다.")
    @Test
    void createPath_throwsSourceNotExistsException() throws Exception {
        mockMvc.perform(get("/paths")
                        .param("target", String.valueOf(2L))
                        .param("age", String.valueOf(1)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("출발역은 공백일 수 없습니다."));
    }

    @DisplayName("경로 탐색시 도착역이 공백일 경우 예외를 발생한다.")
    @Test
    void createPath_throwsTargetNotExistsException() throws Exception {
        mockMvc.perform(get("/paths")
                        .param("source", String.valueOf(2L))
                        .param("age", String.valueOf(1)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("도착역은 공백일 수 없습니다."));
    }

    @DisplayName("경로 탐색시 나이가 음수이거나 공백일 경우 예외를 발생한다.")
    @Test
    void createPath_throwsInvalidAgeException() throws Exception {
        mockMvc.perform(get("/paths")
                        .param("source", String.valueOf(1L))
                        .param("target", String.valueOf(2L))
                        .param("age", String.valueOf(-1)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("나이는 공백이거나 음수일 수 없습니다."));
    }

}
