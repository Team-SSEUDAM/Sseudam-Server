package com.sseudam.common

import jakarta.persistence.Embeddable

@Embeddable
data class Address(
    val zipCode: String,
    val city: String,
    val street: String,
    val detail: String,
)
