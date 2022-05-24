package wooteco.subway.domain.fare;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import wooteco.subway.domain.line.Line;
import wooteco.subway.domain.section.Section;

public class FareTest {

    private Long 강남 = 1L;
    private Long 선릉 = 2L;
    private Long 잠실 = 3L;
    private Long 사당 = 4L;

    private Line 지하철_2호선 = new Line(1L, "지하철2호선", "green", 0);
    private Line 지하철_3호선 = new Line(2L, "지하철3호선", "orange", 400);

    private Section 강남_선릉_9 = new Section(1L, 1L, 강남, 선릉, 10);
    private Section 선릉_잠실_10 = new Section(2L, 1L, 선릉, 잠실, 10);
    private Section 잠실_사당_10 = new Section(3L, 2L, 잠실, 사당, 10);

    @Test
    @DisplayName("요금을 계산한다 / 10km 이하")
    void calculateFareUnder10km() {
        List<Long> path = List.of(강남, 선릉);
        List<Section> sections = List.of(강남_선릉_9);
        List<Line> lines = List.of(지하철_2호선);
        Fare _9_25_0 = new Fare(9, 25);

        assertThat(_9_25_0.calculateFare(path, sections, lines)).isEqualTo(1250);
    }

    @Test
    @DisplayName("요금을 계산한다 / 50km 이하")
    void calculateFareUnder50km() {
        List<Long> path = List.of(강남, 선릉);
        List<Section> sections = List.of(강남_선릉_9);
        List<Line> lines = List.of(지하철_2호선);
        Fare _49_25_0 = new Fare(49, 25);

        assertThat(_49_25_0.calculateFare(path, sections, lines)).isEqualTo(2050);
    }

    @Test
    @DisplayName("요금을 계산한다 / 50km 초과")
    void calculateFareOver50km() {
        List<Long> path = List.of(강남, 선릉);
        List<Section> sections = List.of(강남_선릉_9);
        List<Line> lines = List.of(지하철_2호선);
        Fare _79_25_0 = new Fare(79, 25);

        assertThat(_79_25_0.calculateFare(path, sections, lines)).isEqualTo(3050);
    }

    @Test
    @DisplayName("요금을 계산한다 / 10km")
    void calculateFareAt10km() {
        List<Long> path = List.of(강남, 선릉);
        List<Section> sections = List.of(강남_선릉_9);
        List<Line> lines = List.of(지하철_2호선);
        Fare _10_25_0 = new Fare(79, 25);

        assertThat(_10_25_0.calculateFare(path, sections, lines)).isEqualTo(3050);
    }

    @Test
    @DisplayName("요금을 계산한다 / 50km")
    void calculateFareAt50km() {
        List<Long> path = List.of(강남, 선릉);
        List<Section> sections = List.of(강남_선릉_9);
        List<Line> lines = List.of(지하철_2호선);
        Fare _50_25_0 = new Fare(50, 25);

        assertThat(_50_25_0.calculateFare(path, sections, lines)).isEqualTo(2050);
    }

    @Test
    @DisplayName("요금을 계산한다 / 25km")
    void calculateFareAt25km() {
        List<Long> path = List.of(강남, 선릉);
        List<Section> sections = List.of(강남_선릉_9);
        List<Line> lines = List.of(지하철_2호선);
        Fare _25_25_0 = new Fare(25, 25);

        assertThat(_25_25_0.calculateFare(path, sections, lines)).isEqualTo(1550);
    }

    @Test
    @DisplayName("요금을 계산한다 / 노선에 추가요금 존재")
    void calculateFareWithExtraFare() {
        List<Long> path = List.of(강남, 선릉, 선릉, 잠실);
        List<Section> sections = List.of(강남_선릉_9, 선릉_잠실_10, 잠실_사당_10);
        List<Line> lines = List.of(지하철_2호선, 지하철_3호선);
        Fare _25_25_400 = new Fare(25, 25);

        assertThat(_25_25_400.calculateFare(path, sections, lines)).isEqualTo(1950);
    }

    @ParameterizedTest
    @DisplayName("요금을 계산한다 / 유아 요금 우대")
    @ValueSource(ints = {0, 1, 2, 3, 4, 5})
    void calculateFareWithInfantDiscount(int age) {
        List<Long> path = List.of(강남, 선릉);
        List<Section> sections = List.of(강남_선릉_9);
        List<Line> lines = List.of(지하철_2호선);
        Fare _11_infant_0 = new Fare(11, age);

        assertThat(_11_infant_0.calculateFare(path, sections, lines)).isEqualTo(0);
    }

    @ParameterizedTest
    @DisplayName("요금을 계산한다 / 어린이 요금 할인")
    @ValueSource(ints = {6, 7, 8, 9, 10, 11, 12})
    void calculateFareWithChildDiscount(int age) {
        List<Long> path = List.of(강남, 선릉);
        List<Section> sections = List.of(강남_선릉_9);
        List<Line> lines = List.of(지하철_2호선);
        Fare _11_child_0 = new Fare(11, age);

        assertThat(_11_child_0.calculateFare(path, sections, lines)).isEqualTo(500);
    }

    @ParameterizedTest
    @DisplayName("요금을 계산한다 / 청소년 요금 할인")
    @ValueSource(ints = {13, 14, 15, 16, 17, 18})
    void calculateFareWithAdolescentDiscount(int age) {
        List<Long> path = List.of(강남, 선릉);
        List<Section> sections = List.of(강남_선릉_9);
        List<Line> lines = List.of(지하철_2호선);
        Fare _11_adolescent_0 = new Fare(11, age);

        assertThat(_11_adolescent_0.calculateFare(path, sections, lines)).isEqualTo(800);
    }

    @ParameterizedTest
    @DisplayName("요금을 계산한다 / 성인 요금")
    @ValueSource(ints = {19, 20, 63, 64})
    void calculateFareWithAdultDiscount(int age) {
        List<Long> path = List.of(강남, 선릉);
        List<Section> sections = List.of(강남_선릉_9);
        List<Line> lines = List.of(지하철_2호선);
        Fare _11_adult_0 = new Fare(11, age);

        assertThat(_11_adult_0.calculateFare(path, sections, lines)).isEqualTo(1350);
    }

    @ParameterizedTest
    @DisplayName("요금을 계산한다 / 노인 우대")
    @ValueSource(ints = {65, 66, Integer.MAX_VALUE})
    void calculateFareWithElderDiscount(int age) {
        List<Long> path = List.of(강남, 선릉);
        List<Section> sections = List.of(강남_선릉_9);
        List<Line> lines = List.of(지하철_2호선);
        Fare _11_elder_0 = new Fare(11, age);

        assertThat(_11_elder_0.calculateFare(path, sections, lines)).isEqualTo(0);
    }
}
