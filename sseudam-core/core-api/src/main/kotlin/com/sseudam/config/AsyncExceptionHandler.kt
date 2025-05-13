package com.sseudam.config
import com.sseudam.support.error.ErrorException
import com.sseudam.support.error.ErrorLevel
import com.sseudam.support.extension.logger
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler
import java.lang.reflect.Method

class AsyncExceptionHandler : AsyncUncaughtExceptionHandler {
    private val log by logger()

    override fun handleUncaughtException(
        e: Throwable,
        method: Method,
        vararg params: Any?,
    ) {
        if (e is ErrorException) {
            when (e.errorType.level) {
                ErrorLevel.ERROR -> log.error("ErrorException : {}", e.message, e)
                ErrorLevel.WARN -> log.warn("ErrorException : {}", e.message, e)
                else -> log.info("ErrorException : {}", e.message, e)
            }
        } else {
            log.error("Exception : {}", e.message, e)
        }
    }
}
