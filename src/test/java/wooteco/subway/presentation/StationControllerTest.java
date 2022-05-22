package wooteco.subway.presentation;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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
import wooteco.subway.dto.StationRequest;


@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:/errors.properties")
class StationControllerTest {

    @Autowired
    Environment environment;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @DisplayName("역 생성 요청시 빈 값이 들어오면 예외 응답을 반환한다")
    @Test
    void sendErrorResponseWithBlankName() throws Exception {
        String blank = " ";
        StationRequest request = new StationRequest(blank);

        String requestString = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/stations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestString))
                .andExpectAll(
                        status().isBadRequest(),
                        jsonPath("$.messages", Matchers.containsInAnyOrder(environment.getProperty("name.notBlank")))
                )
                .andDo(print());
    }
}
