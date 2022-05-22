package wooteco.subway.service;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import wooteco.subway.dto.request.LineRequest;
import wooteco.subway.dto.request.StationRequest;
import wooteco.subway.dto.response.LineResponse;
import wooteco.subway.dto.response.StationResponse;

@SpringBootTest
@Sql("classpath:schema.sql")
class LineServiceTest {

    @Autowired
    private LineService lineService;

    @Autowired
    private StationService stationService;

    private Long lineId;

    @Test
    @DisplayName("노선을 저장한다.")
    public void saveLine() {
        // given
        setUpLine();

        LineRequest lineRequest = new LineRequest("노오서언", "새액까알", 1L, 2L, 10, 0);
        // when
        final LineResponse save = lineService.save(lineRequest);

        // then
        assertThat(save.getId()).isNotNull();
    }

    @Test
    @DisplayName("모든 노선을 찾는다.")
    public void findAllLines() {
        // given
        setUpLine();

        // when
        final List<LineResponse> foundLines = lineService.findAll();
        // then
        assertThat(foundLines).hasSize(1);
    }

    @Test
    @DisplayName("노선 하나를 찾는다.")
    public void findOneLine() {
        // given
        setUpLine();

        // when
        final LineResponse foundLine = lineService.findOne(lineId);
        // then
        assertThat(foundLine.getName()).isEqualTo("분당");
    }

    @Test
    @DisplayName("노선을 수정한다.")
    public void updateLine() {
        // given
        setUpLine();
        // when
        lineService.update(lineId, new LineRequest("새분당", "새색", 1L, 2L, 10, 500));
        // then
        assertThat(lineService.findOne(lineId).getName()).isEqualTo("새분당");
    }

    @Test
    @DisplayName("노선을 삭제한다")
    public void deleteLine() {
        // given
        setUpLine();
        // when
        lineService.delete(lineId);
        // then
        assertThat(lineService.findAll()).hasSize(0);
    }

    private void setUpLine() {
        final StationResponse stationA = stationService.save(new StationRequest("역1"));
        final StationResponse stationB = stationService.save(new StationRequest("역2"));

        final LineResponse save = lineService.save(new LineRequest("분당", "색", stationA.getId(), stationB.getId(), 10, 0));
        lineId = save.getId();
    }
}