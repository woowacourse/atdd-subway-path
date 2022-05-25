package wooteco.subway.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import wooteco.subway.dao.StationDao;
import wooteco.subway.domain.station.Station;
import wooteco.subway.dto.request.StationRequest;
import wooteco.subway.dto.response.StationResponse;
import wooteco.subway.exception.NotFoundException;
import wooteco.subway.fixture.DatabaseUsageTest;

@SuppressWarnings("NonAsciiCharacters")
class StationServiceTest extends DatabaseUsageTest {

    @Autowired
    private StationService service;

    @Autowired
    private StationDao stationDao;

    @Test
    void findAll_메서드는_모든_데이터를_id_순서대로_조회() {
        Station 강남역 = new Station(1L, "강남역");
        Station 선릉역 = new Station(2L, "선릉역");
        Station 잠실역 = new Station(3L, "잠실역");
        databaseFixtureUtils.saveStations(강남역, 선릉역, 잠실역);

        List<StationResponse> actual = service.findAll();
        List<StationResponse> expected = List.of(
                new StationResponse(1L, "강남역"),
                new StationResponse(2L, "선릉역"),
                new StationResponse(3L, "잠실역"));

        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("save 메서드는 데이터를 저장한다")
    @Nested
    class SaveTest {

        @Test
        void 중복되지_않는_이름인_경우_성공() {
            StationResponse actual = service.save(new StationRequest("새로운 지하철역"));

            StationResponse expected = new StationResponse(1L, "새로운 지하철역");

            assertThat(actual).isEqualTo(expected);
        }

        @Test
        void 중복되는_이름을_입력한_경우_예외발생() {
            Station 이미_존재하는_역_이름 = new Station("존재하는 역 이름");
            databaseFixtureUtils.saveStations(이미_존재하는_역_이름);

            assertThatThrownBy(() -> service.save(new StationRequest("존재하는 역 이름")))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @DisplayName("delete 메서드는 데이터를 삭제한다")
    @Nested
    class DeleteTest {

        @Test
        void 유효한_데이터의_id가_입력된_경우_삭제성공() {
            Station 이미_존재하는_역 = new Station(1L, "존재하는 역");
            databaseFixtureUtils.saveStations(이미_존재하는_역);

            service.delete(1L);
            boolean notFound = stationDao.findById(1L).isEmpty();

            assertThat(notFound).isTrue();
        }

        @Test
        void 존재하지_않는_데이터의_id가_입력된_경우_예외발생() {
            assertThatThrownBy(() -> service.delete(99999L))
                    .isInstanceOf(NotFoundException.class);
        }

        @Test
        void 노선에_등록된_지하철역의_id가_입력된_경우_예외발생() {
            Station 강남역 = new Station(1L, "강남역");
            Station 선릉역 = new Station(1L, "선릉역");
            databaseFixtureUtils.saveStations(강남역, 선릉역);
            databaseFixtureUtils.saveLine("노선명", "색상");
            databaseFixtureUtils.saveSection(1L, 강남역, 선릉역, 15);

            assertThatThrownBy(() -> service.delete(1L))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
