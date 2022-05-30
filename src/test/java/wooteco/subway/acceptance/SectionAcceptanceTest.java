package wooteco.subway.acceptance;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import wooteco.subway.acceptance.fixture.SimpleCreate;
import wooteco.subway.acceptance.fixture.SimpleResponse;
import wooteco.subway.acceptance.fixture.SimpleRestAssured;
import wooteco.subway.dto.request.StationRequest;
import wooteco.subway.dto.response.LineCreateResponse;
import wooteco.subway.dto.response.StationResponse;

public class SectionAcceptanceTest extends AcceptanceTest {

    @Test
    @DisplayName("구간을 생성한다.")
    public void createSection() {
        // given
        StationResponse 강남역 = SimpleCreate.createStation(new StationRequest("강남역")).toObject(StationResponse.class);
        StationResponse 역삼역 = SimpleCreate.createStation(new StationRequest("역삼역")).toObject(StationResponse.class);
        StationResponse 선릉역 = SimpleCreate.createStation(new StationRequest("선릉역")).toObject(StationResponse.class);

        SimpleCreate.createLine(강남역, 역삼역);

        SimpleResponse response = SimpleCreate.createSection(역삼역, 선릉역);
        // then
        response.assertStatus(HttpStatus.OK);
    }

    @Test
    @DisplayName("구간을 삭제한다.")
    public void deleteSection() {
        // given
        StationResponse 강남역 = SimpleCreate.createStation(new StationRequest("강남역")).toObject(StationResponse.class);
        StationResponse 역삼역 = SimpleCreate.createStation(new StationRequest("역삼역")).toObject(StationResponse.class);
        StationResponse 선릉역 = SimpleCreate.createStation(new StationRequest("선릉역")).toObject(StationResponse.class);

        LineCreateResponse line = SimpleCreate.createLine(강남역, 역삼역).toObject(LineCreateResponse.class);

        SimpleCreate.createSection(역삼역, 선릉역);
        // when
        SimpleResponse response = SimpleRestAssured.delete(
                "/lines/" + line.getId() + "/sections?stationId=" + 선릉역.getId());
        // then
        response.assertStatus(HttpStatus.OK);
    }
}
