package wooteco.subway.admin.domain;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import wooteco.subway.admin.domain.exception.NoExistStationException;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class StationsTest {
    private static Station 왕십리;
    private static Station 한양대;
    private static Station 뚝섬;
    private static Station 성수;
    private static Station 건대입구;
    private static Station 뚝섬유원지;
    private static Station 청담;
    private static Station 강남구청;
    private static Station 압구정로데오;
    private static Station 서울숲;
    private static Station 잠실;
    private static List<Station> stations;

    private static Stations stations1;

    @BeforeAll
    static void setUp() {
        왕십리 = new Station(1L, "왕십리");
        한양대 = new Station(2L, "한양대");
        뚝섬 = new Station(3L, "뚝섬");
        성수 = new Station(4L, "성수");
        건대입구 = new Station(5L, "건대입구");
        뚝섬유원지 = new Station(6L, "뚝섬유원지");
        청담 = new Station(7L, "청담");
        강남구청 = new Station(8L, "강남구청");
        압구정로데오 = new Station(9L, "압구정로데오");
        서울숲 = new Station(10L, "서울숲");
        잠실 = new Station(11L, "잠실");

        stations = Arrays.asList(왕십리, 한양대, 뚝섬, 성수, 건대입구, 뚝섬유원지,
                청담, 강남구청, 압구정로데오, 서울숲);

        stations1 = new Stations(stations);
    }

    @Test
    void findStation() {
        assertThat(stations1.findStation(강남구청.getId())).isEqualTo(강남구청);
        assertThat(stations1.findStation(성수.getId())).isEqualTo(성수);
        assertThat(stations1.findStation(압구정로데오.getId())).isEqualTo(압구정로데오);
    }

    @Test
    void findNoExistStation() {
        assertThatThrownBy(() -> stations1.findStation(잠실.getId()))
                .isInstanceOf(NoExistStationException.class);
    }
}