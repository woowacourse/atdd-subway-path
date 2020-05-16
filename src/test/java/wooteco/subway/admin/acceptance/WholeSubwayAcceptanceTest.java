package wooteco.subway.admin.acceptance;

import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.admin.dto.LineDetailResponse;
import wooteco.subway.admin.dto.LineResponse;
import wooteco.subway.admin.dto.StationResponse;

/*
모든 지하철 노선과 각 노선에 포함된 지하철역 조회 기능 구현
인수 테스트와 단위 테스트를 작성

given: 지하철 노선이 여러 개 추가되어있다.
and: 지하철 역이 여러 개 추가되어있다.
and: 각 지하철 노선에 지하철 구간이 여러 개 추가되어있다.

when: 조회 요청을 한다.

then: 각 노선에 등록된 지하철역을 조회 응답을 받는다.
*/
public class WholeSubwayAcceptanceTest extends AcceptanceTest {

    @DisplayName("전체 지하철 노선과 그 노선의 지하철 역 조회")
    @Test
    void test() {
        // given
        LineResponse line1 = createLine(LINE_NAME_2);
        LineResponse line2 = createLine(LINE_NAME_3);

        StationResponse station1 = createStation(STATION_NAME_KANGNAM);
        StationResponse station2 = createStation(STATION_NAME_YEOKSAM);
        StationResponse station3 = createStation(STATION_NAME_SEOLLEUNG);

        addLineStation(line1.getId(), null, station1.getId());
        addLineStation(line1.getId(), station1.getId(), station2.getId());
        addLineStation(line2.getId(), null, station3.getId());

        // when
        List<LineDetailResponse> response = getDetailLines();

        // then
        Assertions.assertThat(response).hasSize(2);
        Assertions.assertThat(response.get(0).getId()).isEqualTo(line1.getId());
        Assertions.assertThat(response.get(1).getId()).isEqualTo(line2.getId());

        Assertions.assertThat(response.get(0).getStations()).hasSize(2);
        Assertions.assertThat(response.get(1).getStations()).hasSize(1);

        Assertions.assertThat(response.get(0).getStations()).contains(station1);
        Assertions.assertThat(response.get(0).getStations()).contains(station2);
        Assertions.assertThat(response.get(1).getStations()).contains(station3);
    }
}
