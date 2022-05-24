package wooteco.subway.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import wooteco.subway.dao.station.StationDao;
import wooteco.subway.domain.Station;
import wooteco.subway.exception.NotFoundException;

@SpringBootTest
@Sql({"classpath:schema-truncate.sql", "classpath:init.sql"})
class StationServiceTest {

    @Autowired
    private StationDao stationDao;

    @Autowired
    private StationService stationService;

    @Test
    @DisplayName("이미 존재하는 역 이름이 있을 때 예외가 발생한다.")
    void saveExceptionByDuplicatedName() {
        stationDao.save(new Station("오리"));
        assertThatThrownBy(() -> stationService.save(new Station("오리")))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("이미 존재하는 역 이름입니다.");
    }

    @Test
    @DisplayName("존재하지 않는 id로 delete하려할 경우 예외가 발생한다.")
    void deleteExceptionByNotFoundLine() {
        assertThatThrownBy(() -> stationService.delete(1L))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("존재하지 않는 Station입니다.");
    }
}
