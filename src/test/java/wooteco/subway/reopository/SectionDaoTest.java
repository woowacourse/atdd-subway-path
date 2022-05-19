package wooteco.subway.reopository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import wooteco.subway.reopository.dao.LineDao;
import wooteco.subway.reopository.dao.StationDao;
import wooteco.subway.reopository.entity.SectionEntity;
import wooteco.subway.domain.Line;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Station;
import wooteco.subway.reopository.dao.SectionDao;

@JdbcTest
@Import({SectionRepository.class, SectionDao.class, StationRepository.class, StationDao.class, LineRepository.class,
        LineDao.class})
public class SectionDaoTest {

    @Autowired
    private SectionRepository sectionRepository;
    @Autowired
    private StationRepository stationRepository;
    @Autowired
    private LineRepository lineRepository;

    //:todo 여기 레포지토리 써서 데이터 변경하는 걸로 바꾸기s

    @DisplayName("구간을 생성한다")
    @Test
    void save() {
        // given
        Line 일호선 = new Line(1L, "1호선", "green");
        Station 그린론역 = new Station(1L, "그린론역");
        Station 토미역 = new Station(2L, "토미역");
        Section saveSection = new Section(일호선, 그린론역, 토미역, 10);

        // when
        Long id = sectionRepository.save(saveSection);

        //then
        assertThat(id).isNotNull();
    }

    @DisplayName("라인 Id로 구간들을 찾아온다.")
    @Test
    void findByLineId() {
        Line 일호선 = new Line(1L, "1호선", "green");
        Station 그린론역 = new Station(1L, "그린론역");
        Station 토미역 = new Station(2L, "토미역");
        Station 수달역 = new Station(3L, "수달역");

        stationRepository.save(그린론역);
        stationRepository.save(토미역);
        stationRepository.save(수달역);
        lineRepository.save(일호선);
        // when
        sectionRepository.save(new Section(일호선, 그린론역, 토미역, 10));
        sectionRepository.save(new Section(일호선, 토미역, 수달역, 10));

        List<Section> sections = sectionRepository.findByLineId(일호선.getId());
        // then

        assertThat(sections).hasSize(2);
    }

    @DisplayName("구간을 변경한다.")
    @Test
    void update() {
        Line 일호선 = new Line(1L, "1호선", "green");
        Station 그린론역 = new Station(1L, "그린론역");
        Station 토미역 = new Station(2L, "토미역");
        Station 수달역 = new Station(3L, "수달역");

        stationRepository.save(그린론역);
        stationRepository.save(토미역);
        stationRepository.save(수달역);
        lineRepository.save(일호선);

        // when
        sectionRepository.save(new Section(일호선, 그린론역, 토미역, 10));
        sectionRepository.update(new Section(일호선, 그린론역, 수달역, 10));

        List<Section> sections = sectionRepository.findByLineId(일호선.getId());
        // then
        assertThat(sections.get(0).getDownStation()).isEqualTo(수달역);
    }

    @DisplayName("구간을 삭제한다.")
    @Test
    void delete() {
        Line 일호선 = new Line(1L, "1호선", "green");
        Station 그린론역 = new Station(1L, "그린론역");
        Station 토미역 = new Station(2L, "토미역");
        Station 수달역 = new Station(3L, "수달역");

        stationRepository.save(그린론역);
        stationRepository.save(토미역);
        stationRepository.save(수달역);
        lineRepository.save(일호선);
        // when
        sectionRepository.save(new Section(일호선, 그린론역, 토미역, 10));
        sectionRepository.save(new Section(일호선, 토미역, 수달역, 10));

        sectionRepository.deleteById(그린론역.getId());

        List<Section> sections = sectionRepository.findByLineId(일호선.getId());
        // then
        assertThat(sections.get(0).getDownStation()).isEqualTo(수달역);
    }
}
