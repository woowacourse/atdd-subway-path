package wooteco.subway.admin.acceptance;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalTime;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import wooteco.subway.admin.dto.LineDetailResponse;
import wooteco.subway.admin.dto.LineResponse;
import wooteco.subway.admin.dto.StationResponse;

public class LineAcceptanceTest extends AcceptanceTest {
    @DisplayName("지하철 노선을 관리한다")
    @Test
    void manageLine() {
        // when
        createLine(LINE_NAME_SINBUNDANG);
        createLine(LINE_NAME_BUNDANG);
        createLine(LINE_NAME_2);
        createLine(LINE_NAME_3);
        // then
        List<LineResponse> lines = getLines();
        assertThat(lines.size()).isEqualTo(4);

        // when
        LineDetailResponse line = getLine(lines.get(0).getId());
        // then
        assertThat(line.getId()).isNotNull();
        assertThat(line.getName()).isNotNull();
        assertThat(line.getStartTime()).isNotNull();
        assertThat(line.getEndTime()).isNotNull();
        assertThat(line.getIntervalTime()).isNotNull();

        // when
        LocalTime startTime = LocalTime.of(8, 00);
        LocalTime endTime = LocalTime.of(22, 00);
        updateLine(line.getId(), startTime, endTime);
        //then
        LineDetailResponse updatedLine = getLine(line.getId());
        assertThat(updatedLine.getStartTime()).isEqualTo(startTime);
        assertThat(updatedLine.getEndTime()).isEqualTo(endTime);

        // when
        deleteLine(line.getId());
        // then
        List<LineResponse> linesAfterDelete = getLines();
        assertThat(linesAfterDelete.size()).isEqualTo(3);
    }

    @DisplayName("전체 지하철 노선도 정보 조회")
    @Test
    void wholeSubway() {
        // Given 지하철역이 여러 개 추가되어있다.
        StationResponse station1 = createStation("1역");
        StationResponse station2 = createStation("2역");
        StationResponse station3 = createStation("3역");
        StationResponse station4 = createStation("4역");
        StationResponse station5 = createStation("5역");
        StationResponse station6 = createStation("6역");

        // And 지하철 노선이 여러 개 추가되어있다.
        LineResponse line1 = createLine("1호선");
        LineResponse line2 = createLine("2호선");

        // And 지하철 노선에 지하철역이 여러 개 추가되어있다.
        addLineStation(line1.getId(), null, station1.getId());
        addLineStation(line1.getId(), station1.getId(), station2.getId());
        addLineStation(line1.getId(), station2.getId(), station3.getId());
        addLineStation(line2.getId(), null, station4.getId());
        addLineStation(line2.getId(), station4.getId(), station5.getId());
        addLineStation(line2.getId(), station5.getId(), station6.getId());

        // When 지하철 노선도 전체 조회 요청을 한다.
        List<LineDetailResponse> response = retrieveWholeSubway().getLineDetailResponses();

        // Then 지하철 노선도 전체를 응답 받는다.
        assertThat(response).hasSize(2);
        assertThat(response.get(0).getStations()).hasSize(3);
        assertThat(response.get(1).getStations()).hasSize(3);
    }
}
