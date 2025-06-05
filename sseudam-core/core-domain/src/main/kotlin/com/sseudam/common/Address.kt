package com.sseudam.common

import jakarta.persistence.Embeddable

@Embeddable
data class Address(
    val city: String,
    val detail: String,
)
