package wooteco.subway.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.domain.Line;
import wooteco.subway.repository.dao.StationDao;
import wooteco.subway.repository.entity.StationEntity;
import wooteco.subway.service.dto.LineRequest;
import wooteco.subway.service.dto.LineUpdateRequest;

@SpringBootTest
@Transactional
class LineServiceTest {

    @Autowired
    private LineService lineService;

    @Autowired
    private StationDao stationDao;

    private StationEntity gangnam;
    private StationEntity nowon;

    @BeforeEach
    void setUp() {
        gangnam = stationDao.save(new StationEntity(null, "강남역"));
        nowon = stationDao.save(new StationEntity(null, "노원역"));
    }

    @Test
    @DisplayName("노선을 생성한다.")
    void createLine() {
        // given
        final LineRequest request = new LineRequest("7호선", "bg-red-600", gangnam.getId(), nowon.getId(), 10);

        // when
        Line save = lineService.save(request);

        // then
        assertThat(save.getName()).isEqualTo(request.getName());
    }

    @Test
    @DisplayName("추가 요금을 가진 노선을 생성한다")
    void createLine_extraFare() {
        // given
        long extraFare = 100L;
        final LineRequest request = new LineRequest("7호선", "bg-red-600", gangnam.getId(), nowon.getId(), 10, extraFare);

        // when
        Line line = lineService.save(request);

        // then
        assertThat(line.getExtraFare()).isEqualTo(extraFare);
    }

    @Test
    @DisplayName("모든 노선을 조회한다.")
    void showLines() {
        // given
        lineService.save(new LineRequest("1호선", "bg-red-600", gangnam.getId(), nowon.getId(), 10));
        lineService.save(new LineRequest("2호선", "bg-blue-600", nowon.getId(), gangnam.getId(), 10));

        // when
        List<Line> lines = lineService.findAll();

        // then
        assertThat(lines).hasSize(2);
    }

    @Test
    @DisplayName("id에 해당하는 노선을 조회한다.")
    void findById() {
        // given
        String color = "bg-red-600";
        String name = "7호선";

        Line savedLine = lineService.save(new LineRequest(name, color, gangnam.getId(), nowon.getId(), 10));

        // when
        Line response = lineService.findById(savedLine.getId());

        // then
        assertThat(response.getName()).isEqualTo(name);
        assertThat(response.getColor()).isEqualTo(color);
    }

    @Test
    @DisplayName("id에 해당하는 노선 정보를 수정한다.")
    void updateById() {
        // given
        Line saved = lineService.save(new LineRequest("1호선", "red", gangnam.getId(), nowon.getId(), 10));

        final String name = "7호선";
        final String color = "bg-blue-600";
        final LineUpdateRequest request = new LineUpdateRequest(name, color, 200L);

        // when
        lineService.updateById(saved.getId(), request);

        // then
        Line updated = lineService.findById(saved.getId());
        assertThat(updated.getName()).isEqualTo(name);
        assertThat(updated.getColor()).isEqualTo(color);
    }

    @Test
    @DisplayName("id에 해당하는 노선을 삭제한다.")
    void deleteById() {
        // given
        Line saved = lineService.save(new LineRequest("1호선", "red", gangnam.getId(), nowon.getId(), 10));

        // when
        lineService.deleteById(saved.getId());

        // then
        List<Line> all = lineService.findAll();
        assertThat(all).hasSize(0);
    }
}
