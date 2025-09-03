package com.sseudam.docs.support

object EnumFormattingUtils {
    fun enumFormat(enums: Collection<Any>): String = enums.joinToString(separator = "|")
}
