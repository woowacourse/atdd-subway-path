package wooteco.subway.member.exception;

public enum MemberError {
    NO_MEMBER_EXIST_BY_ID(400, "해당 아이디와 일치하는 회원정보가 없습니다");

    private int statusCode;
    private String message;

    MemberError(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getMessage() {
        return message;
    }
}
