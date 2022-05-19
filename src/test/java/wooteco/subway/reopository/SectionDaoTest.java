package wooteco.subway.reopository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import wooteco.subway.domain.Line;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Station;
import wooteco.subway.reopository.dao.LineDao;
import wooteco.subway.reopository.dao.SectionDao;
import wooteco.subway.reopository.dao.StationDao;

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
        Line 일호선 = new Line("1호선", "green");
        Station 그린론역 = new Station(1L, "그린론역");
        Station 토미역 = new Station(2L, "토미역");
        Station 수달역 = new Station(3L, "수달역");

        Long 그린론_id = stationRepository.save(그린론역);
        Long 토미_id = stationRepository.save(토미역);
        Long 수달역_id = stationRepository.save(수달역);
        Long 일호선_id = lineRepository.save(일호선);

        일호선 = new Line(일호선_id, "1호선", "green");
        그린론역 = new Station(그린론_id, "그린론역");
        토미역 = new Station(토미_id, "토미역");
        수달역 = new Station(수달역_id, "수달역");

        sectionRepository.save(new Section(일호선, 그린론역, 토미역, 10));
        sectionRepository.save(new Section(일호선, 토미역, 수달역, 10));

        List<Section> sections = sectionRepository.findByLineId(일호선_id);
        // then®
        assertThat(sections).hasSize(2);
    }

    @DisplayName("구간을 변경한다.")
    @Test
    void update() {
        Line 일호선 = new Line("1호선", "green");
        Station 그린론역 = new Station("그린론역");
        Station 토미역 = new Station("토미역");
        Station 수달역 = new Station("수달역");

        Long 그린론_id = stationRepository.save(그린론역);
        Long 토미_id = stationRepository.save(토미역);
        Long 수달역_id = stationRepository.save(수달역);
        Long 일호선_id = lineRepository.save(일호선);

        일호선 = new Line(일호선_id, "1호선", "green");
        그린론역 = new Station(그린론_id, "그린론역");
        토미역 = new Station(토미_id, "토미역");
        수달역 = new Station(수달역_id, "수달역");
        // when
        Long 구간_id = sectionRepository.save(new Section(일호선, 그린론역, 토미역, 10));
        sectionRepository.update(new Section(구간_id, 일호선, 그린론역, 수달역, 5));

        List<Section> sections = sectionRepository.findByLineId(일호선_id);
        // then
        Station target = sections.get(0).getDownStation();
        assertThat(target.getId()).isEqualTo(수달역.getId());
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
