package wooteco.member;

import static org.assertj.core.api.Assertions.assertThat;
import static wooteco.member.auth.AuthAcceptanceTest.로그인되어_있음;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import wooteco.member.controller.dto.request.MemberRequestDto;
import wooteco.member.controller.dto.response.MemberResponseDto;
import wooteco.member.controller.dto.response.LoginTokenResponseDto;
import wooteco.subway.AcceptanceTest;

public class MemberAcceptanceTest extends AcceptanceTest {
    public static final String EMAIL = "email@email.com";
    public static final String PASSWORD = "password";
    public static final int AGE = 20;
    public static final String NEW_EMAIL = "new_email@email.com";
    public static final String NEW_PASSWORD = "new_password";
    public static final int NEW_AGE = 30;

    @DisplayName("회원 정보를 관리한다.")
    @Test
    void manageMember() {
        ExtractableResponse<Response> createResponse = 회원_생성을_요청(EMAIL, PASSWORD, AGE);
        회원_생성됨(createResponse);

        LoginTokenResponseDto 사용자 = 로그인되어_있음(EMAIL, PASSWORD);

        ExtractableResponse<Response> findResponse = 내_회원_정보_조회_요청(사용자);
        회원_정보_조회됨(findResponse, EMAIL, AGE);

        ExtractableResponse<Response> updateResponse = 내_회원_정보_수정_요청(사용자, EMAIL, NEW_PASSWORD, NEW_AGE);
        회원_정보_수정됨(updateResponse);

        ExtractableResponse<Response> deleteResponse = 내_회원_삭제_요청(사용자);
        회원_삭제됨(deleteResponse);
    }

    public static ExtractableResponse<Response> 회원_생성을_요청(String email, String password, Integer age) {
        MemberRequestDto memberRequestDto = new MemberRequestDto(email, password, age);

        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(memberRequestDto)
            .when().post("/api/members")
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 내_회원_정보_조회_요청(LoginTokenResponseDto loginTokenResponseDto) {
        return RestAssured
            .given().log().all()
            .auth().oauth2(loginTokenResponseDto.getAccessToken())
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .when().get("/api/members/me")
            .then().log().all()
            .statusCode(HttpStatus.OK.value())
            .extract();
    }

    public static ExtractableResponse<Response> 내_회원_정보_수정_요청(LoginTokenResponseDto loginTokenResponseDto, String email, String password, Integer age) {
        MemberRequestDto memberRequestDto = new MemberRequestDto(email, password, age);

        return RestAssured
            .given().log().all()
            .auth().oauth2(loginTokenResponseDto.getAccessToken())
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(memberRequestDto)
            .when().put("/api/members/me")
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 내_회원_삭제_요청(LoginTokenResponseDto loginTokenResponseDto) {
        return RestAssured
            .given().log().all()
            .auth().oauth2(loginTokenResponseDto.getAccessToken())
            .when().delete("/api/members/me")
            .then().log().all()
            .extract();
    }

    public static void 회원_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static void 회원_정보_조회됨(ExtractableResponse<Response> response, String email, int age) {
        MemberResponseDto memberResponseDto = response.as(MemberResponseDto.class);
        assertThat(memberResponseDto.getId()).isNotNull();
        assertThat(memberResponseDto.getEmail()).isEqualTo(email);
        assertThat(memberResponseDto.getAge()).isEqualTo(age);
    }

    public static void 회원_정보_수정됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    public static void 회원_삭제됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
