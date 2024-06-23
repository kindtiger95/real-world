package real.world.springbootkt.global.common

import com.fasterxml.jackson.annotation.JsonFormat
import java.time.OffsetDateTime

data class BaseResponse(
    val code: ErrorCode,
    val body: Any? = null,
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    val time: OffsetDateTime = OffsetDateTime.now()
)