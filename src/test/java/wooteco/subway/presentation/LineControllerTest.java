package wooteco.subway.presentation;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import wooteco.subway.dto.SectionRequest;
import wooteco.subway.dto.line.LineSaveRequest;
import wooteco.subway.dto.line.LineUpdateRequest;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:/errors.properties")
class LineControllerTest {

    @Autowired
    Environment environment;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @DisplayName("노선 생성 요청시 이름 혹은 색깔이 빈 값이면 예외 응답을 반환한다.")
    @Test
    void throwExceptionWhenNameOrColorBlank() throws Exception {
        String blank = " ";

        String request = objectMapper.writeValueAsString(new LineSaveRequest(blank, blank, 1L, 2L, 10, 10));

        mockMvc.perform(post("/lines")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpectAll(
                        status().isBadRequest(),
                        jsonPath("$.messages", Matchers.containsInAnyOrder(
                                environment.getProperty("name.notBlank"),
                                environment.getProperty("color.notBlank")))
                );
    }

    @DisplayName("노선 생성 요청시 숫자 필드가 음수이면 예외 응답을 반환한다.")
    @Test
    void throwExceptionWhenNumberFieldNegative() throws Exception {
        String request = objectMapper.writeValueAsString(new LineSaveRequest("name", "color", 1L, 2L, -1, -1));

        mockMvc.perform(post("/lines")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpectAll(
                        status().isBadRequest(),
                        jsonPath("$.messages", Matchers.containsInAnyOrder(
                                environment.getProperty("number.positive"),
                                environment.getProperty("extraFare.positiveOrZero")))
                );
    }

    @DisplayName("노선 생성 요청시 이름 혹은 색깔이 최대값을 넘으면 예외 응답을 반환한다.")
    @Test
    void throwExceptionWhenNameOrColorTooLong() throws Exception {

        String name = "가나다라마바사아자차카";
        StringBuilder color = new StringBuilder();
        for (int i = 0; i < 256; i++) {
            color.append(i);
        }

        String request = objectMapper.writeValueAsString(new LineSaveRequest(name, color.toString(), 1L, 2L, 1, 1));

        mockMvc.perform(post("/lines")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpectAll(
                        status().isBadRequest(),
                        jsonPath("$.messages", Matchers.containsInAnyOrder(
                                environment.getProperty("name.tooLong"),
                                environment.getProperty("string.tooLong")))
                );
    }

    @DisplayName("노선 수정 요청시 유효하지 않은 값을 입력하면 예외 응답을 반환한다.")
    @Test
    void throwExceptionWhenInvalidUpdateRequest() throws Exception {

        String name = "가나다라마바사아자차카";
        StringBuilder color = new StringBuilder();
        for (int i = 0; i < 256; i++) {
            color.append(i);
        }

        String request = objectMapper.writeValueAsString(new LineUpdateRequest(name, color.toString(), -1));

        mockMvc.perform(put("/lines/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpectAll(
                        status().isBadRequest(),
                        jsonPath("$.messages", Matchers.containsInAnyOrder(
                                environment.getProperty("name.tooLong"),
                                environment.getProperty("string.tooLong"),
                                environment.getProperty("extraFare.positiveOrZero"))
                        )
                );
    }

    @DisplayName("구간 추가 요청시 유효하지 않은 값을 입력하면 예외 응답을 반환한다.")
    @Test
    void throwExceptionWhenInvalidSectionRequest() throws Exception {

        String request = objectMapper.writeValueAsString(new SectionRequest(1L, 2L, -1));

        mockMvc.perform(post("/lines/1/sections")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpectAll(
                        status().isBadRequest(),
                        jsonPath("$.messages", Matchers.containsInAnyOrder(
                                environment.getProperty("number.positive"))
                        )
                );
    }
}
