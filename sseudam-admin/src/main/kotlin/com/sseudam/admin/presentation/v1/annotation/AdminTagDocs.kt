package com.sseudam.admin.presentation.v1.annotation

import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.stereotype.Component

@Component
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@Tag(name = "🔐 Admin API", description = "관리자 관련 API 입니다.")
annotation class AdminTagDocs
