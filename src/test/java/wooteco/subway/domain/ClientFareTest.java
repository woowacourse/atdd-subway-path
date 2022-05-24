package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class ClientFareTest {

    @DisplayName("구간 리스트와 나이를 입력하여 요금을 계산한다.")
    @ParameterizedTest
    @CsvSource(value = {"9:1250", "10:1250", "11:1350", "15:1350", "16:1450", "50:2050", "58:2150", "59:2250"}, delimiter = ':')
    void calculateFare1(int input, int expected) {
        // given
        Line line = new Line("name", "color", 0);
        Section section1 = new Section(line, 1L, 2L, 3);
        Section section2 = new Section(line, 2L, 3L, 4);
        List<Section> inputSections = new ArrayList<>();
        inputSections.add(section1);
        inputSections.add(section2);
        Sections sections = new Sections(inputSections);

        // when
        ClientFare clientFare = new ClientFare(20, sections, input);
        int result = clientFare.calculateFare();

        // then
        assertThat(result).isEqualTo(expected);
    }

    @DisplayName("구간 리스트와 나이를 입력한 후 나이에 따른 할인 요금을 계산한다. -> 청소년(13이상 19세미만): 운임에서 350원을 공제한 금액의 20%할인")
    @ParameterizedTest
    @CsvSource(value = {"9:720", "10:720", "11:800", "15:800", "16:880", "50:1360", "58:1440", "59:1520"}, delimiter = ':')
    void calculateFare2(int input, int expected) {
        // given
        Line line = new Line("name", "color", 0);
        Section section1 = new Section(line, 1L, 2L, 3);
        Section section2 = new Section(line, 2L, 3L, 4);
        List<Section> inputSections = new ArrayList<>();
        inputSections.add(section1);
        inputSections.add(section2);
        Sections sections = new Sections(inputSections);

        // when
        ClientFare clientFare = new ClientFare(13, sections, input);
        int result = clientFare.calculateFare();

        // then
        assertThat(result).isEqualTo(expected);
    }

    @DisplayName("구간 리스트와 나이를 입력한 후 나이에 따른 할인 요금을 계산한다. -> 어린이(6세이상 13세미만): 운임에서 350원을 공제한 금액의 50%할인")
    @ParameterizedTest
    @CsvSource(value = {"9:450", "10:450", "11:500", "15:500", "16:550", "50:850", "58:900", "59:950"}, delimiter = ':')
    void calculateFare3(int input, int expected) {
        // given
        Line line = new Line("name", "color", 0);
        Section section1 = new Section(line, 1L, 2L, 3);
        Section section2 = new Section(line, 2L, 3L, 4);
        List<Section> inputSections = new ArrayList<>();
        inputSections.add(section1);
        inputSections.add(section2);
        Sections sections = new Sections(inputSections);

        // when
        ClientFare clientFare = new ClientFare(6, sections, input);
        int result = clientFare.calculateFare();

        // then
        assertThat(result).isEqualTo(expected);
    }

    @DisplayName("구간 리스트와 나이를 입력한 후 나이에 따른 할인 요금을 계산한다. -> 노선에 추가 요금이 있을 경우, 가장 큰 요금을 부과한다.")
    @ParameterizedTest
    @CsvSource(value = {"9:1450", "10:1450", "11:1550", "15:1550", "16:1650", "50:2250", "58:2350", "59:2450"}, delimiter = ':')
    void calculateFare4(int input, int expected) {
        // given
        Line line = new Line("name", "color", 200);
        Section section1 = new Section(line, 1L, 2L, 3);
        Section section2 = new Section(line, 2L, 3L, 4);
        List<Section> inputSections = new ArrayList<>();
        inputSections.add(section1);
        inputSections.add(section2);
        Sections sections = new Sections(inputSections);

        // when
        ClientFare clientFare = new ClientFare(20, sections, input);
        int result = clientFare.calculateFare();

        // then
        assertThat(result).isEqualTo(expected);
    }
}
