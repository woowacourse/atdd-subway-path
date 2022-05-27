package wooteco.subway.domain.fare;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import wooteco.subway.domain.line.Line;
import wooteco.subway.domain.line.Lines;

class FareCalculatorTest {

    private FareCalculator fareCalculator;

    @DisplayName("무임승차가 가능한 age 이고 거리가 10km 이하 일 때, 요금을 계산한다.")
    @ParameterizedTest
    @ValueSource(ints = {5, 65})
    void calculateFareByFreeAgeAndLessThen10Km(int age) {
        fareCalculator = new FareCalculator(List.of(1L, 2L, 3L));

        int distance = 10;
        Lines lines = new Lines(
                List.of(new Line(1L, "", "", 0),
                        new Line(2L, "", "", 0),
                        new Line(3L, "", "", 0)));

        int fare = fareCalculator.calculateFare(age, distance, lines);

        assertThat(fare).isEqualTo(0);
    }

    @DisplayName("무임승차가 가능한 age 이고 거리가 10km 초과 50km 이하 일 때, 요금을 계산한다.")
    @ParameterizedTest
    @CsvSource({"5,11", "5,15", "5,16", "5,50", "65,11", "65,15", "65,16", "65,50"})
    void calculateFareByFreeAgeAndExceed10KmAndLessThen50Km(int age, int distance) {
        fareCalculator = new FareCalculator(List.of(1L, 2L, 3L));

        Lines lines = new Lines(
                List.of(new Line(1L, "", "", 0),
                        new Line(2L, "", "", 0),
                        new Line(3L, "", "", 0)));

        int fare = fareCalculator.calculateFare(age, distance, lines);

        assertThat(fare).isEqualTo(0);
    }

    @DisplayName("무임승차가 가능한 age 이고 거리가 50km 초과 일 때, 요금을 계산한다.")
    @ParameterizedTest
    @ValueSource(ints = {5, 65})
    void calculateFareByFreeAgeAndExceed50Km(int age) {
        fareCalculator = new FareCalculator(List.of(1L, 2L, 3L));

        Lines lines = new Lines(
                List.of(new Line(1L, "", "", 0),
                        new Line(2L, "", "", 0),
                        new Line(3L, "", "", 0)));

        int fare = fareCalculator.calculateFare(age, 51, lines);

        assertThat(fare).isEqualTo(0);
    }

    @DisplayName("age 가 6이상 12이하 이고 거리가 10km 이하 일 때, 요금을 계산한다.")
    @ParameterizedTest
    @ValueSource(ints = {6, 12})
    void calculateFareByChildrenAgeAndLessThen10Km(int age) {
        fareCalculator = new FareCalculator(List.of(1L, 2L, 3L));

        Lines lines = new Lines(
                List.of(new Line(1L, "", "", 0),
                        new Line(2L, "", "", 0),
                        new Line(3L, "", "", 0)));

        int fare = fareCalculator.calculateFare(age, 10, lines);

        assertThat(fare).isEqualTo(450);
    }

    @DisplayName("age 가 6이상 12이하 이고 거리가 10km 이하이고 MaxExtraFare 가 1000원 일 때, 요금을 계산한다.")
    @ParameterizedTest
    @ValueSource(ints = {6, 12})
    void calculateFareByChildrenAgeAndLessThen10KmAndMaxExtraFare1000(int age) {
        fareCalculator = new FareCalculator(List.of(1L, 2L, 3L));

        Lines lines = new Lines(
                List.of(new Line(1L, "", "", 0),
                        new Line(2L, "", "", 1000),
                        new Line(3L, "", "", 500)));

        int fare = fareCalculator.calculateFare(age, 10, lines);

        assertThat(fare).isEqualTo(500 + 450);
    }

