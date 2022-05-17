package wooteco.subway.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static wooteco.subway.TestFixtures.LINE_COLOR;
import static wooteco.subway.TestFixtures.LINE_SIX;
import static wooteco.subway.TestFixtures.STANDARD_DISTANCE;
import static wooteco.subway.TestFixtures.동묘앞역;
import static wooteco.subway.TestFixtures.보문역;
import static wooteco.subway.TestFixtures.신당역;
import static wooteco.subway.TestFixtures.창신역;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import wooteco.subway.domain.Line;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Station;
import wooteco.subway.dto.PathResponse;
import wooteco.subway.dto.StationResponse;
import wooteco.subway.repository.LineRepository;
import wooteco.subway.repository.SectionRepository;
import wooteco.subway.repository.StationRepository;

class PathAcceptanceTest extends AcceptanceTest {

    @Autowired
    private LineRepository lineRepository;
    @Autowired
    private SectionRepository sectionRepository;
    @Autowired
    private StationRepository stationRepository;

    @DisplayName("경로 조회를 요청하면, 200 OK 와 관련 지하철역 정보, 거리, 요금을 반환한다.")
    @Test
    void getPaths() {
        Station saved_신당역 = stationRepository.save(신당역);
        Station saved_동묘앞역 = stationRepository.save(동묘앞역);
        Station saved_창신역 = stationRepository.save(창신역);

        Long lineId = lineRepository.save(new Line(LINE_SIX, LINE_COLOR));

        sectionRepository.save(new Section(lineId, saved_신당역, saved_동묘앞역, STANDARD_DISTANCE));
        sectionRepository.save(new Section(lineId, saved_동묘앞역, saved_창신역, STANDARD_DISTANCE));

        ExtractableResponse<Response> response = httpGetTest(
                "/paths?source=" + saved_신당역.getId() + "&target=" + saved_창신역.getId() + "&age=15");

        PathResponse pathResponse = response.jsonPath()
                .getObject(".", PathResponse.class);

        List<StationResponse> stations = pathResponse.getStations();
        assertAll(() -> {
            assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
            assertThat(stations).hasSize(3);
            assertThat(stations).containsExactly(new StationResponse(saved_신당역),
                    new StationResponse(saved_동묘앞역),
                    new StationResponse(saved_창신역));
            assertThat(pathResponse.getDistance()).isEqualTo(STANDARD_DISTANCE + STANDARD_DISTANCE);
            assertThat(pathResponse.getFare()).isEqualTo(1450);
        });
    }

    @DisplayName("경로 조회 시, 연결된 구간을 찾을 수 없으면 404 Not Found 에러를 발생한다.")
    @Test
    void getPathsException() {
        Station saved_신당역 = stationRepository.save(신당역);
        Station saved_동묘앞역 = stationRepository.save(동묘앞역);
        Station saved_창신역 = stationRepository.save(창신역);
        Station saved_보문역 = stationRepository.save(보문역);

        Long lineId = lineRepository.save(new Line(LINE_SIX, LINE_COLOR));

        sectionRepository.save(new Section(lineId, saved_신당역, saved_동묘앞역, STANDARD_DISTANCE));
        sectionRepository.save(new Section(lineId, saved_보문역, saved_창신역, STANDARD_DISTANCE));

        ExtractableResponse<Response> response = httpGetTest(
                "/paths?source=" + saved_신당역.getId() + "&target=" + saved_창신역.getId() + "&age=15");

        assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }
}
