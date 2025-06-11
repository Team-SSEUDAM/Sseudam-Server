package com.sseudam.admin.application

import com.sseudam.admin.domain.Admin

interface AdminRepository {
    fun findByLogin(loginId: String): Admin.Info
}