    @DisplayName("age 가 6이상 12이하 이고 거리가 10km 초과 50km 이하 일 때, 요금을 계산한다.")
    @ParameterizedTest
    @CsvSource({"6,11,500", "6,15,500", "6,16,550", "6,50,850", "12,11,500", "12,15,500", "12,16,550", "12,50,850"})
    void calculateFareByChildrenAgeAndExceed10KmAndLessThen50Km(int age, int distance, int expectedFare) {
        fareCalculator = new FareCalculator(List.of(1L, 2L, 3L));

        Lines lines = new Lines(
                List.of(new Line(1L, "", "", 0),
                        new Line(2L, "", "", 0),
                        new Line(3L, "", "", 0)));

        int fare = fareCalculator.calculateFare(age, distance, lines);

        assertThat(fare).isEqualTo(expectedFare);
    }

    @DisplayName("age 가 6이상 12이하 이고 거리가 10km 초과 50km 이하이고 MaxExtraFare 가 1000원 일 때, 요금을 계산한다.")
    @ParameterizedTest
    @CsvSource({"6,11,1000", "6,15,1000", "6,16,1050", "6,50,1350",
            "12,11,1000", "12,15,1000", "12,16,1050", "12,50,1350"})
    void calculateFareByChildrenAgeAndExceed10KmAndLessThen50KmAndMaxExtraFare1000(int age, int distance,
                                                                                   int expectedFare) {
        fareCalculator = new FareCalculator(List.of(1L, 2L, 3L));

        Lines lines = new Lines(
                List.of(new Line(1L, "", "", 0),
                        new Line(2L, "", "", 1000),
                        new Line(3L, "", "", 500)));

        int fare = fareCalculator.calculateFare(age, distance, lines);

        assertThat(fare).isEqualTo(expectedFare);
    }

    @DisplayName("age 가 6이상 12이하 이고 거리가 50km 초과 일 때, 요금을 계산한다.")
    @ParameterizedTest
    @ValueSource(ints = {6, 12})
    void calculateFareByChildrenAgeAndExceed50Km(int age) {
        fareCalculator = new FareCalculator(List.of(1L, 2L, 3L));

        Lines lines = new Lines(
                List.of(new Line(1L, "", "", 0),
                        new Line(2L, "", "", 0),
                        new Line(3L, "", "", 0)));

        int fare = fareCalculator.calculateFare(age, 51, lines);

        assertThat(fare).isEqualTo(900);
    }

    @DisplayName("age 가 6이상 12이하 이고 거리가 50km 초과이고 MaxExtraFare 가 1000원 일 때, 요금을 계산한다.")
    @ParameterizedTest
    @ValueSource(ints = {6, 12})
    void calculateFareByChildrenAgeAndExceed50KmAndMaxExtraFare1000(int age) {
        fareCalculator = new FareCalculator(List.of(1L, 2L, 3L));

        Lines lines = new Lines(
                List.of(new Line(1L, "", "", 0),
                        new Line(2L, "", "", 1000),
                        new Line(3L, "", "", 500)));

        int fare = fareCalculator.calculateFare(age, 51, lines);

        assertThat(fare).isEqualTo(1400);
    }

    @DisplayName("age 가 13이상 18이하 이고 거리가 10km 이하 일 때, 요금을 계산한다.")
    @ParameterizedTest
    @ValueSource(ints = {13, 18})
    void calculateFareByTeenagerAgeAndLessThen10Km(int age) {
        fareCalculator = new FareCalculator(List.of(1L, 2L, 3L));

        Lines lines = new Lines(
                List.of(new Line(1L, "", "", 0),
                        new Line(2L, "", "", 0),
                        new Line(3L, "", "", 0)));

        int fare = fareCalculator.calculateFare(age, 10, lines);

        assertThat(fare).isEqualTo(720);
    }

    @DisplayName("age 가 13이상 18이하 이고 거리가 10km 이하이고 MaxExtraFare 가 1000원 일 때, 요금을 계산한다.")
    @ParameterizedTest
    @ValueSource(ints = {13, 18})
    void calculateFareByTeenagerAgeAndLessThen10KmAndMaxExtraFare1000(int age) {
        fareCalculator = new FareCalculator(List.of(1L, 2L, 3L));

        Lines lines = new Lines(
                List.of(new Line(1L, "", "", 0),
                        new Line(2L, "", "", 1000),
                        new Line(3L, "", "", 500)));

        int fare = fareCalculator.calculateFare(age, 10, lines);

        assertThat(fare).isEqualTo(1520);
    }

