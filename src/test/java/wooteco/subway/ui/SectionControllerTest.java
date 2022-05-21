package wooteco.subway.ui;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import wooteco.subway.dto.request.SectionRequest;
import wooteco.subway.service.SectionService;

@DisplayName("SectionController 는")
@WebMvcTest(SectionController.class)
class SectionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    private SectionService sectionService;

    @DisplayName("새로운 섹션을 생성할 때")
    @Nested
    class CreateSectionTest {

        @Test
        @DisplayName("유효한 섹션 정보가 요청으로 오면 새로운 섹션을 추가한다.")
        void successfullyCreateSection() throws Exception {
            mockMvc.perform(post("/lines/1/sections")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(
                            new SectionRequest(1L, 2L, 10))))
                    .andExpect(status().isOk());
        }
        
        @DisplayName("유효하지 않은 정보가 요청에 포함되 있어면 섹션을 추가하지 않는다.")
        @ParameterizedTest(name = "{index} {displayName} lineId={0} sectionRequest={1}")
        @ArgumentsSource(TestSourceProvider.class)
        void failedToCreateSection(final Long lineId, final SectionRequest body) throws Exception {
            final String requestUrl = "/lines/" + lineId + "/sections";
            mockMvc.perform(post(requestUrl)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(body)))
                    .andExpect(status().isBadRequest());
        }
    }

    static class TestSourceProvider implements ArgumentsProvider {
        @Override
        public Stream<Arguments> provideArguments(ExtensionContext context) {
            return Stream.of(
                    Arguments.of(0L, new SectionRequest(1L, 2L, 10)),
                    Arguments.of(1L, new SectionRequest(0L, 2L, 10)),
                    Arguments.of(1L, new SectionRequest(1L, 0L, 10)),
                    Arguments.of(1L, new SectionRequest(1L, 2L, 0))
            );
        }
    }

    @DisplayName("특적 섹션을 지울 때")
    @Nested
    class DeleteSectionTest {

        @Test
        @DisplayName("유효한 섹션 정보가 요청으로 오면 새로운 섹션을 지운다.")
        void successfullyCreateSection() throws Exception {
            mockMvc.perform(delete("/lines/1/sections?stationId=1"))
                    .andExpect(status().isOk());
        }

        @DisplayName("유효하지 않은 정보가 요청에 포함되 있어면 섹션을 지우지 않는다.")
        @ParameterizedTest(name = "{index} {displayName} lineId={0} sectionRequest={1}")
        @CsvSource(value = {"1, 0", "0, 1"})
        void failedToCreateSection(final Long lineId, final Long stationId) throws Exception {
            final String requestUrl = "/lines/" + lineId + "/sections?stationId=" + stationId;
            mockMvc.perform(delete(requestUrl))
                    .andExpect(status().isBadRequest());
        }
    }
}
