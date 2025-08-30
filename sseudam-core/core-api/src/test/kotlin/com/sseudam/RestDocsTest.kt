package com.sseudam

import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.TestConstructor
import org.springframework.test.context.TestConstructor.AutowireMode

@SpringBootTest
@AutoConfigureRestDocs
@ActiveProfiles("local")
@TestConstructor(autowireMode = AutowireMode.ALL)
annotation class RestDocsTest
