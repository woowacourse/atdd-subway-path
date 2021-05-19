package wooteco.subway.learningtest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;

import java.security.SecureRandom;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

public class SecureRandomTest {
    int SALT_SIZE = 16;

    @DisplayName("Secure Random의 메소드를 확인하고 16진수로 변경한다.")
    @RepeatedTest(3)
    void secureRandomTest() {
        SecureRandom sRnd = new SecureRandom();
        byte[] randomBytes = new byte[SALT_SIZE];
        sRnd.nextBytes(randomBytes);

        System.out.println(Arrays.toString(randomBytes));
        String saltString = createSaltString(randomBytes);
        System.out.println(saltString);
        assertThat(saltString.length()).isEqualTo(32); // byte를 2글자 16진수로(%02x), 16byte => 32글자
    }

    public static String createSaltString(byte[] randomBytes) {
        StringBuilder stringBuilder = new StringBuilder();
        for (byte a : randomBytes) {
            stringBuilder.append(String.format("%02x", a));
        }
        return stringBuilder.toString();
    }
}
