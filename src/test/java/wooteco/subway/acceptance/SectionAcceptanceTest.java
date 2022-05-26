package wooteco.subway.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static wooteco.subway.acceptance.fixture.HttpRequestUtil.delete;
import static wooteco.subway.acceptance.fixture.HttpRequestUtil.post;
import static wooteco.subway.acceptance.fixture.TestLine.LINE_2;
import static wooteco.subway.acceptance.fixture.TestStation.GANGNAM;
import static wooteco.subway.acceptance.fixture.TestStation.SEOLLEUNG;
import static wooteco.subway.acceptance.fixture.TestStation.YEOKSAM;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import wooteco.subway.domain.Line;
import wooteco.subway.domain.Station;
import wooteco.subway.dto.request.SectionRequest;

public class SectionAcceptanceTest extends AcceptanceTest {

    @DisplayName("등록된 노선에 추가 구간 생성 요청을 하면 200 OK를 반환한다.")
    @Test
    void createSection() {
        Station gangnam = GANGNAM.save();
        Station yeoksam = YEOKSAM.save();
        Station seolleung = SEOLLEUNG.save();
        Line line2 = LINE_2.save(gangnam, yeoksam, 1);
        SectionRequest sectionRequest = new SectionRequest(yeoksam.getId(), seolleung.getId(), 1);

        ExtractableResponse<Response> response = post(sectionRequest, "/lines/" + line2.getId() + "/sections");

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @DisplayName("시작 역과 도착 역이 같도록 구간 생성 요청을 하면 400 BAD REQUEST를 반환한다.")
    @Test
    void createSectionWithSameStation() {
        Station gangnam = GANGNAM.save();
        Station yeoksam = YEOKSAM.save();
        Station seolleung = SEOLLEUNG.save();
        Line line2 = LINE_2.save(gangnam, yeoksam, 1);
        SectionRequest sectionRequest = new SectionRequest(seolleung.getId(), seolleung.getId(), 1);

        ExtractableResponse<Response> response = post(sectionRequest, "/lines/" + line2.getId() + "/sections");

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.body().asString()).isEqualTo("구간의 시작과 끝은 같은 역일 수 없습니다.");
    }

    @DisplayName("등록되지 않은 노선으로 추가 구간 생성 요청을 하면 404 NOT FOUND를 반환한다.")
    @Test
    void createSectionWithNotFoundLine() {
        Station gangnam = GANGNAM.save();
        Station yeoksam = YEOKSAM.save();
        SectionRequest sectionRequest = new SectionRequest(gangnam.getId(), yeoksam.getId(), 1);

        ExtractableResponse<Response> response = post(sectionRequest, "/lines/" + Long.MAX_VALUE + "/sections");

        assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @DisplayName("등록되지 않은 역으로 추가 구간 생성 요청을 하면 404 NOT FOUND를 반환한다.")
    @Test
    void createSectionWithNotFoundStation() {
        Station gangnam = GANGNAM.save();
        Station yeoksam = YEOKSAM.save();
        Line line2 = LINE_2.save(gangnam, yeoksam, 1);
        SectionRequest sectionRequest = new SectionRequest(yeoksam.getId(), yeoksam.getId() + 1, 1);

        ExtractableResponse<Response> response = post(sectionRequest, "/lines/" + line2.getId() + "/sections");

        assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @DisplayName("기존에 존재하는 구간 생성 요청을 하면 400 BAD REQUEST를 반환한다.")
    @Test
    void createDuplicateSection() {
        Station gangnam = GANGNAM.save();
        Station yeoksam = YEOKSAM.save();
        Line line2 = LINE_2.save(gangnam, yeoksam, 1);
        SectionRequest sectionRequest = new SectionRequest(gangnam.getId(), yeoksam.getId(), 1);

        ExtractableResponse<Response> response = post(sectionRequest, "/lines/" + line2.getId() + "/sections");

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.body().asString()).isEqualTo("이미 존재하는 구간입니다.");
    }

    @DisplayName("지하철 역 id로 null을 넣어서 생성 요청을 하면 400 BAD REQUEST를 반환한다.")
    @Test
    void notAllowNullStationId() {
        Station gangnam = GANGNAM.save();
        Station yeoksam = YEOKSAM.save();
        Line line2 = LINE_2.save(gangnam, yeoksam, 1);
        SectionRequest sectionRequest = new SectionRequest(null, null, 1);

        ExtractableResponse<Response> response = post(sectionRequest, "/lines/" + line2.getId() + "/sections");

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.body().asString()).contains("id는 null일 수 없습니다.");
    }

    @DisplayName("지하철 역 id로 1보다 작은 값을 넣어서 생성 요청을 하면 400 BAD REQUEST를 반환한다.")
    @Test
    void notAllowStationIdLessThan1() {
        Station gangnam = GANGNAM.save();
        Station yeoksam = YEOKSAM.save();
        Line line2 = LINE_2.save(gangnam, yeoksam, 1);
        SectionRequest sectionRequest = new SectionRequest(0L, 0L, 1);

        ExtractableResponse<Response> response = post(sectionRequest, "/lines/" + line2.getId() + "/sections");

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.body().asString()).contains("id는 1보다 작을 수 없습니다.");
    }

    @DisplayName("구간 거리로 1보다 작은 값을 넣어서 생성 요청을 하면 400 BAD REQUEST를 반환한다.")
    @Test
    void notAllowDistanceLessThan1() {
        Station gangnam = GANGNAM.save();
        Station yeoksam = YEOKSAM.save();
        Station seolleung = SEOLLEUNG.save();
        Line line2 = LINE_2.save(gangnam, yeoksam, 1);
        SectionRequest sectionRequest = new SectionRequest(yeoksam.getId(), seolleung.getId(), 0);

        ExtractableResponse<Response> response = post(sectionRequest, "/lines/" + line2.getId() + "/sections");

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.body().asString()).isEqualTo("구간 거리는 1보다 작을 수 없습니다.");
    }

    @DisplayName("등록된 구간을 포함하고 있는 노선과 역의 id를 URI에 담아서 구간 삭제 요청을 하면 404 NOT FOUND를 반환한다.")
    @Test
    void deleteSection() {
        Station gangnam = GANGNAM.save();
        Station yeoksam = YEOKSAM.save();
        Line line2 = LINE_2.save(gangnam, yeoksam, 1);

        ExtractableResponse<Response> response = delete(
                "/lines/" + line2.getId() + "/sections?stationId=" + gangnam.getId());

        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @DisplayName("존재하지 않는 노선의 id를 URI에 담아서 구간 삭제 요청을 하면 404 NOT FOUND를 반환한다.")
    @Test
    void deleteSectionWithNotFoundLine() {
        Station gangnam = GANGNAM.save();

        ExtractableResponse<Response> response = delete(
                "/lines/" + Long.MAX_VALUE + "/sections?stationId=" + gangnam.getId());

        assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @DisplayName("존재하지 않는 구간 삭제 요청을 하면 404 NOT FOUND를 반환한다.")
    @Test
    void deleteNotFoundSection() {
        Station gangnam = GANGNAM.save();
        Station yeoksam = YEOKSAM.save();
        Station seolleung = SEOLLEUNG.save();
        Line line2 = LINE_2.save(gangnam, yeoksam, 1);

        ExtractableResponse<Response> response = delete(
                "/lines/" + line2.getId() + "/sections?stationId=" + seolleung.getId());

        assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

}
