package wooteco.subway.admin.acceptance;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import wooteco.subway.admin.dto.LineDetailResponse;
import wooteco.subway.admin.dto.LineResponse;

import java.time.LocalTime;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

public class LineAcceptanceTest extends AcceptanceTest {
    @DisplayName("지하철 노선을 관리한다")
    @TestFactory
    Stream<DynamicTest> manageLine() {
        return Stream.of(
                dynamicTest("노선 추가", () -> {
                    //when
                    createLine(LINE_NAME_SINBUNDANG);
                    createLine(LINE_NAME_BUNDANG);
                    createLine(LINE_NAME_2);
                    createLine(LINE_NAME_3);

                    //then
                    List<LineResponse> lines = getLines();
                    assertThat(lines.size()).isEqualTo(4);

                    LineDetailResponse line = getLine(lines.get(0).getId());

                    assertThat(line.getId()).isNotNull();
                    assertThat(line.getName()).isNotNull();
                    assertThat(line.getStartTime()).isNotNull();
                    assertThat(line.getEndTime()).isNotNull();
                    assertThat(line.getIntervalTime()).isNotNull();
                }),
                dynamicTest("노선 정보 수정하기", () -> {
                    //given
                    List<LineResponse> lines = getLines();
                    LineDetailResponse line = getLine(lines.get(0).getId());

                    //when
                    LocalTime startTime = LocalTime.of(8, 00);
                    LocalTime endTime = LocalTime.of(22, 00);
                    updateLine(line.getId(), startTime, endTime);

                    //then
                    LineDetailResponse updatedLine = getLine(line.getId());
                    assertThat(updatedLine.getStartTime()).isEqualTo(startTime);
                    assertThat(updatedLine.getEndTime()).isEqualTo(endTime);
                }),
                dynamicTest("노선 삭제하기", () -> {
                    List<LineResponse> lines = getLines();
                    LineDetailResponse line = getLine(lines.get(0).getId());

                    // when
                    deleteLine(line.getId());

                    // then
                    List<LineResponse> linesAfterDelete = getLines();
                    assertThat(linesAfterDelete.size()).isEqualTo(3);
                })
        );


    }
}
