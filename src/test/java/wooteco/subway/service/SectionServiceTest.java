package wooteco.subway.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import wooteco.subway.dao.SectionDao;
import wooteco.subway.domain.section.Section;
import wooteco.subway.domain.station.Station;
import wooteco.subway.dto.request.CreateSectionRequest;
import wooteco.subway.entity.LineEntity;
import wooteco.subway.exception.NotFoundException;
import wooteco.subway.fixture.DatabaseUsageTest;

@SuppressWarnings("NonAsciiCharacters")
class SectionServiceTest extends DatabaseUsageTest {

    @Autowired
    private SectionService service;

    @Autowired
    protected SectionDao dao;

    @DisplayName("save 메서드는 데이터를 저장한다")
    @Nested
    class SaveTest {

        private final Station 강남역 = new Station(1L, "강남역");
        private final Station 선릉역 = new Station(2L, "선릉역");
        private final Station 잠실역 = new Station(3L, "잠실역");

        @BeforeEach
        void setupStations() {
            databaseFixtureUtils.saveStations(강남역, 선릉역, 잠실역);
            saveLineTestFixture("존재하는 노선", "색깔", 0);
        }

        @Test
        void 상행_종점_등록시_그대로_저장() {
            Section 선릉_잠실 = new Section(1L, 선릉역, 잠실역, 10);
            databaseFixtureUtils.saveSections(선릉_잠실);

            service.save(1L, generateCreateSectionRequest(강남역, 선릉역, 20));
            List<Section> actual = dao.findAllByLineId(1L);
            List<Section> expected = List.of(
                    new Section(1L, 강남역, 선릉역, 20),
                    선릉_잠실);

            assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);
        }

        private CreateSectionRequest generateCreateSectionRequest(Station upStation,
                                                                  Station downStation,
                                                                  int distance) {
            CreateSectionRequest request = new CreateSectionRequest();
            request.setUpStationId(upStation.getId());
            request.setDownStationId(downStation.getId());
            request.setDistance(distance);
            return request;
        }

        @Test
        void 하행_종점_등록시_그대로_저장() {
            Section 강남_선릉 = new Section(1L, 강남역, 선릉역, 10);
            databaseFixtureUtils.saveSections(강남_선릉);

            service.save(1L, generateCreateSectionRequest(선릉역, 잠실역, 30));
            List<Section> actual = dao.findAllByLineId(1L);
            List<Section> expected = List.of(강남_선릉,
                    new Section(1L, 선릉역, 잠실역, 30));

            assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);
        }

        @Test
        void 저장하려는_구간의_상행역이_이미_상행역으로_등록된_경우_저장_후_기존_구간은_수정() {
            int existingSectionDistance = 10;
            int newSectionDistance = 2;
            Section 선릉_잠실 = new Section(1L, 선릉역, 잠실역, existingSectionDistance - newSectionDistance);
            databaseFixtureUtils.saveSections(선릉_잠실);

            service.save(1L, generateCreateSectionRequest(강남역, 선릉역, newSectionDistance));
            List<Section> actual = dao.findAllByLineId(1L);
            List<Section> expected = List.of(
                    new Section(1L, 강남역, 선릉역, newSectionDistance), 선릉_잠실);

            assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);
        }

        @Test
        void 저장하려는_구간의_히행역이_이미_하행역으로_등록된_경우_저장_후_기존_구간은_수정() {
            int existingSectionDistance = 10;
            int newSectionDistance = 3;
            saveSectionTestFixture(1L, 강남역, 잠실역, existingSectionDistance);

            service.save(1L, generateCreateSectionRequest(선릉역, 잠실역, newSectionDistance));
            List<Section> actual = dao.findAllByLineId(1L);
            List<Section> expected = List.of(
                    new Section(1L, 강남역, 선릉역, existingSectionDistance - newSectionDistance),
                    new Section(1L, 선릉역, 잠실역, newSectionDistance));

            assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);
        }
    }

    @DisplayName("delete 메서드는 노선의 특정 구간 데이터를 삭제한다")
    @Nested
    class DeleteTest {

        private final Station 강남역 = new Station(1L, "강남역");
        private final Station 선릉역 = new Station(2L, "선릉역");
        private final Station 잠실역 = new Station(3L, "잠실역");

        @Test
        void 노선의_종점을_제거하려는_경우_그와_연결된_구간만_하나_제거() {
            databaseFixtureUtils.saveStations(강남역, 선릉역, 잠실역);
            saveLineTestFixture("노선", "색깔", 0);
            Section 선릉_잠실 = new Section(1L, 선릉역, 잠실역, 10);
            saveSectionTestFixture(1L, 강남역, 선릉역, 10);
            databaseFixtureUtils.saveSections(선릉_잠실);

            service.delete(1L, 1L);
            List<Section> actual = dao.findAllByLineId(1L);
            List<Section> expected = List.of(선릉_잠실);

            assertThat(actual).isEqualTo(expected);
        }

        @Test
        void 노선의_중앙에_있는_역을_제거한_경우_그_사이를_잇는_구간을_새로_생성() {
            databaseFixtureUtils.saveStations(강남역, 선릉역, 잠실역);
            saveLineTestFixture("노선", "색깔", 0);
            saveSectionTestFixture(1L, 강남역, 선릉역, 5);
            saveSectionTestFixture(1L, 선릉역, 잠실역, 10);

            service.delete(1L, 2L);
            List<Section> actual = dao.findAllByLineId(1L);
            List<Section> expected = List.of(
                    new Section(1L, 강남역, 잠실역, 15));

            assertThat(actual).isEqualTo(expected);
        }

        @Test
        void 등록되지_않은_노선_id가_입력된_경우_예외발생() {
            databaseFixtureUtils.saveStations(강남역, 선릉역, 잠실역);
            assertThatThrownBy(() -> service.delete(99999L, 1L))
                    .isInstanceOf(NotFoundException.class);
        }

        @Test
        void 노선에_구간으로_등록되지_않은_지하철역_id가_입력된_경우_예외발생() {
            databaseFixtureUtils.saveStations(강남역, 선릉역, 잠실역);
            saveLineTestFixture("노선", "색깔", 0);
            saveSectionTestFixture(1L, 강남역, 선릉역, 10);

            assertThatThrownBy(() -> service.delete(1L, 3L))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 노선의_구간이_하나_남은_경우_구간_제거_시도시_예외발생() {
            databaseFixtureUtils.saveStations(강남역, 선릉역, 잠실역);
            saveLineTestFixture("노선", "색깔", 0);
            saveSectionTestFixture(1L, 강남역, 선릉역, 10);

            assertThatThrownBy(() -> service.delete(1L, 1L))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    private void saveLineTestFixture(String name, String color, int extraFare) {
        databaseFixtureUtils.saveLines(new LineEntity(name, color, extraFare));
    }

    private void saveSectionTestFixture(Long lineId, Station upStation, Station downStation, int distance) {
        databaseFixtureUtils.saveSections(new Section(lineId, upStation,downStation, distance));
    }
}
