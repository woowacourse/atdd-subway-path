package wooteco.subway.acceptance;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import wooteco.subway.acceptance.fixture.SimpleCreate;
import wooteco.subway.acceptance.fixture.SimpleResponse;
import wooteco.subway.acceptance.fixture.SimpleRestAssured;
import wooteco.subway.dto.request.LineCreateRequest;
import wooteco.subway.dto.request.StationRequest;
import wooteco.subway.dto.response.LineCreateResponse;

public class LineAcceptanceTest extends AcceptanceTest {

    @BeforeEach
    void setUpStations() {
        SimpleCreate.createStation(new StationRequest("강남역"));
        SimpleCreate.createStation(new StationRequest("역삼역"));
    }

    @Test
    @DisplayName("노선을 생성한다.")
    public void createLine() {
        // given
        LineCreateRequest lineCreateRequest = createRequest("신분당선", "bg-red-600");
        // when
        SimpleResponse response = SimpleRestAssured.post("/lines", lineCreateRequest);
        // then
        Assertions.assertAll(
                () -> response.assertStatus(HttpStatus.CREATED),
                () -> assertThat(response.getHeader("Location")).isNotBlank()
        );
    }

    @Test
    @DisplayName("입력값이 비어있는 경우 노선을 생성할 수 없다.")
    public void createLine_throwsExceptionWithBlankInput() {
        // given
        LineCreateRequest lineCreateRequest = createRequest("신분당선", "");
        // when
        SimpleResponse response = SimpleRestAssured.post("/lines", lineCreateRequest);
        // then
        Assertions.assertAll(
                () -> response.assertStatus(HttpStatus.BAD_REQUEST),
                () -> assertThat(response.containsExceptionMessage("필수 입력")).isTrue()
        );
    }

    @Test
    @DisplayName("기존에 존재하는 노선 이름으로 노선을 생성할 수 없다.")
    public void createLine_throwsExceptionWithDuplicatedName() {
        // given
        LineCreateRequest lineCreateRequest = createRequest("신분당선", "bg-red-600");
        SimpleRestAssured.post("/lines", lineCreateRequest);
        // when
        LineCreateRequest invalidLineCreateRequest = createRequest("신분당선", "bg-red-600");
        SimpleResponse createdResponse = SimpleRestAssured.post("/lines", invalidLineCreateRequest);
        // then
        Assertions.assertAll(
                () -> createdResponse.assertStatus(HttpStatus.BAD_REQUEST),
                () -> assertThat(createdResponse.containsExceptionMessage("이미 존재")).isTrue()
        );
    }

    @Test
    @DisplayName("전체 노선 목록을 조회한다.")
    void getLines() {
        /// given
        LineCreateRequest 신분당선 = createRequest("신분당선", "bg-red-600");
        LineCreateRequest 경의중앙선 = createRequest("경의중앙선", "bg-red-800");

        SimpleResponse createResponse1 = SimpleRestAssured.post("/lines", 신분당선);
        SimpleResponse createResponse2 = SimpleRestAssured.post("/lines", 경의중앙선);
        // when
        SimpleResponse response = SimpleRestAssured.get("/lines");
        // then
        List<Long> expectedLineIds = Stream.of(createResponse1, createResponse2)
                .map(SimpleResponse::getIdFromLocation)
                .collect(Collectors.toList());
        List<Long> resultLineIds = response.toList(LineCreateResponse.class).stream()
                .map(LineCreateResponse::getId)
                .collect(Collectors.toList());

        Assertions.assertAll(
                () -> response.assertStatus(HttpStatus.OK),
                () -> assertThat(resultLineIds).containsAll(expectedLineIds)
        );
    }

