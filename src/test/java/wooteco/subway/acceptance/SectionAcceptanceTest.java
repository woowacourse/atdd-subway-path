package wooteco.subway.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import wooteco.subway.dto.LineRequest;
import wooteco.subway.dto.SectionRequest;
import wooteco.subway.dto.StationRequest;

@DisplayName("구간 관련 기능")
public class SectionAcceptanceTest extends AcceptanceTest {

    @DisplayName("지하철 구간을 추가한다.")
    @Test
    void createSection() {
        // given
        StationRequest 선릉 = new StationRequest("선릉역");
        StationRequest 잠실 = new StationRequest("잠실역");
        StationRequest 동두천 = new StationRequest("동두천역");

        insert(convertRequest(선릉), "/stations");
        insert(convertRequest(잠실), "/stations");
        insert(convertRequest(동두천), "/stations");

        LineRequest 일호선 = new LineRequest("1호선", "green", 1L, 2L, 10, 0);
        insert(convertRequest(일호선), "/lines");

        SectionRequest 잠실_동두천 = new SectionRequest(2L, 3L, 10);

        // when
        ExtractableResponse<Response> response = insert(convertRequest(잠실_동두천), "/lines/1/sections");

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @DisplayName("존재하지 않는 노선의 id값을 입력한 경우 404에러를 발생시킨다.")
    @Test
    void createSectionLineNotExist() {
        // given
        StationRequest 선릉 = new StationRequest("선릉역");
        StationRequest 잠실 = new StationRequest("잠실역");
        StationRequest 동두천 = new StationRequest("동두천역");

        insert(convertRequest(선릉), "/stations");
        insert(convertRequest(잠실), "/stations");
        insert(convertRequest(동두천), "/stations");

        LineRequest 일호선 = new LineRequest("1호선", "green", 1L, 2L, 10, 0);
        insert(convertRequest(일호선), "/lines");

        SectionRequest 잠실_동두천 = new SectionRequest(2L, 3L, 10);

        //when
        ExtractableResponse<Response> response = insert(convertRequest(잠실_동두천), "/lines/2/sections");

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }


    @DisplayName("존재하지 않는 지하철역의 id값을 입력한 경우 404에러를 발생시킨다.")
    @Test
    void createSectionStationNotExist() {
        // given
        StationRequest 선릉 = new StationRequest("선릉역");
        StationRequest 잠실 = new StationRequest("잠실역");

        insert(convertRequest(선릉), "/stations");
        insert(convertRequest(잠실), "/stations");

        LineRequest 일호선 = new LineRequest("1호선", "green", 1L, 2L, 10, 0);
        insert(convertRequest(일호선), "/lines");

        SectionRequest 잠실_동두천 = new SectionRequest(2L, 3L, 10);

        //when
        ExtractableResponse<Response> response = insert(convertRequest(잠실_동두천), "/lines/2/sections");

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @DisplayName("기존에 존재하는 지하철 구간을 추가할 경우 400에러를 발생시킨다.")
    @Test
    void createSectionByStationExist() {
        // given
        StationRequest 선릉 = new StationRequest("선릉역");
        StationRequest 잠실 = new StationRequest("잠실역");

        insert(convertRequest(선릉), "/stations");
        insert(convertRequest(잠실), "/stations");

        LineRequest 일호선 = new LineRequest("1호선", "green", 1L, 2L, 10, 0);
        insert(convertRequest(일호선), "/lines");

        SectionRequest 선릉_잠실 = new SectionRequest(1L, 2L, 10);

        //when
        ExtractableResponse<Response> response = insert(convertRequest(선릉_잠실), "/lines/1/sections");

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("지하철 구간을 제거한다.")
    @Test
    void deleteSection() {
        //given
        StationRequest 선릉 = new StationRequest("선릉역");
        StationRequest 잠실 = new StationRequest("잠실역");
        StationRequest 동두천 = new StationRequest("동두천역");

        insert(convertRequest(선릉), "/stations");
        insert(convertRequest(잠실), "/stations");
        insert(convertRequest(동두천), "/stations");

        LineRequest 일호선 = new LineRequest("1호선", "green", 1L, 2L, 10, 0);
        insert(convertRequest(일호선), "/lines");

        SectionRequest 잠실_동두천 = new SectionRequest(2L, 3L, 10);
        insert(convertRequest(잠실_동두천), "/lines/1/sections");

        // when
        ExtractableResponse<Response> response = delete("/lines/1/sections", 1L);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @DisplayName("존재하지 않는 line의 id값을 입력할 경우 404에러를 발생시킨다.")
    @Test
    void deleteSectionNotExistLine() {
        //given
        StationRequest 선릉 = new StationRequest("선릉역");
        StationRequest 잠실 = new StationRequest("잠실역");
        StationRequest 동두천 = new StationRequest("동두천역");

        insert(convertRequest(선릉), "/stations");
        insert(convertRequest(잠실), "/stations");
        insert(convertRequest(동두천), "/stations");

        LineRequest 일호선 = new LineRequest("1호선", "green", 1L, 2L, 10, 0);
        insert(convertRequest(일호선), "/lines");

        SectionRequest 잠실_동두천 = new SectionRequest(2L, 3L, 10);
        insert(convertRequest(잠실_동두천), "/lines/1/sections");

        // when
        ExtractableResponse<Response> response = delete("/lines/1/sections", 5L);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @DisplayName("지하철 구간에 해당하는 역이 두개 뿐일 경우 400코드를 보낸다.")
    @Test
    void deleteSectionWhenSectionIsOnlyOne() {
        //given
        StationRequest 선릉 = new StationRequest("선릉역");
        StationRequest 잠실 = new StationRequest("잠실역");

        insert(convertRequest(선릉), "/stations");
        insert(convertRequest(잠실), "/stations");

        LineRequest 일호선 = new LineRequest("1호선", "green", 1L, 2L, 10, 0);
        insert(convertRequest(일호선), "/lines");

        // when
        ExtractableResponse<Response> response = delete("/lines/1/sections", 1L);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
}
