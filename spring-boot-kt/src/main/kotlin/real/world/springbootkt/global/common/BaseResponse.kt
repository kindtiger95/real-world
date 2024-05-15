package real.world.springbootkt.global.common

import java.time.OffsetDateTime

data class BaseResponse(
    val code: ErrorCode,
    val timestamp: OffsetDateTime = OffsetDateTime.now(),
    val body: Any
)
