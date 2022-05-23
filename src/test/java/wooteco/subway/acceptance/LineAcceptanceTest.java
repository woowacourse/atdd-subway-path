package wooteco.subway.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static wooteco.subway.helper.TLine.LINE_SIX;
import static wooteco.subway.helper.TLine.LINE_TWO;
import static wooteco.subway.helper.TStation.DONGMYO;
import static wooteco.subway.helper.TStation.SINDANG;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import wooteco.subway.domain.Station;
import wooteco.subway.dto.LineResponse;
import wooteco.subway.dto.SectionRequest;

@DisplayName("노선 관련 기능")
public class LineAcceptanceTest extends AcceptanceTest {

    @DisplayName("노선을 생성하면 201 created를 반환하고 Location header에 url resource를 반환한다.")
    @Test
    void createLine() {
        Station 신당역 = SINDANG.역을등록한다();
        Station 동묘앞역 = DONGMYO.역을등록한다();

        SectionRequest 신당_동묘 = createSection(신당역, 동묘앞역);
        ExtractableResponse<Response> response = LINE_SIX.노선을등록한다(신당_동묘, HttpStatus.CREATED.value());

        assertThat(response.header("Location")).isNotBlank();
    }

    @DisplayName("기존에 존재하는 노선 이름으로 노선을 생성하면 400 bad-request가 발생한다.")
    @Test
    void createLineWithDuplicateName() {
        Station 신당역 = SINDANG.역을등록한다();
        Station 동묘앞역 = DONGMYO.역을등록한다();

        SectionRequest 신당_동묘 = createSection(신당역, 동묘앞역);
        LINE_SIX.노선을등록하고(신당_동묘)
                .중복노선을등록한다(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("전체 노선을 조회하면 200 ok와 노선 정보를 반환한다.")
    @Test
    void getLines() {
        Station 신당역 = SINDANG.역을등록한다();
        Station 동묘앞역 = DONGMYO.역을등록한다();
        SectionRequest 신당_동묘 = createSection(신당역, 동묘앞역);

        LINE_SIX.노선을등록한다(신당_동묘);
        List<LineResponse> response = LINE_TWO.노선을등록하고(신당_동묘)
                .전체노선을조회한다(HttpStatus.OK.value());

        assertThat(response).extracting("name")
                .containsExactly(LINE_SIX.getName(), LINE_TWO.getName());
    }

    @DisplayName("단건 노선을 조회하면 200 OK와 노선 정보를 반환한다")
    @Test
    void getLine() {
        Station 신당역 = SINDANG.역을등록한다();
        Station 동묘앞역 = DONGMYO.역을등록한다();
        SectionRequest 신당_동묘 = createSection(신당역, 동묘앞역);

        LineResponse response = LINE_SIX.노선을등록하고(신당_동묘)
                .단건노선을조회한다(HttpStatus.OK.value());

        assertThat(response.getStations()).extracting("id", "name")
                .containsExactly(
                        tuple(신당역.getId(), 신당역.getName()),
                        tuple(동묘앞역.getId(), 동묘앞역.getName())
                );
    }

    @DisplayName("노선을 수정하면 200 OK를 반환한다.")
    @Test
    void updateLine() {
        Station 신당역 = SINDANG.역을등록한다();
        Station 동묘앞역 = DONGMYO.역을등록한다();
        SectionRequest 신당_동묘 = createSection(신당역, 동묘앞역);

        LINE_SIX.노선을등록하고(신당_동묘)
                .정보를변경한다(LINE_TWO, HttpStatus.OK.value());
    }

    @DisplayName("노선을 제거하면 204 No Content를 반환한다.")
    @Test
    void deleteStation() {
        Station 신당역 = SINDANG.역을등록한다();
        Station 동묘앞역 = DONGMYO.역을등록한다();
        SectionRequest 신당_동묘 = createSection(신당역, 동묘앞역);

        LINE_SIX.노선을등록하고(신당_동묘)
                .노선을제거한다(HttpStatus.NO_CONTENT.value());
    }

    private SectionRequest createSection(Station upStation, Station downStation) {
        return new SectionRequest(upStation.getId(), downStation.getId(), 5);
    }
}
