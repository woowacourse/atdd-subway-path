package wooteco.subway.ui;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.exception.DomainException;
import wooteco.subway.exception.NotFoundException;
import wooteco.subway.repository.dao.LineDao;
import wooteco.subway.repository.dao.StationDao;
import wooteco.subway.repository.entity.LineEntity;
import wooteco.subway.repository.entity.StationEntity;
import wooteco.subway.service.SectionService;
import wooteco.subway.service.dto.SectionRequest;
import wooteco.subway.service.dto.SectionSaveRequest;

@SpringBootTest
@Transactional
class SectionControllerTest {

    @Autowired
    SectionController sectionController;

    @Autowired
    LineDao lineDao;

    @Autowired
    StationDao stationDao;

    @Autowired
    SectionService sectionService;

    LineEntity 칠호선;
    StationEntity 상계역;
    StationEntity 중계역;
    StationEntity 하계역;

    @BeforeEach
    void setUp() {
        칠호선 = lineDao.save(new LineEntity(null, "7호선", "red", 100L));
        상계역 = stationDao.save(new StationEntity(null, "상계역"));
        중계역 = stationDao.save(new StationEntity(null, "중계역"));
        하계역 = stationDao.save(new StationEntity(null, "하계역"));
    }

    @Test
    @DisplayName("구간을 저장한다.")
    void save() {
        assertThatCode(() -> sectionController.save(칠호선.getId(), new SectionRequest(상계역.getId(), 중계역.getId(), 10)))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("거리가 0인 구간은 저장하려는 경우 예외.")
    void save_invalidDistance() {
        assertThatThrownBy(() -> sectionController.save(칠호선.getId(), new SectionRequest(상계역.getId(), 중계역.getId(), 0)))
                .isInstanceOf(DomainException.class);
    }

    @Test
    @DisplayName("없는 구간 id로 구간 저장하려 하면 예외.")
    void save_invalidLine() {
        assertThatThrownBy(
                () -> sectionController.save(칠호선.getId() + 1, new SectionRequest(상계역.getId(), 중계역.getId(), 10)))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    @DisplayName("구간을 삭제한다.")
    void delete() {
        // given
        sectionService.save(new SectionSaveRequest(칠호선.getId(), 상계역.getId(), 중계역.getId(), 10));
        sectionService.save(new SectionSaveRequest(칠호선.getId(), 중계역.getId(), 하계역.getId(), 10));

        // then
        assertThatCode(() -> sectionController.delete(칠호선.getId(), 중계역.getId()))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("구간이 하나인데 노선을 삭제하려면 예외")
    void delete_onlyOneSection() {
        // given
        sectionService.save(new SectionSaveRequest(칠호선.getId(), 상계역.getId(), 중계역.getId(), 10));

        // then
        assertThatThrownBy(() -> sectionController.delete(칠호선.getId(), 중계역.getId()))
                .isInstanceOf(DomainException.class);
    }

    @Test
    @DisplayName("구간에 존재하지 않은 역을 통해 삭제하려면 예외")
    void delete_notFoundStation() {
        // given
        sectionService.save(new SectionSaveRequest(칠호선.getId(), 상계역.getId(), 중계역.getId(), 10));

        // then
        assertThatThrownBy(() -> sectionController.delete(칠호선.getId(), 하계역.getId()))
                .isInstanceOf(DomainException.class);
    }
}
