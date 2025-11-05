package com.sseudam

import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.TestConstructor
import org.springframework.test.context.TestConstructor.AutowireMode

@ActiveProfiles("test")
@TestConstructor(autowireMode = AutowireMode.ALL)
annotation class RestDocsTest