    @DisplayName("age 가 13이상 18이하 이고 거리가 10km 초과 50km 이하 일 때, 요금을 계산한다.")
    @ParameterizedTest
    @CsvSource({"13,11,800", "13,15,800", "13,16,880", "13,50,1360",
            "18,11,800", "18,15,800", "18,16,880", "18,50,1360"})
    void calculateFareByTeenagerAgeAndExceed10KmAndLessThen50Km(int age, int distance, int expectedFare) {
        fareCalculator = new FareCalculator(List.of(1L, 2L, 3L));

        Lines lines = new Lines(
                List.of(new Line(1L, "", "", 0),
                        new Line(2L, "", "", 0),
                        new Line(3L, "", "", 0)));

        int fare = fareCalculator.calculateFare(age, distance, lines);

        assertThat(fare).isEqualTo(expectedFare);
    }

    @DisplayName("age 가 13이상 18이하 이고 거리가 10km 초과 50km 이하이고 MaxExtraFare 가 1000원 일 때, 요금을 계산한다.")
    @ParameterizedTest
    @CsvSource({"13,11,1600", "13,15,1600", "13,16,1680", "13,50,2160",
            "18,11,1600", "18,15,1600", "18,16,1680", "18,50,2160"})
    void calculateFareByTeenagerAgeAndExceed10KmAndLessThen50KmAndMaxExtraFare1000(int age, int distance,
                                                                                   int expectedFare) {
        fareCalculator = new FareCalculator(List.of(1L, 2L, 3L));

        Lines lines = new Lines(
                List.of(new Line(1L, "", "", 0),
                        new Line(2L, "", "", 1000),
                        new Line(3L, "", "", 500)));

        int fare = fareCalculator.calculateFare(age, distance, lines);

        assertThat(fare).isEqualTo(expectedFare);
    }

    @DisplayName("age 가 13이상 18이하 이고 거리가 50km 초과 일 때, 요금을 계산한다.")
    @ParameterizedTest
    @ValueSource(ints = {13, 18})
    void calculateFareByTeenagerAgeAndExceed50Km(int age) {
        fareCalculator = new FareCalculator(List.of(1L, 2L, 3L));

        Lines lines = new Lines(
                List.of(new Line(1L, "", "", 0),
                        new Line(2L, "", "", 0),
                        new Line(3L, "", "", 0)));

        int fare = fareCalculator.calculateFare(age, 51, lines);

        assertThat(fare).isEqualTo(1440);
    }

    @DisplayName("age 가 13이상 18이하 이고 거리가 50km 초과이고 MaxExtraFare 가 1000원 일 때, 요금을 계산한다.")
    @ParameterizedTest
    @ValueSource(ints = {13, 18})
    void calculateFareByTeenagerAgeAndExceed50KmAndMaxExtraFare1000(int age) {
        fareCalculator = new FareCalculator(List.of(1L, 2L, 3L));

        Lines lines = new Lines(
                List.of(new Line(1L, "", "", 0),
                        new Line(2L, "", "", 1000),
                        new Line(3L, "", "", 500)));

        int fare = fareCalculator.calculateFare(age, 51, lines);

        assertThat(fare).isEqualTo(2240);
    }

    @DisplayName("age 가 19이상 64이하 이고 거리가 10km 이하 일 때, 요금을 계산한다.")
    @ParameterizedTest
    @ValueSource(ints = {19, 64})
    void calculateFareByAdultAgeAndLessThen10Km(int age) {
        fareCalculator = new FareCalculator(List.of(1L, 2L, 3L));

        Lines lines = new Lines(
                List.of(new Line(1L, "", "", 0),
                        new Line(2L, "", "", 0),
                        new Line(3L, "", "", 0)));

        int fare = fareCalculator.calculateFare(age, 10, lines);

        assertThat(fare).isEqualTo(1250);
    }

