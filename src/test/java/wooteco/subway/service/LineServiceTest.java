package wooteco.subway.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import wooteco.subway.dao.line.LineDao;
import wooteco.subway.dao.station.StationDao;
import wooteco.subway.domain.Line;
import wooteco.subway.domain.Station;
import wooteco.subway.dto.line.LineSaveRequest;
import wooteco.subway.dto.line.LineUpdateRequest;
import wooteco.subway.exception.NotFoundException;

@SpringBootTest
@Sql({"classpath:schema-truncate.sql", "classpath:init.sql"})
class LineServiceTest {

    @Autowired
    private LineDao lineDao;

    @Autowired
    private StationDao stationDao;

    @Autowired
    private LineService lineService;

    @Test
    @DisplayName("이미 존재하는 노선의 이름이 있을 때 예외가 발생한다.")
    void saveExceptionByExistName() {
        lineDao.save(new Line("신분당선", "bg-red-600"));
        assertThatThrownBy(() -> lineService.save(new LineSaveRequest("신분당선", "bg-green-600", 1L, 2L, 2)))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("이미 존재하는 노선 이름입니다.");
    }

    @Test
    @DisplayName("정상적으로 원하는 LineRequest를 저장하여 반환할 수 있다.")
    void save() {
        Station upStation = stationDao.findById(stationDao.save(new Station("오리")));
        Station downStation = stationDao.findById(stationDao.save(new Station("배카라")));
        LineSaveRequest lineSaveRequest = new LineSaveRequest("신분당선", "bg-red-600", upStation.getId(), downStation.getId(), 1);

        assertThat(lineService.save(lineSaveRequest)).isNotNull();
    }

    @Test
    @DisplayName("존재하지 않는 id로 update하려할 경우 예외가 발생한다.")
    void updateExceptionByNotFoundLine() {
        assertThatThrownBy(() -> lineService.update(1L, new LineUpdateRequest("신분당선", "bg-green-600")))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("존재하지 않는 Line입니다.");
    }

    @Test
    @DisplayName("존재하지 않는 id로 delete하려할 경우 예외가 발생한다.")
    void deleteExceptionByNotFoundLine() {
        assertThatThrownBy(() -> lineService.delete(1L))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("존재하지 않는 Line입니다.");
    }
}
