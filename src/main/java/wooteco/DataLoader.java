package wooteco;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import wooteco.auth.dao.MemberDao;
import wooteco.auth.domain.Member;
import wooteco.subway.dao.LineDao;
import wooteco.subway.dao.SectionDao;
import wooteco.subway.dao.StationDao;
import wooteco.subway.domain.Distance;
import wooteco.subway.domain.Line;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Station;

@Component
@Profile("!test")
public class DataLoader implements CommandLineRunner {

    private final StationDao stationDao;
    private final LineDao lineDao;
    private final SectionDao sectionDao;
    private final MemberDao memberDao;

    public DataLoader(StationDao stationDao, LineDao lineDao, SectionDao sectionDao,
        MemberDao memberDao) {
        this.stationDao = stationDao;
        this.lineDao = lineDao;
        this.sectionDao = sectionDao;
        this.memberDao = memberDao;
    }

    @Override
    public void run(String... args) throws Exception {
        Station 강남역 = stationDao.insert(new Station("강남역"));
        Station 판교역 = stationDao.insert(new Station("판교역"));
        Station 정자역 = stationDao.insert(new Station("정자역"));
        Station 역삼역 = stationDao.insert(new Station("역삼역"));
        Station 잠실역 = stationDao.insert(new Station("잠실역"));

        Distance 공통거리 = new Distance(10);

        Line 신분당선 = lineDao.insert(new Line("신분당선", "red lighten-1"));
        sectionDao.insert(new Section(신분당선, 강남역, 판교역, 공통거리));
        sectionDao.insert(new Section(신분당선, 판교역, 정자역, 공통거리));

        Line 이호선 = lineDao.insert(new Line("2호선", "green lighten-1"));
        sectionDao.insert(new Section(이호선, 강남역, 역삼역, 공통거리));
        sectionDao.insert(new Section(이호선, 역삼역, 잠실역, 공통거리));

        Member member = new Member("email@email.com", "password", 10);
        memberDao.insert(member);
    }
}