    @DisplayName("age 가 19이상 64이하 이고 거리가 10km 이하이고 MaxExtraFare 가 1000원 일 때, 요금을 계산한다.")
    @ParameterizedTest
    @ValueSource(ints = {19, 64})
    void calculateFareByAdultAgeAndLessThen10KmAndMaxExtraFare1000(int age) {
        fareCalculator = new FareCalculator(List.of(1L, 2L, 3L));

        Lines lines = new Lines(
                List.of(new Line(1L, "", "", 0),
                        new Line(2L, "", "", 1000),
                        new Line(3L, "", "", 500)));

        int fare = fareCalculator.calculateFare(age, 10, lines);

        assertThat(fare).isEqualTo(2250);
    }

    @DisplayName("age 가 19이상 64이하 이고 거리가 10km 초과 50km 이하 일 때, 요금을 계산한다.")
    @ParameterizedTest
    @CsvSource({"19,11,1350", "19,15,1350", "19,16,1450", "19,50,2050",
            "64,11,1350", "64,15,1350", "64,16,1450", "64,50,2050"})
    void calculateFareByAdultAgeAndExceed10KmAndLessThen50Km(int age, int distance, int expectedFare) {
        fareCalculator = new FareCalculator(List.of(1L, 2L, 3L));

        Lines lines = new Lines(
                List.of(new Line(1L, "", "", 0),
                        new Line(2L, "", "", 0),
                        new Line(3L, "", "", 0)));

        int fare = fareCalculator.calculateFare(age, distance, lines);

        assertThat(fare).isEqualTo(expectedFare);
    }

    @DisplayName("age 가 19이상 64이하 이고 거리가 10km 초과 50km 이하이고 MaxExtraFare 가 1000원 일 때, 요금을 계산한다.")
    @ParameterizedTest
    @CsvSource({"19,11,2350", "19,15,2350", "19,16,2450", "19,50,3050",
            "64,11,2350", "64,15,2350", "64,16,2450", "64,50,3050"})
    void calculateFareByAdultAgeAndExceed10KmAndLessThen50KmAndMaxExtraFare1000(int age, int distance,
                                                                                int expectedFare) {
        fareCalculator = new FareCalculator(List.of(1L, 2L, 3L));

        Lines lines = new Lines(
                List.of(new Line(1L, "", "", 0),
                        new Line(2L, "", "", 1000),
                        new Line(3L, "", "", 500)));

        int fare = fareCalculator.calculateFare(age, distance, lines);

        assertThat(fare).isEqualTo(expectedFare);
    }

    @DisplayName("age 가 19이상 64이하 이고 거리가 50km 초과 일 때, 요금을 계산한다.")
    @ParameterizedTest
    @ValueSource(ints = {19, 64})
    void calculateFareByAdultAgeAndExceed50Km(int age) {
        fareCalculator = new FareCalculator(List.of(1L, 2L, 3L));

        Lines lines = new Lines(
                List.of(new Line(1L, "", "", 0),
                        new Line(2L, "", "", 0),
                        new Line(3L, "", "", 0)));

        int fare = fareCalculator.calculateFare(age, 51, lines);

        assertThat(fare).isEqualTo(2150);
    }

    @DisplayName("age 가 19이상 64이하 이고 거리가 50km 초과이고 MaxExtraFare 가 1000원 일 때, 요금을 계산한다.")
    @ParameterizedTest
    @ValueSource(ints = {19, 64})
    void calculateFareByAdultAgeAndExceed50KmAndMaxExtraFare1000(int age) {
        fareCalculator = new FareCalculator(List.of(1L, 2L, 3L));

        Lines lines = new Lines(
                List.of(new Line(1L, "", "", 0),
                        new Line(2L, "", "", 1000),
                        new Line(3L, "", "", 500)));

        int fare = fareCalculator.calculateFare(age, 51, lines);

        assertThat(fare).isEqualTo(3150);
    }

}
