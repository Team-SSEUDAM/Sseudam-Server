package com.sseudam.support.extension

import io.github.oshai.kotlinlogging.KLogger
import io.github.oshai.kotlinlogging.KotlinLogging

inline fun <reified T : Any> T.logger(): Lazy<KLogger> =
    lazy(LazyThreadSafetyMode.NONE) {
        val clazz = T::class.java
        val actual =
            clazz.enclosingClass
                ?.takeIf { clazz.simpleName == "Companion" }
                ?: clazz
        KotlinLogging.logger(actual.name)
    }
