package com.sseudam

import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.TestConstructor
import org.springframework.test.context.TestConstructor.AutowireMode

@AutoConfigureRestDocs
@ActiveProfiles("test")
@TestConstructor(autowireMode = AutowireMode.ALL)
annotation class RestDocsTest
