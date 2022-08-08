package springboot.exceptions;

import lombok.Getter;

public enum ErrorCode {
    BAD_REQUEST(400, "9000", "Bad request."),
    UNAUTHORIZED(401, "9001", "Unauthorized."),
    NOT_FOUND(404, "9002", "Not found"),
    INTERNAL_SERVER_ERROR(500, "9003", "Internal server error");

    @Getter private final int httpCode;
    @Getter private final String code;
    @Getter private final String message;

    private ErrorCode(int httpCode, String code, String message) {
        this.httpCode = httpCode;
        this.code = code;
        this.message = message;
    }
}
