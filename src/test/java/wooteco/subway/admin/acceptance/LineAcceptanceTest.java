package wooteco.subway.admin.acceptance;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.admin.dto.response.LineDetailResponse;
import wooteco.subway.admin.dto.response.LineResponse;

import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class LineAcceptanceTest extends AcceptanceTest {
    @DisplayName("지하철 노선을 관리한다")
    @Test
    void manageLine() {
        // when : 노선이 추가된다.
        createLine(LINE_NAME_SINBUNDANG);
        createLine(LINE_NAME_BUNDANG);
        createLine(LINE_NAME_2);
        createLine(LINE_NAME_3);
        // then : 추가된 만큼의 노선이 저장되었는지 확인한다.
        List<LineResponse> lines = getLines();
        assertThat(lines.size()).isEqualTo(4);

        // when : 한 노선을 조회한다.
        LineDetailResponse line = getLine(lines.get(0).getId());
        // then : 조회한 노선의 정보를 확인한다.
        assertThat(line.getId()).isNotNull();
        assertThat(line.getName()).isNotNull();
        assertThat(line.getStartTime()).isNotNull();
        assertThat(line.getEndTime()).isNotNull();
        assertThat(line.getIntervalTime()).isNotNull();

        // when : 노선의 정보를 갱신한다.
        LocalTime startTime = LocalTime.of(8, 00);
        LocalTime endTime = LocalTime.of(22, 00);
        updateLine(line.getId(), startTime, endTime);
        //then : 갱신한 노선의 정보를 확인한다.
        LineDetailResponse updatedLine = getLine(line.getId());
        assertThat(updatedLine.getStartTime()).isEqualTo(startTime);
        assertThat(updatedLine.getEndTime()).isEqualTo(endTime);

        // when : 노선을 삭제한다.
        deleteLine(line.getId());
        // then : 노선이 삭제되었는지 확인한다.
        List<LineResponse> linesAfterDelete = getLines();
        assertThat(linesAfterDelete.size()).isEqualTo(3);
    }
}
