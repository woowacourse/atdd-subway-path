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

    @DisplayName("등록된 구간을 포함하고 있는 노선과 역의 id를 URI에 담아서 구간 삭제 요청을 하면 204 NO CONTENT를 반환한다.")
    @Test
    void deleteSection() {
        Station gangnam = GANGNAM.save();
        Station yeoksam = YEOKSAM.save();
        Line line2 = LINE_2.save(gangnam, yeoksam, 1);

        ExtractableResponse<Response> response = delete(
                "/lines/" + line2.getId() + "/sections?stationId=" + gangnam.getId());

        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

}
