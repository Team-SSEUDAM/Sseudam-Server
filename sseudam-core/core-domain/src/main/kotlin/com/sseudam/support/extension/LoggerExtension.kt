package com.sseudam.support.extension

import io.github.oshai.kotlinlogging.KLogger
import io.github.oshai.kotlinlogging.KotlinLogging

fun logger(): Lazy<KLogger> = lazy { KotlinLogging.logger {} }
