package wooteco.subway.ui;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import wooteco.subway.domain.Distance;
import wooteco.subway.domain.Fare;
import wooteco.subway.domain.Line;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Station;
import wooteco.subway.dto.path.PathResponse;
import wooteco.subway.exception.IllegalInputException;
import wooteco.subway.exception.path.NoSuchPathException;

class PathControllerTest extends ControllerTest {

    @Autowired
    private PathController pathController;

    private Station gangnam;
    private Station yeoksam;
    private Station seolleung;
    private Station samsung;

    private Station seoulForest;
    private Station wangsimni;

    private Station heangdang;
    private Station majang;
    private Station dapsimni;

    private Station yacksu;
    private Station geumho;
    private Station oksu;

    @BeforeEach
    void setUpData() {
        gangnam = stationDao.insert(new Station("강남역")).orElseThrow();
        yeoksam = stationDao.insert(new Station("역삼역")).orElseThrow();
        seolleung = stationDao.insert(new Station("선릉역")).orElseThrow();
        samsung = stationDao.insert(new Station("삼성역")).orElseThrow();

        seoulForest = stationDao.insert(new Station("서울숲역")).orElseThrow();
        wangsimni = stationDao.insert(new Station("왕십리역")).orElseThrow();

        heangdang = stationDao.insert(new Station("행당역")).orElseThrow();
        majang = stationDao.insert(new Station("마장역")).orElseThrow();
        dapsimni = stationDao.insert(new Station("답십리역")).orElseThrow();

        yacksu = stationDao.insert(new Station("약수역")).orElseThrow();
        geumho = stationDao.insert(new Station("금호역")).orElseThrow();
        oksu = stationDao.insert(new Station("옥수역")).orElseThrow();

        final Line greenLine = lineDao.insert(new Line("2호선", "green")).orElseThrow();
        sectionDao.insert(new Section(greenLine, gangnam, yeoksam, new Distance(10)));
        sectionDao.insert(new Section(greenLine, yeoksam, seolleung, new Distance(8)));
        sectionDao.insert(new Section(greenLine, seolleung, samsung, new Distance(5)));

        final Line yellowLine = lineDao.insert(new Line("수인분당선", "yellow")).orElseThrow();
        sectionDao.insert(new Section(yellowLine, seolleung, seoulForest, new Distance(12)));
        sectionDao.insert(new Section(yellowLine, seoulForest, wangsimni, new Distance(7)));

        final Line purpleLine = lineDao.insert(new Line("5호선", "purple")).orElseThrow();
        sectionDao.insert(new Section(purpleLine, heangdang, wangsimni, new Distance(11)));
        sectionDao.insert(new Section(purpleLine, wangsimni, majang, new Distance(17)));
        sectionDao.insert(new Section(purpleLine, majang, dapsimni, new Distance(15)));

        final Line orangeLine = lineDao.insert(new Line("3호선", "orange")).orElseThrow();
        sectionDao.insert(new Section(orangeLine, yacksu, geumho, new Distance(7)));
        sectionDao.insert(new Section(orangeLine, geumho, oksu, new Distance(12)));
    }

    @Test
    @DisplayName("출발 역과 도착 역이 동일하면 예외를 던진다.")
    void ShowPath_SameStation_ExceptionThrown() {
        // given
        final Long stationId = gangnam.getId();

        // when, then
        assertThatThrownBy(() -> pathController.showPath(stationId, stationId))
                .isInstanceOf(IllegalInputException.class)
                .hasMessage("출발역과 도착역이 동일합니다.");
    }

    @Test
    @DisplayName("출발 역에서 도착 역으로 이동할 수 없으면 예외를 던진다.")
    void ShowPath_NotExistPath_ExceptionThrown() {
        // given
        final Long sourceStationId = gangnam.getId();
        final Long targetStationId = yacksu.getId();

        // when, then
        assertThatThrownBy(() -> pathController.showPath(sourceStationId, targetStationId))
                .isInstanceOf(NoSuchPathException.class);
    }

    @Test
    @DisplayName("출발 역에서 도착 역으로 최단 경로를 계산한다.")
    void ShowPath() {
        // given
        final Long sourceStationId = gangnam.getId();
        final Long targetStationId = dapsimni.getId();

        final List<Station> expectedStations = List.of(
                gangnam,
                yeoksam,
                seolleung,
                seoulForest,
                wangsimni,
                majang,
                dapsimni
        );
        final PathResponse expected = PathResponse.of(expectedStations, new Distance(69), new Fare(2350));

        // when
        final PathResponse actual = pathController.showPath(sourceStationId, targetStationId)
                .getBody();

        // then
        assertThat(actual).isEqualTo(expected);
    }
}