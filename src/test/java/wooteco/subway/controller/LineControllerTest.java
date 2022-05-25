package wooteco.subway.controller;

import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import wooteco.subway.dto.LineRequest;
import wooteco.subway.exception.DuplicateNameException;
import wooteco.subway.service.LineService;
import wooteco.subway.service.SectionService;
import wooteco.subway.ui.LineController;

@WebMvcTest(LineController.class)
class LineControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private LineService lineService;

    @MockBean
    private SectionService sectionService;

    @DisplayName("중복된 노선을 생성할 경우 예외를 발생한다.")
    @Test
    void createLine_throwsExceptionWithDuplicatedPath() throws Exception {
        final LineRequest lineRequest = new LineRequest("5호선", "bg-purple-600", 1L, 2L, 10, 0);
        final String content = objectMapper.writeValueAsString(lineRequest);

        given(lineService.createLine(any())).willThrow(new DuplicateNameException("이미 존재하는 노선입니다."));

        mockMvc.perform(post("/lines")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("이미 존재하는 노선입니다."));
    }
}
