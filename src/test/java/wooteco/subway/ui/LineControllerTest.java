package wooteco.subway.ui;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import wooteco.subway.domain.Line;
import wooteco.subway.domain.Sections;
import wooteco.subway.domain.factory.SectionFactory;
import wooteco.subway.dto.response.LineResponse;
import wooteco.subway.dto.response.StationResponse;
import wooteco.subway.service.LineService;

@DisplayName("LineController 는")
@WebMvcTest(LineController.class)
public class LineControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private LineService lineService;

    private final LineResponse response = new LineResponse(1L, "a", "bg-red-600", 10,
            List.of(new StationResponse(1L, "sa"), new StationResponse(2L, "sb")));

    private final Map<String, Object> body = new HashMap<>(Map.ofEntries(
            Map.entry("name", "a"),
            Map.entry("color", "bg-red-600"),
            Map.entry("upStationId", 1L),
            Map.entry("downStationId", 2L),
            Map.entry("distance", 10)
    ));


    @DisplayName("새로운 라인을 생성할 때")
    @Nested
    class CreateLineTest {

        @Test
        @DisplayName("라인 생성에 필요한 데이터가 모두 유효하면 라인을 생성해야 한다.")
        void successfullyCreateLine() throws Exception {
            given(lineService.saveLine(any())).willReturn(response);

            mockMvc.perform(post("/lines")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(body)))
                    .andExpect(status().isCreated());
        }

        @DisplayName("라인 생성에 필요한 데이터 중 하나가 비어 있거나, 역을 나타내는 식별자가 0이하면 라인을 생성하지 않는다.")
        @ParameterizedTest(name = "{index} {displayName} name={0}, color={1}, upStationId={2}, downStationId={3}, distance={4}")
        @CsvSource(value = {"'', bg-red-600, 1, 2, 10", "a, bg-red-600, 0, 2, 10"})
        void failedToCreateLine(final String name, final String color, final Long upStationId, final Long downStationId,
                            final int distance) throws Exception {
            given(lineService.saveLine(any())).willReturn(response);

            final Map<String, Object> body = new HashMap<>(Map.ofEntries(
                    Map.entry("name", name),
                    Map.entry("color", color),
                    Map.entry("upStationId", upStationId),
                    Map.entry("downStationId", downStationId),
                    Map.entry("distance", distance)
            ));

            mockMvc.perform(post("/lines")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(body)))
                    .andExpect(status().isBadRequest());
        }
    }

    @DisplayName("기존에 존재하는 라인들을 가져올 수 있어야 한다.")
    @Test
    void showLines() throws Exception {
        final Sections sections = new Sections(List.of(SectionFactory.from(SectionFactory.AB3),
                SectionFactory.from(SectionFactory.BC3)));
        given(lineService.findAll())
                .willReturn(List.of(new Line(1L, "a", "bg-red-600", sections, 1L, 10)));

        mockMvc.perform(get("/lines"))
                .andExpect(status().isOk());
    }

    @DisplayName("기존에 존재하는 특정 라인을 가져올 떄")
    @Nested
    class ShowLineTest {

        @Test
        @DisplayName("라인 아이디가 1보다 크면 라인 정보를 가져온다.")
        void successfullyShowLine() throws Exception {
            given(lineService.findById(any()))
                    .willReturn(response);

            mockMvc.perform(get("/lines/1"))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("라인 아이디가 0 이하면 라인 정보를 반환하지 않는다.")
        void FailedToShowLine() throws Exception {
            mockMvc.perform(get("/lines/0"))
                    .andExpect(status().isBadRequest());
        }
    }

    @DisplayName("라인 정보를 업데이트할 때")
    @Nested
    class UpdateLineTest {

        @DisplayName("라인 아이디가 1 이상이면 라인 정보를 업데이트 한다.")
        @Test
        void successfullyUpdateLine() throws Exception {
            mockMvc.perform(put("/lines/1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(body)))
                    .andExpect(status().isOk());
        }

        @DisplayName("라인 아이디가 0 이하면 라인 정보를 업데이트 하지 않는다.")
        @Test
        void failedToCreateLine() throws Exception {
            mockMvc.perform(put("/lines/0")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(body)))
                    .andExpect(status().isBadRequest());
        }
    }

    @DisplayName("특정 라인을 지울 떄")
    @Nested
    class DeleteLineTest {

        @DisplayName("라인 아이디가 1 이상이면 해당 라인을 지운다.")
        @Test
        void successfullyDeleteLine() throws Exception {
            mockMvc.perform(delete("/lines/1"))
                    .andExpect(status().isNoContent());
        }

        @DisplayName("라인 아이디가 0 이하면 라인을 지우지 않는다.")
        @Test
        void filedToDeleteLine() throws Exception {
            mockMvc.perform(delete("/lines/0"))
                    .andExpect(status().isBadRequest());
        }
    }

}
