package wooteco.subway.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import wooteco.subway.dto.LineRequest;
import wooteco.subway.dto.LineResponse;
import wooteco.subway.exception.datanotfound.LineNotFoundException;
import wooteco.subway.exception.datanotfound.StationNotFoundException;
import wooteco.subway.exception.duplicatename.LineDuplicateException;

@SpringBootTest
@Sql("classpath:setupSchema.sql")
class LineServiceTest {

    private final static int DISTANCE = 10;
    private final static int EXTRA_FARE = 0;

    @Autowired
    private LineService lineService;

    @DisplayName("노선, 구간 정보를 저장한다.")
    @Test
    void save() {
        LineRequest line = new LineRequest("4호선", "green", 1L, 2L, DISTANCE, EXTRA_FARE);
        LineResponse newLine = lineService.save(line);

        assertAll(() -> assertThat(newLine.getName()).isEqualTo("4호선"),
                () -> assertThat(newLine.getColor()).isEqualTo("green"),
                () -> assertThat(newLine.getExtraFare()).isEqualTo(0),
                () -> assertThat(newLine.getStations().size()).isEqualTo(2));
    }

    @DisplayName("노선, 구간 정보를 저장할때, 노선과 구간 정보에 존재하지 않는 지하철역 정보가 있으면 예외를 발생시킨다.")
    @Test
    void save_no_data_exception() {
        LineRequest line = new LineRequest("4호선", "green", 1L, 7L, DISTANCE, EXTRA_FARE);

        assertThatThrownBy(() -> lineService.save(line))
                .isInstanceOf(StationNotFoundException.class)
                .hasMessageContaining("존재하지 않는 역입니다.");
    }

    @DisplayName("중복된 노선 저장시 예외를 발생시킨다.")
    @Test
    void duplicateLine() {
        LineRequest line = new LineRequest("3호선", "green", 1L, 2L, DISTANCE, EXTRA_FARE);
        LineRequest duplicateLine = new LineRequest("3호선", "red", 1L, 2L, DISTANCE, EXTRA_FARE);
        lineService.save(line);

        assertThatThrownBy(() -> lineService.save(duplicateLine))
                .isInstanceOf(LineDuplicateException.class)
                .hasMessageContaining("이미 등록된 지하철 노선이름 입니다.");
    }

    @DisplayName("노선 정보 전체를 조회한다.")
    @Test
    void findAll() {
        LineRequest firstLine = new LineRequest("5호선", "green", 1L, 2L, DISTANCE, EXTRA_FARE);
        LineRequest secondLine = new LineRequest("7호선", "red", 1L, 3L, DISTANCE, EXTRA_FARE);
        lineService.save(firstLine);
        lineService.save(secondLine);

        List<LineResponse> lines = lineService.findLines();

        assertThat(lines.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("노선 정보 조회")
    void find() {
        LineRequest line = new LineRequest("4호선", "red", 1L, 2L, DISTANCE, EXTRA_FARE);
        LineResponse newLine = lineService.save(line);

        assertThat(lineService.findLine(newLine.getId()).getName()).isEqualTo(line.getName());
        assertThat(lineService.findLine(newLine.getId()).getColor()).isEqualTo(line.getColor());
    }

    @Test
    @DisplayName("존재하지 않는 노선정보 조회시 예외를 발생한다.")
    void findNotExistLine() {
        assertThatThrownBy(() -> lineService.findLine(1L))
                .isInstanceOf(LineNotFoundException.class)
                .hasMessageContaining("존재하지 않는 노선입니다.");
    }

    @Test
    @DisplayName("노선 정보 삭제")
    void delete() {
        LineRequest line = new LineRequest("4호선", "red", 1L, 2L, DISTANCE, EXTRA_FARE);
        LineResponse newLine = lineService.save(line);

        assertThat(lineService.deleteLine(newLine.getId())).isEqualTo(1);
    }

    @Test
    @DisplayName("존재하지 않는 노선정보 삭제시 예외를 발생한다.")
    void deleteNotExistLine() {
        assertThatThrownBy(() -> lineService.deleteLine(1L))
                .isInstanceOf(LineNotFoundException.class)
                .hasMessageContaining("존재하지 않는 노선입니다.");
    }

    @Test
    @DisplayName("노선 정보 업데이트")
    void update() {
        LineRequest line = new LineRequest("11호선", "red", 1L, 2L, DISTANCE, EXTRA_FARE);
        LineRequest lineForUpdate = new LineRequest("신분당선", "red", 1L, 3L, DISTANCE, EXTRA_FARE);

        LineResponse newLine = lineService.save(line);

        assertThat(lineService.updateLine(newLine.getId(), lineForUpdate)).isEqualTo(1);
    }

    @Test
    @DisplayName("중복되는 이름으로 노선이름 업데이트시 예외를 발생한다.")
    void updateNotExistLine() {
        LineRequest line = new LineRequest("11호선", "red", 1L, 2L, DISTANCE, EXTRA_FARE);
        LineResponse newLine = lineService.save(line);

        assertThatThrownBy(() -> lineService.updateLine(newLine.getId(), line))
                .isInstanceOf(LineDuplicateException.class)
                .hasMessageContaining("이미 등록된 지하철 노선이름 입니다.");
    }

    @Test
    @DisplayName("존재하지 않는 노선정보 업데이트시 예외를 발생한다.")
    void updateDuplicateNameLine() {
        LineRequest line = new LineRequest("11호선", "red", 1L, 2L, DISTANCE, EXTRA_FARE);
        LineRequest lineForUpdate = new LineRequest("12호선", "red", 1L, 2L, DISTANCE, EXTRA_FARE);

        lineService.save(line);

        assertThatThrownBy(() -> lineService.updateLine(12L, lineForUpdate))
                .isInstanceOf(LineNotFoundException.class)
                .hasMessageContaining("존재하지 않는 노선입니다.");
    }
}
