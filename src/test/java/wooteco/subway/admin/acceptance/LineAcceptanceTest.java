package wooteco.subway.admin.acceptance;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.admin.dto.LineDetailResponse;
import wooteco.subway.admin.dto.LineResponse;
import wooteco.subway.admin.dto.StationResponse;

import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

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

    @DisplayName("지하철 노선도 정보 조회를 한다.")
    @Test
    void retrieveLines() {
        // Given 지하철 역이 여러개 추가되어있다
        StationResponse gStation = createStation("강남역");
        StationResponse jStation = createStation("잠실역");
        StationResponse hStation = createStation("화정역");
        StationResponse sStation = createStation("삼송역");

        // And 지하철 노선이 여러 개 추가되어있다
        LineResponse line1 = createLine("1호선");
        LineResponse line2 = createLine("2호선");

        // And 지하철 노선에 지하철역이 여러 개 추가되어있다.
        addLineStation(line1.getId(), null, gStation.getId());
        addLineStation(line1.getId(), gStation.getId(), jStation.getId());
        addLineStation(line2.getId(), null, hStation.getId());
        addLineStation(line2.getId(), hStation.getId(), sStation.getId());
        addLineStation(line2.getId(), sStation.getId(), jStation.getId());

        // When 지하철 노선도 전체 조회 요청을 한다
        List<LineDetailResponse> lines = getDetailLines();

        // Then 지하철 노선도 전체를 응답 받는다
        assertThat(lines.size()).isEqualTo(2);
        assertThat(lines.get(0).getStations().size()).isEqualTo(2);
        assertThat(lines.get(1).getStations().size()).isEqualTo(3);
    }
}