    @Test
    @DisplayName("ID값으로 노선을 조회한다.")
    public void getLine() {
        // given
        LineCreateRequest lineCreateRequest = createRequest("신분당선", "bg-red-600");
        SimpleResponse createdResponse = SimpleRestAssured.post("/lines", lineCreateRequest);
        // when
        String uri = createdResponse.getHeader("Location");
        SimpleResponse foundResponse = SimpleRestAssured.get(uri);
        LineCreateResponse createdLineCreateResponse = createdResponse.toObject(LineCreateResponse.class);
        LineCreateResponse foundLineCreateResponse = foundResponse.toObject(LineCreateResponse.class);
        // then
        Assertions.assertAll(
                () -> foundResponse.assertStatus(HttpStatus.OK),
                () -> assertThat(foundLineCreateResponse.getId()).isEqualTo(createdLineCreateResponse.getId())
        );
    }

    @Test
    @DisplayName("존재하지 않는 ID값으로 노선을 조회할 수 없다.")
    public void getLine_throwExceptionWithInvalidId() {
        // given
        LineCreateRequest lineCreateRequest = createRequest("신분당선", "bg-red-600");
        SimpleRestAssured.post("/lines", lineCreateRequest);
        // when
        SimpleResponse response = SimpleRestAssured.get("/lines/99");
        // then
        Assertions.assertAll(
                () -> response.assertStatus(HttpStatus.BAD_REQUEST),
                () -> assertThat(response.containsExceptionMessage("존재하지 않습니다")).isTrue()
        );
    }

    @Test
    @DisplayName("ID값으로 노선을 수정한다.")
    public void modifyLine() {
        // given
        LineCreateRequest lineCreateRequest = createRequest("신분당선", "bg-red-600");
        SimpleResponse createdResponse = SimpleRestAssured.post("/lines", lineCreateRequest);
        // when
        LineCreateRequest modificationParam = createRequest("구분당선", "bg-red-600");
        String uri = createdResponse.getHeader("Location");
        SimpleResponse modifiedResponse = SimpleRestAssured.put(uri, modificationParam);
        // then
        modifiedResponse.assertStatus(HttpStatus.OK);
    }

    @Test
    @DisplayName("존재하지 않는 ID값의 노선을 수정할 수 없다.")
    public void modifyLine_throwExceptionWithInvalidId() {
        // given
        LineCreateRequest lineCreateRequest = createRequest("신분당선", "bg-red-600");
        SimpleRestAssured.post("/lines", lineCreateRequest);
        // when
        LineCreateRequest modificationParam = createRequest("구분당선", "bg-red-600");
        SimpleResponse response = SimpleRestAssured.put("/lines/99", modificationParam);
        // then
        Assertions.assertAll(
                () -> response.assertStatus(HttpStatus.BAD_REQUEST),
                () -> assertThat(response.containsExceptionMessage("존재하지 않습니다")).isTrue()
        );
    }

    @Test
    @DisplayName("ID값으로 노선을 제거한다.")
    public void deleteLine() {
        // given
        LineCreateRequest lineCreateRequest = createRequest("신분당선", "bg-red-600");
        SimpleResponse createdResponse = SimpleRestAssured.post("/lines", lineCreateRequest);

        // when
        String uri = createdResponse.getHeader("Location");
        SimpleResponse deleteResponse = SimpleRestAssured.delete(uri);
        // then
        deleteResponse.assertStatus(HttpStatus.NO_CONTENT);
    }

    @Test
    @DisplayName("존재하지 않는 ID값의 노선을 제거할 수 없다.")
    public void deleteLine_throwExceptionWithInvalidId() {
        // given
        LineCreateRequest lineCreateRequest = createRequest("신분당선", "bg-red-600");
        SimpleRestAssured.post("/lines", lineCreateRequest);
        // when
        SimpleResponse response = SimpleRestAssured.delete("/lines/99");
        // then
        Assertions.assertAll(
                () -> response.assertStatus(HttpStatus.BAD_REQUEST),
                () -> assertThat(response.containsExceptionMessage("존재하지 않습니다")).isTrue()
        );
    }

    private LineCreateRequest createRequest(String name, String color) {
        return new LineCreateRequest(name, color, 1L, 2L, 10, 0);
    }
}
