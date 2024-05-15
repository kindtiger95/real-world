package real.world.springbootkt.global.common

import java.time.OffsetDateTime

enum class ErrorCode(
    val httpCode: Int,
    val code: String,
    val message: String,
    val time: OffsetDateTime = OffsetDateTime.now()
) {
    BAD_REQUEST(400, "9000", "Bad request."),
    UNAUTHORIZED(401, "9001", "Unauthorized."),
    FORBIDDEN(403, "9002", "Forbidden."),
    NOT_FOUND(404, "9002", "Not found"),
    INTERNAL_SERVER_ERROR(500, "9003", "Internal server error");
}
